package com.souza.souzafood.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrupoModel {

    private Long id;
    private String nome;
// Caso queira listar as permissões na hora de listar os grupos apenas adicione a propiedade 
// que voce quer que apareça, automaticamente sera inclusa porque tem o mapeamento 
// muitos para muitos na entidade grupo 
// private Set<Permissao> permissoes = new HashSet<>();
}