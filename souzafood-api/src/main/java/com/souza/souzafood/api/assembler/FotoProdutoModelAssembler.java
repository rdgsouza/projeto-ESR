package com.souza.souzafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.souza.souzafood.api.model.FotoProdutoModel;
import com.souza.souzafood.domain.model.FotoProduto;

@Component
public class FotoProdutoModelAssembler {

	@Autowired
	private ModelMapper modelMapper;

	public FotoProdutoModel toModel(FotoProduto foto) {
		return modelMapper.map(foto, FotoProdutoModel.class);
	}

	public List<FotoProdutoModel> toModelList(List<FotoProduto> fotos) {
		return fotos.stream().map(this::toModel).collect(Collectors.toList());
	}
}