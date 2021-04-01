package com.souza.souzafood.api.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestauranteModel {

	private Long id;
	private String nome;
	private BigDecimal taxaFrete;
	private CozinhaModel cozinha;
	private Boolean ativo;
	private EnderecoModel endereco;
	private Boolean aberto;

//  Caso queira listar os produtos relecionados ao restaurante na hora de listar os restaurantes coloque a 
//	propiedade private List<Produto> produtos = new ArrayList<>(); e anotação:
//	JsonIgnoreProperties("restaurante") para que não entre em um loop infinito.	
//	@JsonIgnoreProperties("restaurante")
//	private List<Produto> produtos = new ArrayList<>();

//	private Set<FormaPagamento> formasPagamento = new HashSet<>();
//  Caso queira listar as forma de pagamentos relacionados ao restaurante na hora de listar os restaurantes 
//	apenas adicione a propiedade formasPagamento, em um relacionamento muitos para muitos ao colocar a propiedade
//	que faz esse tipo de relacionamento no caso a propiedade 'formasPagamento' o JPA
//	de forma automatica faz o mapeamento trazendo as formas de pagamentos com seus respectivos relacionamentos 
//	com restaurantes essa forma de adiconar apenas a propiedade para trazer os relacionamentos é possível graças
//	ao relacionamento muitos para muitos 

}
