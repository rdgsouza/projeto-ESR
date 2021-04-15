package com.souza.souzafood.domain.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.exception.NegocioException;
import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.model.StatusPedido;

@Service
public class FluxoPedidoService {

	@Autowired
	private EmissaoPedidoService emissaoPedido;

	@Transactional
	public void confirmar(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		if (!pedido.getStatus().equals(StatusPedido.CRIADO)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser alterado de %s para %s",
					pedido.getId(), pedido.getStatus().getDescricao(), StatusPedido.CONFIRMADO.getDescricao()));
		}

		pedido.setStatus(StatusPedido.CONFIRMADO);
		pedido.setDataConfirmacao(OffsetDateTime.now());
	}

	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		if (!pedido.getStatus().equals(StatusPedido.CRIADO)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser alterado de %s para %s",
					pedido.getId(), pedido.getStatus().getDescricao(), StatusPedido.CANCELADO.getDescricao()));
		}

		pedido.setStatus(StatusPedido.CANCELADO);
		pedido.setDataCancelamento(OffsetDateTime.now());
	}

	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		if (!pedido.getStatus().equals(StatusPedido.CONFIRMADO)) {
			throw new NegocioException(
					String.format("Status do pedido %d não pode ser alterado de %s para %s",
					pedido.getId(), pedido.getStatus().getDescricao(), StatusPedido.ENTREGUE.getDescricao()));
		}

		pedido.setStatus(StatusPedido.ENTREGUE);
		pedido.setDataEntrega(OffsetDateTime.now());
	}
	
//	Desafio: Usar Stream e Lambda no método abaixo para diminuir as linhas de códigos 
	@Transactional
	public List<Pedido> retornaTodosStatusEmObjetos(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		List<Object> pedidosStatusObject = new ArrayList<>();
		List<Pedido> pedidosStatus = new ArrayList<>();

		if (!pedidosStatusObject.contains(pedido.getDataCriacao()) && pedido.getDataCriacao() != null) {
			Pedido pedidoCriado = new Pedido();
			pedidoCriado.setDataCriacao(pedido.getDataCriacao());
			pedidoCriado.setStatus(StatusPedido.CRIADO);
			pedidosStatusObject.add(pedidoCriado);
		}

		if (!pedidosStatusObject.contains(pedido.getDataConfirmacao()) && pedido.getDataConfirmacao() != null) {
			Pedido pedidoConfirmado = new Pedido();
			pedidoConfirmado.setDataConfirmacao(pedido.getDataConfirmacao());
			pedidoConfirmado.setStatus(StatusPedido.CONFIRMADO);
			pedidosStatusObject.add(pedidoConfirmado);
		}

		if (!pedidosStatusObject.contains(pedido.getDataEntrega()) && pedido.getDataEntrega() != null) {
			Pedido pedidoEntregue = new Pedido();
			pedidoEntregue.setDataEntrega(pedido.getDataEntrega());
			pedidoEntregue.setStatus(StatusPedido.ENTREGUE);
			pedidosStatusObject.add(pedidoEntregue);
		}

		if (!pedidosStatusObject.contains(pedido.getDataCancelamento()) && pedido.getDataCancelamento() != null) {
			Pedido pedidoCancelado = new Pedido();
			pedidoCancelado.setDataCancelamento(pedido.getDataCancelamento());
			pedidoCancelado.setStatus(StatusPedido.CANCELADO);
			pedidosStatusObject.add(pedidoCancelado);
		}

		pedidosStatus = pedidosStatusObject
				.stream()
				.map(element -> (Pedido) element).collect(Collectors.toList());

		return pedidosStatus;
	}
	
//	Desafio: Usar Stream e Lambda no método abaixo para diminuir as linhas de códigos 
	@Transactional
	public List<String> retornaTodosStatusEmArray(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		List<Object> pedidosStatusObject = new ArrayList<>();
		List<String> pedidosStatus = new ArrayList<>();

		if (!pedidosStatusObject.contains(pedido.getDataCriacao()) && pedido.getDataCriacao() != null) {
			pedidosStatusObject.add("Status: 'Criado' Data da criação: " + pedido.getDataCriacao());
		}

		if (!pedidosStatusObject.contains(pedido.getDataConfirmacao()) && pedido.getDataConfirmacao() != null) {
			pedidosStatusObject.add("Status: 'Confirmado' Data da confirmação: " + pedido.getDataConfirmacao());
		}

		if (!pedidosStatusObject.contains(pedido.getDataEntrega()) && pedido.getDataEntrega() != null) {
			pedidosStatusObject.add("Status: 'Entregue' Data da entrega: " + pedido.getDataEntrega());
		}

		if (!pedidosStatusObject.contains(pedido.getDataCancelamento()) && pedido.getDataCancelamento() != null) {
			pedidosStatusObject.add("Status: 'Cancelado' Data do cancelamento: " + pedido.getDataCancelamento());
		}

		pedidosStatus = pedidosStatusObject
				.stream()
				.map(element -> (String) element).collect(Collectors.toList());

		return pedidosStatus;
	}

}
