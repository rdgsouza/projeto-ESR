package com.souza.souzafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.model.FotoProduto;
import com.souza.souzafood.domain.repository.ProdutoRepository;

@Service
public class CatalagoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto) {
	
		return produtoRepository.save(foto);
	}
}