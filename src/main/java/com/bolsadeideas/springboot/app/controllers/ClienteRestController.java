package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;




//Cuando son solo metodos rest.
@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
	
	//Inyeccion de dependencia, inyectando interfaz.
	@Autowired
	//@Qualifier("clienteDaoJPA")
	//private IClienteDao clienteDao;
	private IClienteService clienteService;
	
	
	@GetMapping(value="/listar")
	public List<Cliente> listarRest(){
		//retorno todos los clientes en formato Json.
		return clienteService.findAll();
	}

}
