package com.souza.souzafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Getter
@Builder //Builder é um padrão de projeto pra construir objeto em uma linguagem mas fluente. 
//Pra mas informações veja essas duas aulas onde usamos o builder: https://www.algaworks.com/aulas/1931/tratando-excecoes-em-nivel-de-controlador-com-exceptionhandler
//e https://www.algaworks.com/aulas/1947/estendendo-o-formato-do-problema-para-adicionar-novas-propriedades
//e https://www.algaworks.com/aulas/1952/estendendo-o-problem-details-para-adicionar-as-propriedades-com-constraints-violadas
//e https://www.algaworks.com/aulas/1966/ajustando-exception-handler-para-adicionar-mensagens-de-validacao-em-nivel-de-classe
public class Problem {				

	private Integer status;
	private String type;
	private String title;
	private String detail;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") 
	private OffsetDateTime timestamp;
	
	private String userMessage;
	private List<Object> objects;

	@Getter
	@Builder
	public static class Object {

		private String name;
		private String userMessage;
	}

}
