package com.testezallpy.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testezallpy.dto.RetornoDTO;
import com.testezallpy.service.ImportarService;

@RestController
@RequestMapping("/importar/")
public class ImportarController {

	private final ImportarService importarService;

    public ImportarController(ImportarService importarService) {
        this.importarService = importarService;
    }

    @GetMapping(value = "/arquivo" )
	public ResponseEntity<RetornoDTO> ImportarArquivo() throws IOException {
		
		RetornoDTO retorno = new RetornoDTO();		
		retorno = importarService.processarArquivo();
		
        return ResponseEntity.ok(retorno);
    }
	
}