package com.souza.souzafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel","Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso","Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	PARAMETRO_INVALIDO("/parametro-invalido","Parâmetro inválido"),
	ERRO_DE_SISTEMA("/erro-de-sistema","Erro de sistema"),
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos");
	
	private String uri;
	private String title;
	
	ProblemType(String path, String title) {
		this.uri = "https://souzafood.com.br" + path;
		this.title = title;
	}
}
