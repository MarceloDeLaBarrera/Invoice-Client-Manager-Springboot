package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Path.ReturnValueNode;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

//Repository es un tipo de componente que indica que es de persistencia. 
@Repository("clienteDaoJPA")
public class ClienteDaoImplement implements IClienteDao {
	
	//EntityManager se encarga de manejar las clases de entidades, su ciclo de vida, las actualiza, persiste, es decir, todas las operaciones a la BBDD, pero a nivel de objetos. Consultas a la clase entity.
	//PersistenceContext inyecta el entity manager
	@PersistenceContext 
	private EntityManager em;
	
	//Transactional, toma contenido del metodo, y lo envuelve en una transaccion. Ya que solo lee datos, se le asigna el readonly true.
	//Si se implementa la clase Service, los transactional se trasladan a la implementacion de la clase Service.
	//@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> findAll() {
		
		return em.createQuery("FROM Cliente").getResultList();
	}

	@Override
	//@Transactional
	public void save(Cliente cliente) {
		
		//Si cliente existe:
		if(cliente.getId() !=null && cliente.getId()>0) {
			//merge lo que hace es actualizar los datos existentes
			em.merge(cliente);
		}else {
			//persist lo que hace es guardar/crear el obj cliente dentro del contexto de persistencia JPA. Sincronizando con la BBDD internamente, para poder realizar un insert.
			em.persist(cliente);
		}	
		
	}

	@Override
	//@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		// .find encuentra el objeto cliente por el id, retornando sus campos.
		return em.find(Cliente.class, id);
	}

	@Override
	//@Transactional
	public void delete(Long id) {
		Cliente cliente = this.findOne(id);
		em.remove(cliente);
		
	}

}
