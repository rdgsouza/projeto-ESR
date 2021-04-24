package com.souza.souzafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.souza.souzafood.api.assembler.PedidoModelAssembler;
import com.souza.souzafood.api.assembler.PedidoResumoModelAssembler;
import com.souza.souzafood.api.model.PedidoModel;
import com.souza.souzafood.api.model.PedidoResumoModel;
import com.souza.souzafood.api.model.input.PedidoInput;
import com.souza.souzafood.api.model.input.PedidoInputDisassembler;
import com.souza.souzafood.core.data.PageableTranslator;
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
	public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, 
	        @PageableDefault(size = 10) Pageable pageable) {
		 pageable = traduzirPageable(pageable);
		
	    Page<Pedido> pedidosPage = pedidoRepository.findAll(
	            PedidoSpecs.usandoFiltro(filtro), pageable);
	    
	    List<PedidoResumoModel> pedidosResumoModel = pedidoResumoModelAssembler
	            .toCollectionModel(pedidosPage.getContent());
	    
	    Page<PedidoResumoModel> pedidosResumoModelPage = new PageImpl<>(
	            pedidosResumoModel, pageable, pedidosPage.getTotalElements());
	    
	    return pedidosResumoModelPage;
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
	
// Aula: https://www.algaworks.com/aulas/2043/implementando-um-conversor-de-propriedades-de-ordenacao
	private Pageable traduzirPageable(Pageable apiPageable) {
	
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"subtotal","subtotal",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
				);
		
		return PageableTranslator.translate(apiPageable, mapeamento);
	}
}
