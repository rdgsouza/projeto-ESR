package com.souza.souzafood.api.controller.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.SmartValidator;

@Configuration
public class ControllerValidateUtilsConfiguration {

	 public ControllerValidateUtilsConfiguration(SmartValidator validator) {
	        ControllerValidateUtil.setValidator(validator);
	    }
}
