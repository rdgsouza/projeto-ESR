package com.souza.souzafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.souza.souzafood.api.model.EnderecoModel;
import com.souza.souzafood.domain.model.Endereco;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		
		// Para forçar o map a fazer um mapping em uma propriedade com nome diferente
		//como vimos sobre na aula dobre correspondência as propiedades tem que haver uma correspondencia
		//mas para ser "copiado" de uma propiedade para outra os valores. Fazemos essa alteração do nome da
		//propiedade porque queriamos que aparecesse no payload da resposta o nome PrecoFrete em vez de taxaFrete
//		modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
//			.addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
		
	var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
		
		   enderecoToEnderecoModelTypeMap.<String>addMapping(
				  src -> src.getCidade().getEstado().getNome(),
				  (enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));
				
		return modelMapper;
	}

}