package com.souza.souzafood.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.souza.souzafood.domain.exception.EntidadeNaoEncontradaException;
import com.souza.souzafood.domain.service.FotoStorageService;

@Controller
@RequestMapping("/home/rodrigo/Documents/catalago/{nomeArquivo}")
public class FotoController {

	@Autowired
	private FotoStorageService fotoStorage;

	
	@GetMapping
	public ResponseEntity<InputStreamResource> servirFoto(@PathVariable String nomeArquivo,
			@RequestHeader(name = "accept") String acceptHeader)  
					throws IOException , HttpMediaTypeNotAcceptableException {
		try {
			
		//Como não temos uma propiedade do tipo FotoProduto para fazer um fotoProduto.getContentType()
		//Pegamos o contentType do arquivo a partir do nome do arquivo usando o metodo retornaMediaType
			MediaType mediaTypeFoto = fotoStorage.retornaMediaType(nomeArquivo);
			List<MediaType> mediatypeAceitas = MediaType.parseMediaTypes(acceptHeader);
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediatypeAceitas);
			
			InputStream inputStream = fotoStorage.recuperar(nomeArquivo);
            
			return ResponseEntity.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(inputStream));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, 
			List<MediaType> mediatypeAceitas) throws HttpMediaTypeNotAcceptableException {
		
		boolean compativel = mediatypeAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		if(!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediatypeAceitas);
		}
	}

}
