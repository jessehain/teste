package com.testezallpy.service;

import com.testezallpy.dto.RetornoDTO;
import com.testezallpy.dto.resultadoDTO;

import lombok.AllArgsConstructor;

import com.testezallpy.constants.Constantes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImportarServiceImpl implements ImportarService {
	
	public static String path;
	
	@Override
	public RetornoDTO processarArquivo() {
		
		BufferedReader buffer;
	    int qtdVend = 0;
		int qtdCli  = 0;
        int i, j;
        double maiorvenda = 0;
        double valorvenda = 0;
        double menorvenda = 0;
        double valortotalvenda = 0;
        String nomepiorvendedor = "";
        String idmaiorvenda = "";
        String aux;        
        String arquivo;
        String pathFile;
        String listaArquivos = "";        
        String linha = "";
        String[] registro;
        String[] vendas;
        String[] dadosvend;
        String[] files;
	    RetornoDTO retorno = new RetornoDTO();
	    resultadoDTO resultado = new resultadoDTO();
        File diretorio = new File(".");
        
	    try {
	    	
           path     = diretorio.getCanonicalPath();		
           pathFile = path + Constantes.IN_DIRECTORY;
        		
           diretorio = new File( pathFile );
           files     = diretorio.list();
        
           for( i = 0; i < files.length; i++  ) {
        	 
              arquivo = files[i];
           
              if( ! arquivo.toUpperCase().contains( Constantes.EXTENSAO_ARQUIVO )) {
        	      continue;
              }        	   
           
              listaArquivos += arquivo + "|"; 
              buffer = new BufferedReader( new FileReader( pathFile + arquivo ) ); 
        	
		      while (true) {
			 
			     linha = buffer.readLine();
						
			     if (linha == null) { 
			        break;
		         }
			  
			     registro = linha.split( Constantes.SPLIT_REGISTRO );
			
			     if (registro[ Constantes.POS_ID_LINE ].contains( Constantes.ID_REG_VENDEDORES )) {
				     qtdVend++;
				     continue;
			     }
			
			     if (registro[ Constantes.POS_ID_LINE ].contains( Constantes.ID_REG_CLIENTES )) {
			         qtdCli++;
			         continue;
			     }
			    
			     if (registro[ Constantes.POS_ID_LINE ].contains( Constantes.ID_REG_VENDAS )) {
				 
				    aux    = registro[2].replace(Constantes.DEMARCADOR_VENDAS_INI, "").replace(Constantes.DEMARCADOR_VENDAS_FIM, ""); 				 				 				
				    vendas = aux.split( Constantes.SPLIT_VENDAS );
				 
				    valortotalvenda = 0;
				 
				    for( j = 0; j < vendas.length; j++ ) {
					
					   dadosvend = vendas[j].split( Constantes.SPLIT_DADOS_VENDAS ); 
					
					   valorvenda = Double.parseDouble(dadosvend[ Constantes.POS_REG_DVENDAS_QUANT]) * Double.parseDouble(dadosvend[Constantes.POS_REG_DVENDAS_VALOR]);
					   valortotalvenda += valorvenda;
					
					   if( valorvenda > maiorvenda ) {
					      maiorvenda   = valorvenda;
					      idmaiorvenda = dadosvend[Constantes.POS_REG_DVENDAS_ID];     
					   }				
					 
				    }
				 
				    if( valortotalvenda < menorvenda || menorvenda == 0 ) {
					   nomepiorvendedor = registro[Constantes.POS_REG_NOME_VENDEDOR];
					   menorvenda = valortotalvenda;
				    }
				 
				    resultado.setIdMaiorVenda(idmaiorvenda);
				    resultado.setPiorVendedor(nomepiorvendedor);
				    resultado.setQtdClientes(qtdCli);
				    resultado.setQtdVend(qtdVend);
			  
			     }
			  
		      }
					
		      buffer.close();
		      
		      if( ! gravarArquivo( arquivo, resultado ) ) {
		    	 throw new Exception("Erro na gravacao do arquivo"); 
		      }
		   
		      retorno.setDiretorioEntrada( path + Constantes.IN_DIRECTORY );
		      retorno.setDiretorioSaida( path + Constantes.OUT_DIRECTORY );
		   		   
           }
           
           retorno.setArquivosImportados(listaArquivos);   
           retorno.setStatus("OK - Arquivo processado com sucesso");
           
	    }catch(Exception e) {
	       retorno.setStatus("NOK - Erro ao processar arquivo");
	    }
	    
        return retorno;		
	}
	
	public static boolean gravarArquivo( String arquivo, resultadoDTO resultado ) throws IOException {
	  
		BufferedWriter buffWrite;
		String linha    = "";
		String nameFile = "";
		String pathFile = "";
        boolean retorno = true;
		
		try {
		
		   nameFile = arquivo.toUpperCase().replace( Constantes.EXTENSAO_ARQUIVO, ".done.dat");
	       pathFile = path + Constantes.OUT_DIRECTORY;
	    		
		   buffWrite = new BufferedWriter(new FileWriter( pathFile + nameFile ) );
		
		   linha = "Quantidade de clientes no arquivo de entrada:" + Integer.toString( resultado.getQtdClientes() );
		   buffWrite.append(linha + "\n");
		
		   linha = "Quantidade de vendedor no arquivo de entrada:" + Integer.toString( resultado.getQtdVend() );
		   buffWrite.append(linha + "\n");
		
		   linha = "ID da venda mais cara:" + resultado.getIdMaiorVenda();
		   buffWrite.append(linha + "\n");
		
		   linha = "Pior Vendedor:" + resultado.getPiorVendedor();
		   buffWrite.append(linha + "\n");
		
		   buffWrite.close();
		
		}catch( Exception e ){
		   	retorno = false;
		}
		
	  return retorno;	
	}
	
}
