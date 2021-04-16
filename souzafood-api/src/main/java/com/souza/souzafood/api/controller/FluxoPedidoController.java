package com.souza.souzafood.api.controller;

import java.util.List;

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
import com.souza.souzafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping(value = "/pedidos/{pedidoId}")
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedido;

	@Autowired
	private PedidoStatusResumoModelAssembler pedidoStatusResumoModelAssembler;
	
	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmar(@PathVariable Long pedidoId) {
		fluxoPedido.confirmar(pedidoId);
	}

	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long pedidoId) {
	    fluxoPedido.cancelar(pedidoId);
	}

	@PutMapping("/entrega")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entregar(@PathVariable Long pedidoId) {
	    fluxoPedido.entregar(pedidoId);
	}

//	Get para o metodo retornaTodosStatusEmObjetos
	@GetMapping("/status")
	public List<PedidoStatusResumoModel> buscar(@PathVariable Long pedidoId) {
		return pedidoStatusResumoModelAssembler
				.toCollectionModel(fluxoPedido.retornaTodosStatusEmArrayDeObjetos(pedidoId));
	}

//	Get para o metodo retornaTodosStatusEmArray
//	@GetMapping("/status")
//	public List<String> buscar(@PathVariable Long pedidoId) {
//		return fluxoPedido.retornaTodosStatusEmArrayDeString(pedidoId);
//	}

}
