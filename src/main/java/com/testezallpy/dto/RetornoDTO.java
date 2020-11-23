package com.testezallpy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class RetornoDTO {
    
	String status;
	String diretorioEntrada;
	String arquivosImportados;
	String diretorioSaida;
		
}
