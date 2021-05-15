package com.souza.souzafood.api.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
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
import com.souza.souzafood.domain.service.FotoStorageService.FotoRecuperada;
import com.souza.souzafood.infrastructure.service.storage.StorageException;

@Controller
@RequestMapping("/home/rodrigo/Documents/catalago/{nomeArquivo}")
public class FotoController {

	@Autowired
	private FotoStorageService fotoStorage;

	private FotoRecuperada fotoRecuperada;
	
	@GetMapping
	public ResponseEntity<InputStreamResource> servirFoto(@PathVariable String nomeArquivo,
			@RequestHeader(name = "accept") String acceptHeader)
			throws IOException, HttpMediaTypeNotAcceptableException {
		
		fotoRecuperada = fotoStorage.recuperar(nomeArquivo);

		try {

			MediaType mediaTypeFoto = retornaTipoDeMediaType(fotoRecuperada.getInputStream());
			List<MediaType> mediatypeAceitas = MediaType.parseMediaTypes(acceptHeader);

			verificarCompatibilidadeMediaType(mediaTypeFoto, mediatypeAceitas);

			return ResponseEntity
					.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(fotoRecuperada.getInputStream()));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediatypeAceitas)
			throws HttpMediaTypeNotAcceptableException {

		boolean compativel = mediatypeAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediatypeAceitas);
		}
	}

	private MediaType retornaTipoDeMediaType(InputStream inputStream) {

		try {
			byte[] bytes = inputStream.readAllBytes();

			InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			String mediaType = URLConnection.guessContentTypeFromStream(byteArrayInputStream);
			MediaType mediaTypeFoto = MediaType.parseMediaType(mediaType);
			
			inputStream = new ByteArrayInputStream(bytes); //Converte novamente os bytes novamente para InputStream
			fotoRecuperada.setInputStream(inputStream);//Precisa setar novamente o inputStream convertido se não ele
			                                          //vai ficar vazio pois ele ainda estar em formato de byte 

			return mediaTypeFoto;

		} catch (Exception e) {

			throw new StorageException("Não foi possível obter o tipo de mídia do arquivo.", e);
		}
	}

}
