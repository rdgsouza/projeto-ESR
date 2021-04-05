package com.souza.souzafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.souza.souzafood.api.assembler.PedidoModelAssembler;
import com.souza.souzafood.api.assembler.PedidoResumoModelAssembler;
import com.souza.souzafood.api.model.PedidoModel;
import com.souza.souzafood.api.model.PedidoResumoModel;
import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.repository.PedidoRepository;
import com.souza.souzafood.domain.service.EmissaoPedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EmissaoPedidoService emissaoPedido;
    
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;

	@GetMapping
	public List<PedidoResumoModel> listar() {
		List<Pedido> todosPedidos = pedidoRepository.findAll();

		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
	}

	@GetMapping("/{pedidoId}")
	public PedidoModel buscar(@PathVariable Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		return pedidoModelAssembler.toModel(pedido);
	}
}
