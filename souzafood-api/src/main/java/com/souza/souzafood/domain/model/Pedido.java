package com.souza.souzafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Pedido {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;

	@Embedded
	private Endereco enderecoEntrega;

	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;

	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataCriacao;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataConfirmacao;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataCancelamento;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime dataEntrega;

	@ManyToOne(fetch = FetchType.LAZY)//Alteramos a estratégia de fetch para lazy porque nem sempre
	//nos vamos precisar da forma de pagamento na consulta por exemplo quando for consultar todas os
	//pedidos no não colocamos na entidade PedidoResumoModel para trazer as formas de pagamentos pois
	//é uma entidade que é usado para uma consulta de collection resource colocmos a forma de pagamento
	//apenas na entidade PedidoModel que é usada para consulta sigleton resorce.
	//Sendo assim podemos colocar a estratégia fetch LAZY
	//Caso tenha dúvida veja as aulas: https://www.algaworks.com/aulas/1904/entendendo-o-lazy-loading
	//e https://www.algaworks.com/aulas/2026/otimizando-a-query-de-pedidos-e-retornando-model-resumido-na-listagem
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;

	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;

	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens = new ArrayList<>();

//	OBS*** No script de migração V007__criar-tabelas-pedido-item-pedido.sql temos uma chave única na 
//	tabela item_pedido:  unique key uk_item_pedido_produto (pedido_id, produto_id)
//	Essa chave única é para garantir que o usuário na hora do cadastro do pedido não haja repetição 
//	de produto várias vezes no itemPedido. Isso impede que o usuário cadastre, por exemplo, 
//	pedido 1132 com o item-pedido, produto = xsalada com quantidade 3 e 
//	logo depois adicione o mesmo item-pedido, produto = xsalada com quantidade 1, ao invés de cadastrar 
//	pedido 1132 adionando o item-pedido, produto = xsalada com quantidade 4. Isso gera uma melhor
//	consistência nos dados, o que facilitaria, por exemplo, a realização de uma auditoria no sistema.
//  fonte resposta de Alexandre Moraes: https://www.algaworks.com/forum/topicos/83459/duvida-no-script-sql-do-conteudo-de-apoio#87766

	public void calcularValorTotal() {
		this.subtotal = getItens().stream()
		.map(item -> item.getPrecoTotal())
		.reduce(BigDecimal.ZERO, BigDecimal::add);
		this.valorTotal = this.subtotal.add(this.taxaFrete);
	}

	public void definirFrete() {
		setTaxaFrete(getRestaurante().getTaxaFrete());
	}

	public void atribuirPedidoAosItens() {
		getItens().forEach(item -> item.setPedido(this));
	}
}
