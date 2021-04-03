package com.souza.souzafood.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioModel {

    private Long id;
    private String nome;
    private String email;
//  Caso queira que os grupos com suas respectivas permissões apareçam na listagem dos usuarios só
//  colocar a propiedade grupos
//	private Set<Grupo> grupos = new HashSet<>();
}             