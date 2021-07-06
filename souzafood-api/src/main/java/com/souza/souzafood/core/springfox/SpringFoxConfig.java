package com.souza.souzafood.core.springfox;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.souza.souzafood.api.exceptionhandler.Problem;
import com.souza.souzafood.api.model.CozinhaModel;
import com.souza.souzafood.api.model.PedidoResumoModel;
import com.souza.souzafood.api.openapi.model.CozinhasModelOpenApi;
import com.souza.souzafood.api.openapi.model.PageableModelOpenApi;
import com.souza.souzafood.api.openapi.model.PedidosResumoModelOpenApi;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
//A interface WebMvcConfigurer é usada para customizar o Spring MVC no projeto
//Aula: https://www.algaworks.com/aulas/2119/gerando-a-documentacao-da-api-em-html-com-swagger-ui-e-springfox
public class SpringFoxConfig implements WebMvcConfigurer {

//	Aula: https://www.algaworks.com/aulas/2120/selecionando-os-endpoints-da-api-para-gerar-a-documentacao
	@Bean
	public Docket apiDocket() {
//		Class[] clazz = {Cidade.class};//Você pode fazer com que o swagger não faça o escaneamento de
//		uma classe para não aparecer na documentação da OpenApi da sua API. Você pode colocar mais de uma classe no Array
//		e em seguida acrescentar o .ignoredParameterTypes abaixo
//		fonte: https://stackoverflow.com/questions/46651381/is-there-a-way-i-can-stop-springfox-swagger-from-scanning-the-model-classes
	
		var typeResolver = new TypeResolver();  
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.souza.souzafood.api"))
				.paths(PathSelectors.any())//Não precisava colocar o any porque quando não coloca por
//				padrão ele ja seleciona todos os controladores referente ao pacote acima deixamos apenas por referencia
//				.paths(PathSelectors.ant("/restaurantes/*")) //Seleciona apenas os contraladores com o nome 'restaurantes' 'barra' 'qualquer coisa'.	 
				.paths(Predicates.not(PathSelectors.ant("/home/rodrigo/Documents/catalago/*"))) //Caso queira ocultar um controlador no mapeamento da documentação
//				OBS:Criamos o controlador fotosController e colocamos o seu requestMapping como: /home/rodrigo/Documents/catalago/{nomeArquivo}
//				esse controlador tem como função servir as fotos de acordo com o nome do arquivo
				.build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
				.globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessages())
	            .globalResponseMessage(RequestMethod.PUT, globalPostPutResponseMessages())
	            .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
//	            .globalOperationParameters(Arrays.asList(   //https://app.algaworks.com/aulas/2140/descrevendo-parametros-globais-em-operacoes
//	            		new ParameterBuilder()
//	            		    .name("campos")
//	            		    .description("Nomes das propiedades para filtrar na resposta, separados por vírgula")
//	            		    .parameterType("query")
//	            		    .modelRef(new ModelRef("string"))
//	            		    .build()))
	            .additionalModels(typeResolver.resolve(Problem.class))
	            .ignoredParameterTypes(ServletWebRequest.class,
	            		URL.class, URI.class, URLStreamHandler.class, Resource.class, 
	            		File.class, InputStream.class, InputStreamResource.class) //https://app.algaworks.com/aulas/2138/ignorando-tipos-de-parametros-de-operacoes-na-documentacao
	            .directModelSubstitute(Pageable.class, PageableModelOpenApi.class
	            		)
	            .alternateTypeRules(AlternateTypeRules.newRule(
	            		typeResolver.resolve(Page.class, CozinhaModel.class), 
	            		CozinhasModelOpenApi.class)) //https://app.algaworks.com/aulas/2136/corrigindo-documentacao-com-substituicao-de-page
	            .alternateTypeRules(AlternateTypeRules.newRule( //https://app.algaworks.com/aulas/2142/desafio-descrevendo-documentacao-de-endpoints-de-pedidos
	                    typeResolver.resolve(Page.class, PedidoResumoModel.class),
	                    PedidosResumoModelOpenApi.class))
	            .apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"),
				        new Tag("Grupos", "Gerencia os grupos de usuários"),
				        new Tag("Cozinhas", "Gerencia as cozinhas"),
				        new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
				        new Tag("Pedidos", "Gerencia os pedidos"),
				        new Tag("Restaurantes", "Gerencia os restaurantes"),
				        new Tag("Estados", "Gerencia os estados"),
				        new Tag("Produtos", "Gerencia os produtos de restaurantes"),
				        new Tag("Usuários", "Gerencia os usuários"),
				        new Tag("Estatísticas", "Estatísticas da AlgaFood"));
 
//				.ignoredParameterTypes(clazz);	         
	}
	
//	Aula: https://app.algaworks.com/aulas/2127/descrevendo-codigos-de-status-de-respostas-de-forma-global

	private List<ResponseMessage> globalPostPutResponseMessages() {
	    return Arrays.asList(
	            new ResponseMessageBuilder()
	                .code(HttpStatus.BAD_REQUEST.value())
	                .message("Requisição inválida (erro do cliente)")
	                .responseModel(new ModelRef("Problema"))
	                .responseModel(new ModelRef("Problema"))
	                .build(),
	            new ResponseMessageBuilder()
	                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
	                .message("Erro interno no servidor")
	                .responseModel(new ModelRef("Problema"))
	                .build(),
	            new ResponseMessageBuilder()
	                .code(HttpStatus.NOT_ACCEPTABLE.value())
	                .message("Recurso não possui representação que poderia ser aceita pelo consumidor")
	                .build(),
	            new ResponseMessageBuilder()
	                .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
	                .message("Requisição recusada porque o corpo está em um formato não suportado")
	                .responseModel(new ModelRef("Problema"))
	                .build()
	        );
	}
	
	private List<ResponseMessage> globalDeleteResponseMessages() {
	    return Arrays.asList(
	            new ResponseMessageBuilder()
	                .code(HttpStatus.BAD_REQUEST.value())
	                .message("Requisição inválida (erro do cliente)")
	                .responseModel(new ModelRef("Problema"))
	                .build(),
	            new ResponseMessageBuilder()
	                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
	                .message("Erro interno no servidor")
	                .responseModel(new ModelRef("Problema"))
	                .build()
	        );
	}
	
	private List<ResponseMessage> globalGetResponseMessages() {
	
		return Arrays.asList(
					
             new ResponseMessageBuilder()
                 .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                 .message("Erro interno do servidor")
                 .responseModel(new ModelRef("Problema"))
                 .build(),
             new ResponseMessageBuilder()
                 .code(HttpStatus.NOT_ACCEPTABLE.value())
                 .message("Recurso não possui representação que poderia ser aceita pelo consumidor")
                 .build()
			);
	}
	

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("SouzaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("1")
				.contact(new Contact("SouzaFood", "https://www.souzafood.com", "contato@souzafood.com"))
				.build();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html")
		.addResourceLocations("classpath:/META-INF/resources/");
		
		registry.addResourceHandler("/webjars/**")
		.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
