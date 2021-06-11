package com.souza.souzafood.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//A interface WebMvcConfigurer é usada para customizar o Spring MVC no projeto
//Aula: https://www.algaworks.com/aulas/2119/gerando-a-documentacao-da-api-em-html-com-swagger-ui-e-springfox
public class SpringFoxConfig implements WebMvcConfigurer {

//	Aula: https://www.algaworks.com/aulas/2120/selecionando-os-endpoints-da-api-para-gerar-a-documentacao
	@Bean
	public Docket apiDocket() {

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.souza.souzafood.api"))
				.paths(PathSelectors.any())//Não precisava colocar o any porque quando não coloca por
//				padrão ele ja seleciona todos os controladores referente ao pacote acima deixamos apenas por referencia
//				.paths(PathSelectors.ant("/restaurantes/*")) //Seleciona apenas os contraladores com o nome 'restaurantes' 'barra' 'qualquer coisa'.	 
				.build()
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"));
		         
	}

	public ApiInfo apiInfo() {
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
