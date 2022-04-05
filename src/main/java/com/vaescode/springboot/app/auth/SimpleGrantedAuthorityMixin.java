package com.vaescode.springboot.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityMixin {

	
	@JsonCreator// authority parametro del token al mostrar el arreglo de los roles 
	public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {
		
	}

	
}
