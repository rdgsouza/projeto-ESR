package com.souza.souzafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.souza.souzafood.api.assembler.PedidoModelAssembler;
import com.souza.souzafood.api.assembler.PedidoResumoModelAssembler;
import com.souza.souzafood.api.model.PedidoModel;
import com.souza.souzafood.api.model.PedidoResumoModel;
import com.souza.souzafood.api.model.input.PedidoInput;
import com.souza.souzafood.api.model.input.PedidoInputDisassembler;
import com.souza.souzafood.domain.exception.EntidadeNaoEncontradaException;
import com.souza.souzafood.domain.exception.NegocioException;
import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.model.Usuario;
import com.souza.souzafood.domain.repository.PedidoRepository;
import com.souza.souzafood.domain.repository.filter.PedidoFilter;
import com.souza.souzafood.domain.service.EmissaoPedidoService;
import com.souza.souzafood.infrastructure.repository.spec.PedidoSpecs;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EmissaoPedidoService emissaoPedido;

	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;

	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;

	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;

//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//		List<Pedido> pedidos = pedidoRepository.findAll();
//        List<PedidoResumoModel> pedidosModel = pedidoResumoModelAssembler.toCollectionModel(pedidos);
//		
//        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
//       
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());  
//        
//        if(StringUtils.isNotBlank(campos)) {
//        	filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.
//        			filterOutAllExcept(campos.split(",")));
//        }
//        
//        pedidosWrapper.setFilters(filterProvider);
//        
//        return pedidosWrapper;
//	}
	
	@GetMapping
	public List<PedidoResumoModel> pesquisar(PedidoFilter filtro) {
		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));

		return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
	}

	@GetMapping("/{codigoPedido}")
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);

		return pedidoModelAssembler.toModel(pedido);
	}

	@PostMapping
	public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
		try {
			Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

			// TODO pegar usuário autenticado
			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(1L);

			novoPedido = emissaoPedido.emitir(novoPedido);

			return pedidoModelAssembler.toModel(novoPedido);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
}
