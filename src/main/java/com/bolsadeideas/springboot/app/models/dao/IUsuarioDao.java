package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	//Query method name, a traves del nombre del metodo, se ejecutara la consulta. (Select u from usuario u where u.username=?)
	public Usuario findByUsername(String username);
}
