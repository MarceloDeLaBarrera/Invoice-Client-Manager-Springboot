package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Factura;

//Implementando crudrepository, que trae metodos crud prestablecidos.
//Crud Repository lleva la clase/objeto relacionado y el tipo de dato de la llave.
public interface IFacturaDao extends CrudRepository<Factura, Long> {

		@Query("SELECT f FROM Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto WHERE f.id=?1")
		public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
