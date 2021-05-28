package com.souza.souzafood.infrastructure.repository.spec;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.souza.souzafood.domain.filter.PedidoFilter;
import com.souza.souzafood.domain.model.Pedido;

public class PedidoSpecs {

	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
//Para entender esse retorno com os parametros root,query e builder veja a aula: https://www.algaworks.com/aulas/1891/criando-uma-fabrica-de-specifications
		return (root, query, builder) -> { //O parametro query é o CriteriaQuery
			if (Pedido.class.equals(query.getResultType())) { //if usado para resolver 
// a exception: org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.QueryException: query specified join fetching, but the owner of the fetched association was not present in the select list [FromElement{explicit,not a collection join,fetch join,fetch non-lazy properties,classAlias=generatedAlias1,role=com.souza.souzafood.domain.model.Pedido.restaurante,tableName=restaurante,tableAlias=restaurant1_,origin=pedido pedido0_,columns={pedido0_.restaurante_id ,className=com.souza.souzafood.domain.model.Restaurante}}] [select count(generatedAlias0) from com.souza.souzafood.domain.model.Pedido as generatedAlias0 inner join fetch generatedAlias0.restaurante as generatedAlias1 inner join fetch generatedAlias1.cozinha as generatedAlias2 inner join fetch generatedAlias0.cliente as generatedAlias3 where generatedAlias0.restaurante=1L]; nested exception is java.lang.IllegalArgumentException: org.hibernate.QueryException: query specified join fetching, but the owner of the fetched association was not present in the select list [FromElement{explicit,not a collection join,fetch join,fetch non-lazy properties,classAlias=generatedAlias1,role=com.souza.souzafood.domain.model.Pedido.restaurante,tableName=restaurante,tableAlias=restaurant1_,origin=pedido pedido0_,columns={pedido0_.restaurante_id ,className=com.souza.souzafood.domain.model.Restaurante}}] [select count(generatedAlias0) from com.souza.souzafood.domain.model.Pedido as generatedAlias0 inner join fetch generatedAlias0.restaurante as generatedAlias1 inner join fetch generatedAlias1.cozinha as generatedAlias2 inner join fetch generatedAlias0.cliente as generatedAlias3 where generatedAlias0.restaurante=1L]
// Aula: https://www.algaworks.com/aulas/2041/desafio-implementando-paginacao-e-ordenacao-de-pedidos
			root.fetch("restaurante").fetch("cozinha");
			root.fetch("cliente");
			}
			
			var predicates = new ArrayList<Predicate>();
			
			if(filtro.getClienteId() != null) {
				predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
			}
			
			if(filtro.getRestauranteId() != null) {
				predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
			}
			
			if(filtro.getDataCriacaoInicio() != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"),
						filtro.getDataCriacaoInicio()));
			}
			
			if(filtro.getDataCriacaoFim() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"),
						filtro.getDataCriacaoFim()));
			}
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
