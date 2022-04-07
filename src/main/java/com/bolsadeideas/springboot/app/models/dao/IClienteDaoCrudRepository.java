package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

//Esta es opcional, implementando crudrepository, que trae metodos crud prestablecidos.
//Crud Repository lleva la clase/objeto relacionado y el tipo de dato de la llave.
//PagingAndSortingRepository hereda de CrudRepository por lo cual posee todos sus metodos, y metodos propios para paginación.
public interface IClienteDaoCrudRepository extends PagingAndSortingRepository<Cliente, Long> {
	
	@Query("SELECT c FROM Cliente c left join fetch c.facturas f WHERE c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);

}
