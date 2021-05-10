package com.souza.souzafood.domain.service;

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

	default String pegarExtensaoArquivo(String nomeOriginalArquivo) {
        if (nomeOriginalArquivo.contains("."))
            return nomeOriginalArquivo.substring(nomeOriginalArquivo.lastIndexOf(".") + 1);
        else
            return "";
    }
	
	default String gerarNovoNomeArquivo(String nomeOriginalArquivo) {
		
		return UUID.randomUUID().toString() + "_n." + pegarExtensaoArquivo(nomeOriginalArquivo);
	}

	public MediaType retornaMediaType(String nomeArquivo);
	
	@Builder
	@Getter
	class NovaFoto {

		private String nomeArquivo;
		private InputStream inputStream;
	}
}
