package com.souza.souzafood.domain.service;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

public interface EnvioEmailService {

	void enviar(Mensagem mensagem);

	@Getter
	@Builder
	class Mensagem {
		
		@Singular
		private Set<String> destinatarios;
		
		@NonNull
		private String assunto;
		
		@NonNull
		private String corpo;
		
//		Aula: https://www.algaworks.com/aulas/2084/processando-template-do-corpo-de-e-mails-com-apache-freemarker
		@Singular("variavel")
		private Map<String, Object> variaveis;
		
	}
}
