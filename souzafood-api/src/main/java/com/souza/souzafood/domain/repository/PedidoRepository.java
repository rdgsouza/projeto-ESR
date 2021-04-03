package com.souza.souzafood.domain.repository;

import org.springframework.stereotype.Repository;

import com.souza.souzafood.domain.model.Pedido;

@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long> {

}
