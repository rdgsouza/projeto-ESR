package com.souza.souzafood.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.souza.souzafood.domain.model.Pedido;
import com.souza.souzafood.domain.service.EnvioEmailService.Mensagem;

@Service
public class FluxoPedidoService {
 
	@Autowired
	private EmissaoPedidoService emissaoPedido;
 
	@Autowired    
	private EnvioEmailService envioEmail; 
	 
	@Transactional
	public void confirmar(String codigoPedido) {
		
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
		  
//      Caso precise que a propiedade souzafood.email.remetente= do application.properties fique dinamica
//	    Implementação feita no forum e suporte https://www.algaworks.com/forum/topicos/84402/duvida-na-propiedade-remetente#90859	
//		String remetente = String.format("%s %s", "SouzaFood", "<souzaafood@gmail.com>");
//		emailProperties.setRemetente(remetente);
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo("pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		envioEmail.enviar(mensagem);
	}

	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		pedido.cancelar();
	}

	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.entregar();
	}
	
}
