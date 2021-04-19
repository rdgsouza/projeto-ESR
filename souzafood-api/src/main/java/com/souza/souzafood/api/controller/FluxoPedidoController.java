package com.souza.souzafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.souza.souzafood.api.assembler.PedidoStatusResumoModelAssembler;
import com.souza.souzafood.api.model.PedidoStatusResumoModel;
import com.souza.souzafood.domain.service.EmissaoPedidoService;
import com.souza.souzafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping(value = "/pedidos/{codigoPedido}")
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedido;

	@Autowired
	private PedidoStatusResumoModelAssembler pedidoStatusResumoModelAssembler;
	
	@Autowired
	private EmissaoPedidoService emissaoPedidoService;
	
	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmar(@PathVariable String codigoPedido) {
		fluxoPedido.confirmar(codigoPedido);
	}

	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable String codigoPedido) {
	    fluxoPedido.cancelar(codigoPedido);
	}

	@PutMapping("/entrega")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entregar(@PathVariable String codigoPedido) {
	    fluxoPedido.entregar(codigoPedido);
	}

	@GetMapping("/status")
	public PedidoStatusResumoModel buscar(@PathVariable String codigoPedido) {
		return pedidoStatusResumoModelAssembler.toModel(
			emissaoPedidoService.buscarOuFalhar(codigoPedido));
	}
}
