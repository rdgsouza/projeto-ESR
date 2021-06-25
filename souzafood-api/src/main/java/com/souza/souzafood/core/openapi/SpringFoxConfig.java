package com.souza.souzafood.core.openapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.classmate.TypeResolver;
import com.souza.souzafood.api.exceptionhandler.Problem;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
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
				.build()
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, globalGetResponseMessages())
				.globalResponseMessage(RequestMethod.POST, globalPostPutResponseMessages())
	            .globalResponseMessage(RequestMethod.PUT, globalPostPutResponseMessages())
	            .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
	            .additionalModels(typeResolver.resolve(Problem.class))
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"),
						new Tag("Grupos", "Gerencia os grupos de usuários"));
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
