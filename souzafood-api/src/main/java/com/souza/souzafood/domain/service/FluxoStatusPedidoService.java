package com.souza.souzafood.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.model.StatusPedido;

@Service
public class FluxoStatusPedidoService {

	@Autowired
	private EmissaoPedidoService emissaoPedido;

	
	@Transactional
	public List<String> verificaTodosStatus2(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

 // **** Verificar uma forma de não adiconar pedido que estiverem com data de criação nulas
		List<String> listaPedido = new ArrayList<>();

		listaPedido.add("Status: Criado. Data da criação: " + pedido.getDataCriacao());
		listaPedido.add("Status: Confirmado. Data da confirmação: " + pedido.getDataConfirmacao());
		listaPedido.add("Status: Entregue. Data da entrega: " + pedido.getDataEntrega());
		listaPedido.add("Status: Cancelado. Data da cancelamento: " + pedido.getDataCancelamento());

		return listaPedido;
	}

	@Transactional
	public List<Pedido> verificaTodosStatus(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		List<Object> listaPedido = new ArrayList<>();
		List<Pedido> listaPedidoStatusEdata = new ArrayList<>();

		if (!listaPedido.contains(pedido.getDataCriacao()) && pedido.getDataCriacao() != null) {
			Pedido pedidoCriado = new Pedido();
			pedidoCriado.setDataCriacao(pedido.getDataCriacao());
			pedidoCriado.setStatus(StatusPedido.CRIADO);
			listaPedido.add(pedidoCriado);
		}

		if (!listaPedido.contains(pedido.getDataConfirmacao()) && pedido.getDataConfirmacao() != null) {
			Pedido pedidoConfirmado = new Pedido();
			pedidoConfirmado.setDataConfirmacao(pedido.getDataConfirmacao());
			pedidoConfirmado.setStatus(StatusPedido.CONFIRMADO);
			listaPedido.add(pedidoConfirmado);
		}

		if (!listaPedido.contains(pedido.getDataEntrega()) && pedido.getDataEntrega() != null) {
			Pedido pedidoEntregue = new Pedido();
			pedidoEntregue.setDataEntrega(pedido.getDataEntrega());
			pedidoEntregue.setStatus(StatusPedido.ENTREGUE);
			listaPedido.add(pedidoEntregue);
		}

		if (!listaPedido.contains(pedido.getDataCancelamento()) && pedido.getDataCancelamento() != null) {
			Pedido pedidoCancelado = new Pedido();
			pedidoCancelado.setDataCancelamento(pedido.getDataCancelamento());
			pedidoCancelado.setStatus(StatusPedido.CANCELADO);
			listaPedido.add(pedidoCancelado);
		}

		listaPedidoStatusEdata = listaPedido.stream()
				.map(element -> (Pedido) element)
				.collect(Collectors.toList());

		return listaPedidoStatusEdata;
	}

}
