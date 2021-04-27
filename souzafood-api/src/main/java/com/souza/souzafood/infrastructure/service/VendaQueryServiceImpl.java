package com.souza.souzafood.infrastructure.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.souza.souzafood.domain.filter.VendaDiariaFilter;
import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.model.StatusPedido;
import com.souza.souzafood.domain.model.dto.VendaDiaria;
import com.souza.souzafood.domain.service.VendaQueryService;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

	@PersistenceContext
	private EntityManager manager;

//	Aula: https://www.algaworks.com/aulas/2046/implementando-consulta-com-dados-agregados-de-vendas-diarias
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {

		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);

		var predicates = new ArrayList<Predicate>();

		if (filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
		
		if (filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"),
					filtro.getDataCriacaoFim()));
		}
		
		if (filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), 
					filtro.getDataCriacaoFim()));
		}

		predicates.add(root.get("status").in(
				StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		
		query.where(predicates.toArray(new Predicate[0]));//Transformando uma collection em array
//		Aula que explica sobre essa transformação: https://www.algaworks.com/aulas/1888/tornando-a-consulta-com-criteria-api-com-filtros-dinamicos
		
		var functionDateDataCriacao = builder.function("date", Date.class, 
				root.get("dataCriacao"));

		var selection = builder.construct(VendaDiaria.class, functionDateDataCriacao, 
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));

		query.select(selection);
		query.groupBy(functionDateDataCriacao);

		return manager.createQuery(query).getResultList();
	}

}
