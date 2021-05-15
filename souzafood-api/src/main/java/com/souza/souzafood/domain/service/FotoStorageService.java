package com.souza.souzafood.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.http.MediaType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface FotoStorageService {

	void armazenar(NovaFoto novaFoto);
	
	FotoRecuperada recuperar(String nomeArquivo);
	
	void remover(String nomeArquivo);

	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		this.armazenar(novaFoto);

		if (nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
	}

	default String pegarExtensaoArquivo(String nomeOriginalArquivo) {
        if (nomeOriginalArquivo.contains("."))
            return nomeOriginalArquivo.substring(nomeOriginalArquivo.lastIndexOf(".") + 1);
        else
            return "";
    }
	
	default String gerarNovoNomeArquivo(String nomeOriginalArquivo) throws IOException {
		
		return UUID.randomUUID().toString() + "_n." + pegarExtensaoArquivo(nomeOriginalArquivo);
	}

	default MediaType returnMediaType(String nomeArquivo, Path caminhoArquivo) throws IOException {
		
		String contentType = Files.probeContentType(Path.of(caminhoArquivo.toString()));
		MediaType mediaType = MediaType.parseMediaType(contentType);

		return mediaType;
	
	}
	
	@Builder
	@Getter
	class NovaFoto {

		private String nomeArquivo;
		private String contentType;
		private InputStream inputStream;
	}

	@Builder
	@Getter
	@Setter
	class FotoRecuperada {
		
		private InputStream inputStream;
		private String url;
		
		public boolean temUrl() {
			return url != null;
		}
		
		public boolean temInputStream() {
			return inputStream != null;
		}
		
	}
}
