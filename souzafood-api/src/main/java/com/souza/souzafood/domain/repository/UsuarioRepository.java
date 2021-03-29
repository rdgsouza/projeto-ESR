package com.souza.souzafood.domain.repository;

import org.springframework.stereotype.Repository;

import com.souza.souzafood.domain.model.Usuario;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {

}