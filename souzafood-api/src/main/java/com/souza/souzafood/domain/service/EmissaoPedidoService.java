package com.souza.souzafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.exception.PedidoNaoEncontradoException;
import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}
}