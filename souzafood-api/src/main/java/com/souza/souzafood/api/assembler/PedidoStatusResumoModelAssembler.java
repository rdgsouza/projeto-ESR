package com.souza.souzafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.souza.souzafood.api.model.PedidoStatusResumoModel;
import com.souza.souzafood.domain.model.Pedido;

@Component
public class PedidoStatusResumoModelAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public PedidoStatusResumoModel toModel(Pedido pedido) {
		return modelMapper.map(pedido, PedidoStatusResumoModel.class);
	}

	public List<PedidoStatusResumoModel> toCollectionModel(List<Pedido> pedidos) {
		return pedidos.stream()
				.map(pedido -> toModel(pedido))
				.collect(Collectors.toList());
	}
}
