package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {
	
	@Query("select p from Producto p where p.nombre like %?1%") //Nombre sea como term.
	public List<Producto> findByNombre(String term);
	
	//Otra forma de generar la consulta pero por medio del nombre del metodo. (Segun el nombre por convencionalismo viene definido en Spring, lo hace automaticamente). Es parte de la api repository de spring.
	//public List<Producto> finbyNombreLikeIgnoreCase(String term);
}
	