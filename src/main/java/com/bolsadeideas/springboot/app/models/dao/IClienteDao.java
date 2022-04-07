package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Cliente;


//Esta interfaz se uso y es implentada por ClienteDaoImplement la cual implementa los metodos. Si se usa la otra interfaz, no es necesario implementar metodos, puesto que hereda de CrudRepository en donde ya vienen implementados estos.
public interface IClienteDao {
	
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);
	
	public void delete(Long id);

}
