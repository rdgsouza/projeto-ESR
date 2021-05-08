package com.souza.souzafood.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.http.MediaType;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	void armazenar(NovaFoto novaFoto);
	
	void remover(String nomeArquivo);

	InputStream recuperar(String nomeArquivo);

	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		this.armazenar(novaFoto);

		if (nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
	}

	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
	}

	public MediaType retornaMediaType(String nomeArquivo) throws IOException;
	
	@Builder
	@Getter
	class NovaFoto {

		private String nomeArquivo;
		private InputStream inputStream;
	}
}
