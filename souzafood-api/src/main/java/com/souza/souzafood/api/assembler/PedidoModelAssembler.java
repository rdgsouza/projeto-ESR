package com.souza.souzafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.souza.souzafood.api.controller.CidadeController;
import com.souza.souzafood.api.controller.FormaPagamentoController;
import com.souza.souzafood.api.controller.PedidoController;
import com.souza.souzafood.api.controller.RestauranteController;
import com.souza.souzafood.api.controller.RestauranteProdutoController;
import com.souza.souzafood.api.controller.UsuarioController;
import com.souza.souzafood.api.model.PedidoModel;
import com.souza.souzafood.domain.model.Pedido;

@Component
public class PedidoModelAssembler 
        extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }
    
    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        TemplateVariables pageVariables = new TemplateVariables(
        		new TemplateVariable("page", VariableType.REQUEST_PARAM),
        		new TemplateVariable("size", VariableType.REQUEST_PARAM),
        		new TemplateVariable("sort", VariableType.REQUEST_PARAM));
        
        
        TemplateVariables filtroVariables = new TemplateVariables(
        		new TemplateVariable("clienteId", VariableType.REQUEST_PARAM),
        		new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
        		new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
        		new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM));
           
        String pedidosUrl = linkTo(PedidoController.class).toUri().toString();
        
//  https://app.algaworks.com/forum/topicos/84494/link-uritemplate-template-string-rel-depreciado
//        pedidoModel.add(Link.of(UriTemplate.of(pedidosUrl, pageVariables), "pedidos"));
        pedidoModel.add(Link.of(UriTemplate.of(pedidosUrl, 
        		pageVariables.concat(filtroVariables)), "pedidos"));
        
        pedidoModel.getRestaurante().add(linkTo(methodOn(RestauranteController.class)
                .buscar(pedido.getRestaurante().getId())).withSelfRel());
        
        pedidoModel.getCliente().add(linkTo(methodOn(UsuarioController.class)
                .buscar(pedido.getCliente().getId())).withSelfRel());
        
        // Passamos null no segundo argumento, porque é indiferente para a
        // construção da URL do recurso de forma de pagamento
        pedidoModel.getFormaPagamento().add(linkTo(methodOn(FormaPagamentoController.class)
                .buscar(pedido.getFormaPagamento().getId(), null)).withSelfRel());
        
        pedidoModel.getEnderecoEntrega().getCidade().add(linkTo(methodOn(CidadeController.class)
                .buscar(pedido.getEnderecoEntrega().getCidade().getId())).withSelfRel());
        
        pedidoModel.getItens().forEach(item -> {
            item.add(linkTo(methodOn(RestauranteProdutoController.class)
                    .buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
                    .withRel("produto"));
        });
        
        return pedidoModel;
    }
}