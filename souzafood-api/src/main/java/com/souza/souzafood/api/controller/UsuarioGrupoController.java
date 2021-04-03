package com.souza.souzafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.souza.souzafood.api.assembler.GrupoModelAssembler;
import com.souza.souzafood.api.model.GrupoModel;
import com.souza.souzafood.domain.model.Usuario;
import com.souza.souzafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping(value = "/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {

	@Autowired
	private CadastroUsuarioService cadastroUsuario;

	@Autowired
	private GrupoModelAssembler grupoModelAssembler;

	@GetMapping
	public List<GrupoModel> listar(@PathVariable Long usuarioId) {

		Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

		return grupoModelAssembler.toCollectionModel(usuario.getGrupos());
	}

	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuario.desassociarPermissao(usuarioId, grupoId);
	}

	@PutMapping("/{grupoId}")
	public void associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuario.associarPermissao(usuarioId, grupoId);
	}

}
