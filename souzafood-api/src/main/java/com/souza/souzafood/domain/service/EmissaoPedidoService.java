package com.souza.souzafood.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.exception.NegocioException;
import com.souza.souzafood.domain.exception.PedidoNaoEncontradoException;
import com.souza.souzafood.domain.model.Cidade;
import com.souza.souzafood.domain.model.FormaPagamento;
import com.souza.souzafood.domain.model.ItemPedido;
import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.model.Produto;
import com.souza.souzafood.domain.model.Restaurante;
import com.souza.souzafood.domain.model.Usuario;
import com.souza.souzafood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@Autowired
	private CadastroUsuarioService cadastroUsuario;

	@Autowired
	private CadastroProdutoService cadastroProduto;

	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;

	@Transactional
	public Pedido emitir(Pedido pedido) {
		validarPedido(pedido);
		normalizarItens(pedido);
		validarItens(pedido);

		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValorTotal();

		return pedidoRepository.save(pedido);
	}

	private void validarPedido(Pedido pedido) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());
		Usuario cliente = cadastroUsuario.buscarOuFalhar(pedido.getCliente().getId());
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(pedido.getFormaPagamento().getId());

		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);

		if (restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
					formaPagamento.getDescricao()));
		}
	}

	private void normalizarItens(Pedido pedido) {
		List<ItemPedido> itensNormalizados = new ArrayList<>();

		for (ItemPedido item : pedido.getItens()) {
			Optional<ItemPedido> itemNormalizadoOpt = itensNormalizados.stream()
					.filter(itemNormalizado -> itemNormalizado.getProduto().equals(item.getProduto())).findFirst();

			itemNormalizadoOpt.ifPresentOrElse(itemNormalizado -> mesclarItens(itemNormalizado, item),
					() -> itensNormalizados.add(item));
		}

		pedido.setItens(new ArrayList<>(itensNormalizados));
	}

	private void mesclarItens(ItemPedido itemNormalizado, ItemPedido itemRepetido) {
		itemNormalizado.setQuantidade(itemNormalizado.getQuantidade() + itemRepetido.getQuantidade());
// ifs criados para pegar apenas a propiedade observacao que não estiver nula. Caso tenha duas propiedades nulas
// vamos setar apenas um null caso as propidades estejam com dua observações ai sim vamos concatenar
// as string das duas propiedades para ficar duas observações em uma. Já que o cliente informou a observação uma
// vez e depois resolver adicionar o mesmo item e adicionou outra observação sendo que são os mesmos itens.		
		if (itemNormalizado.getObservacao() == null && itemRepetido.getObservacao() != null) {
			itemNormalizado.setObservacao(itemRepetido.getObservacao());
		} else if (itemNormalizado.getObservacao() != null && itemRepetido.getObservacao() == null) {
			itemNormalizado.setObservacao(itemNormalizado.getObservacao());
		} else if (itemNormalizado.getObservacao() == null && itemRepetido.getObservacao() == null) {
			itemNormalizado.setObservacao(null);
		} else if (itemNormalizado.getObservacao() != null && itemRepetido.getObservacao() != null) {
			itemNormalizado.setObservacao(itemNormalizado.getObservacao() + " / " + itemRepetido.getObservacao());
		}
	} 

	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			Produto produto = cadastroProduto.buscarOuFalhar(pedido.getRestaurante().getId(),
					item.getProduto().getId());

			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
	}

	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}

}
