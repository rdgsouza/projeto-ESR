package com.souza.souzafood.api.model.input;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.souza.souzafood.api.model.EnderecoModel;
import com.souza.souzafood.api.model.FormaPagamentoModel;
import com.souza.souzafood.api.model.ItemPedidoModel;
import com.souza.souzafood.api.model.RestauranteResumoModel;
import com.souza.souzafood.api.model.UsuarioModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoModel {

	private Long id;
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	private String status;
	private OffsetDateTime dataCriacao;
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataEntrega;
	private OffsetDateTime dataCancelamento;
	private RestauranteResumoModel restaurante;
	private UsuarioModel cliente;
	private FormaPagamentoModel formaPagamento;
	private EnderecoModel enderecoEntrega;
	private List<ItemPedidoModel> itens;
}
