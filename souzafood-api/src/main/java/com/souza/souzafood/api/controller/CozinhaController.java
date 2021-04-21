package com.souza.souzafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.souza.souzafood.api.assembler.CozinhaInputDisassembler;
import com.souza.souzafood.api.assembler.CozinhaModelAssembler;
import com.souza.souzafood.api.model.CozinhaModel;
import com.souza.souzafood.api.model.input.CozinhaInput;
import com.souza.souzafood.domain.model.Cozinha;
import com.souza.souzafood.domain.repository.CozinhaRepository;
import com.souza.souzafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;
	
	@GetMapping
	public List<CozinhaModel> listar(Pageable pageable) {
		       Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
			return cozinhaModelAssembler.toCollectionModel(cozinhasPage.getContent());
	}
	
	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		 Cozinha cozinha =  cadastroCozinha.buscarOuFalhar(cozinhaId);
		 return cozinhaModelAssembler.toModel(cozinha);	
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid Cozinha cozinha) {
		 return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));	
	}
	
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId, 
			@RequestBody @Valid CozinhaInput cozinhaInput) {
		
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);	
			
			return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual)); 	
		}
	
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {     	
     	      cadastroCozinha.excluir(cozinhaId); 
	}
	
	
//	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<Cozinha> listar1() {
//		System.out.println("LISTAR 1");
//			return cozinhaRepository.listar();	
//	}
//	
//	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
//	public List<Cozinha> listar2() {
//		System.out.println("LISTAR 2");
//			return cozinhaRepository.listar();	
//	}
} 
