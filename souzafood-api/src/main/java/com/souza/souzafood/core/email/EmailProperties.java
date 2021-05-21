package com.souza.souzafood.core.email;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Valid
@Getter
@Setter
@Component
@ConfigurationProperties("souzafood.email")
public class EmailProperties {

	@NotNull
	private String remetente;
}
