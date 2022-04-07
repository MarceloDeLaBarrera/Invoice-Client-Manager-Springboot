package com.bolsadeideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImplement implements IUploadFileService {
	
	@Value("${upload.path}")
	private String rootPath;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	/*
	@Override
	public Resource load(String filename) throws MalformedURLException {
		
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);
	    Resource recurso = null;
	    
	    
	        recurso = new UrlResource(pathFoto.toUri());
	        
	        if(!recurso.exists() && !recurso.isReadable()) {
	            throw new RuntimeException("Error: no se puede cargar la imagen: "+ pathFoto.toString());
	        }
	            
		
		return recurso;
	}

	*/
	
	@Override
	public String copy(MultipartFile file) throws IOException{
		
		String uniqueFilename= UUID.randomUUID().toString().concat("_").concat(file.getOriginalFilename());
		log.info("rootPath: ", rootPath);
		
		//Obteniendo tamaño de foto, junto a ruta completa incluido el nombre de la misma foto, para escribir y almacenarla en upload.
		byte[] bytes= file.getBytes();
		Path rutaCompleta = Paths.get(this.rootPath + "//" + uniqueFilename); //o tambien --> getPath(uniqueFilename);
		Files.write(rutaCompleta, bytes);
		
		return uniqueFilename;
	}



	@Override
	public boolean delete(String filename) {
		
		//Caputurando ruta completa y accediendo al archivo para trabajar con el.	
		Path rootP = getPath(filename);
		File archivo = rootP.toFile();
		
		if (archivo.exists() && archivo.canRead()) {
			//Borrado del archivo.
			if(archivo.delete()) {
				return true;
			}
			
		}
			
		
		return false;
	}
	
	public Path getPath(String filename) {
		//Paths.get("uploads").resolve(filename).toAbsolutePath(); ---- Cuando uploads estaba dentro del proyecto como un directorio general mas.
		return Paths.get(rootPath).resolve(filename);
	}

}
