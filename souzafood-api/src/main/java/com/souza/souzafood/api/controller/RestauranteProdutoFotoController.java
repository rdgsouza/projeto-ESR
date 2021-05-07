package com.souza.souzafood.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.souza.souzafood.api.assembler.FotoProdutoModelAssembler;
import com.souza.souzafood.api.model.FotoProdutoModel;
import com.souza.souzafood.api.model.input.FotoProdutoInput;
import com.souza.souzafood.domain.exception.EntidadeNaoEncontradaException;
import com.souza.souzafood.domain.model.FotoProduto;
import com.souza.souzafood.domain.model.Produto;
import com.souza.souzafood.domain.service.CadastroProdutoService;
import com.souza.souzafood.domain.service.CatalagoFotoProdutoService;
import com.souza.souzafood.domain.service.FotoStorageService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoFotoController {

	@Autowired
	private CadastroProdutoService cadastroProduto;

	@Autowired
	private CatalagoFotoProdutoService catalagoFotoProduto;

	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;

	@Autowired
	private FotoStorageService fotoStorage;
	
	@PutMapping(value = "/{produtoId}/foto",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@Valid FotoProdutoInput fotoProdutoInput) throws IOException {

		Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

		MultipartFile arquivo = fotoProdutoInput.getArquivo();

		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setNomeArquivo(arquivo.getOriginalFilename());

		FotoProduto fotoSalva = catalagoFotoProduto.salvar(foto, arquivo.getInputStream());

		return fotoProdutoModelAssembler.toModel(fotoSalva);

	}

	@GetMapping(value = "/{produtoId}/foto", produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

		FotoProduto fotoProduto = catalagoFotoProduto.buscarOuFalhar(restauranteId, produtoId);

		return fotoProdutoModelAssembler.toModel(fotoProduto);
	}

	@GetMapping(value = "/{produtoId}/foto")
	public ResponseEntity<InputStreamResource> servir(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) 
				    throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = catalagoFotoProduto.buscarOuFalhar(restauranteId, produtoId);
			 
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			List<MediaType> mediatypeAceitas = MediaType.parseMediaTypes(acceptHeader);
// Aula: https://www.algaworks.com/aulas/2066/checando-media-type-ao-servir-arquivos-de-fotos					
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediatypeAceitas);
			
			InputStream inputStream = fotoStorage.recuperar(fotoProduto.getNomeArquivo());
			return ResponseEntity.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(inputStream));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
 
	}
	
	@GetMapping(value = "/fotos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FotoProdutoModel> buscarFotos(@PathVariable Long restauranteId) {
		List<FotoProduto> fotoProdutos = catalagoFotoProduto.buscarTodos(restauranteId);
		
		return fotoProdutoModelAssembler.toModelList(fotoProdutos);
	}

	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, 
			List<MediaType> mediatypeAceitas) throws HttpMediaTypeNotAcceptableException {
		
		boolean compativel = mediatypeAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		if(!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediatypeAceitas);
		}
	}
	
	@DeleteMapping(value = "/{produtoId}/foto")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId) {
      catalagoFotoProduto.excluir(restauranteId, produtoId);
	}
	
}
