package com.testezallpy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class resultadoDTO {
    private int qtdClientes;
    private int qtdVend;
    private String idMaiorVenda;
    private String piorVendedor;
}