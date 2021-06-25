package com.souza.souzafood.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrupoModel {

	@ApiModelProperty(example = "1")
    private Long id;
	
	@ApiModelProperty(example = "Gerente")
    private String nome;
// Caso queira listar as permissões na hora de listar os grupos apenas adicione a propiedade 
// que voce quer que apareça, automaticamente sera inclusa porque tem o mapeamento 
// muitos para muitos na entidade grupo 
// private Set<Permissao> permissoes = new HashSet<>();
}