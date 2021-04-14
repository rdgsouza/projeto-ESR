package com.souza.souzafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.souza.souzafood.api.assembler.PedidoResumoModelAssembler;
import com.souza.souzafood.api.model.PedidoResumoModel;
import com.souza.souzafood.domain.service.FluxoStatusPedidoService;

@RestController
@RequestMapping(value = "/pedidos/{pedidoId}/status")
public class FluxoStatusPedidoController {

	@Autowired
	private FluxoStatusPedidoService fluxoStatusPedido;

	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@GetMapping
	public List<PedidoResumoModel> buscar(@PathVariable Long pedidoId) {
		return pedidoResumoModelAssembler.toCollectionModel(
				fluxoStatusPedido.verificaTodosStatus(pedidoId));
		
	}

}
