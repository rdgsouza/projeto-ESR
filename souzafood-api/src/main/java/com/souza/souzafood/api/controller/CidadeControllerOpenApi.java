package com.souza.souzafood.api.controller;

import java.util.List;

import com.souza.souzafood.api.exceptionhandler.Problem;
import com.souza.souzafood.api.model.CidadeModel;
import com.souza.souzafood.api.model.input.CidadeInput;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Cidades")
public interface CidadeControllerOpenApi {

	@ApiOperation("Lista as cidades")
	public List<CidadeModel> listar(); 
	
	@ApiOperation("Busca uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Id da cidade inválido", response = Problem.class),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})

	public CidadeModel buscar(
			@ApiParam(value = "ID de uma cidade", example = "1") 
			Long cidadeId);

	@ApiOperation("Cadastra uma cidade")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Cidade Cadastrada"),
	})

	public CidadeModel adicionar(
			@ApiParam(name = "corpo", value = "Representação de uma cidade")
			CidadeInput cidadeInput);
	
	@ApiOperation("Atualiza uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Cidade Atualizada"),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})

	public CidadeModel atualizar(
			@ApiParam(value = "ID de uma nova cidade", example = "1") 
        	Long cidadeId, 
		
        	@ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
			CidadeInput cidadeInput);
	
	@ApiOperation("Exclui uma cidade por ID")
	@ApiResponses({
		@ApiResponse(code = 204, message = "Cidade excluída"),
		@ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
	})

	public void remover(
			@ApiParam(value = "ID de uma cidade", example = "1")
			Long cidadeId);
}