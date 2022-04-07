package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

//Se usa Entity para indicar que clase POJO es una entidad de JPA e Hibernate, y que representará una tabla en la BBDD
//En este momento se esta usando H2 que seria una BBDD que por configuracion de Spring,  se aloja en memoria, es decir, solamente virtual para testing, no produccion.
@Entity
//Para que la tabla tenga distinto nombre de la clase entity.
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id // Indica que atributo es llave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@Column(name = "nombre_cliente")
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email
	private String email;
	
	//@NotNull va comentado porque la fecha se genera de forma automatica, y si estuviera la anotacion, no permitiria completar el formulario.
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE) // Indica formato en que se guardara fecha en la BBDD
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createAt;
	
	private String foto;
	
	//1 cliente -> muchas facturas. 1 factura -> 1 cliente.
	//cascadetype.all -> elimina elementos hijos en cascada. Si se borra un cliente, se borran todas sus facturas.
	//mappedBy establece que la relacion bidireccional y permite que se cree la llave foranea en Factura "cliente_id" al no especificar el JoinColumn.
	//orphan removal sirve para eliminar registros huerfanos, que no esten asociados a ningun cliente, es decir, eliminar facturas sin asociacion a un cliente.
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//@JsonIgnore //Para que Json ignore y no se genere doble loop como con XML.
	@JsonManagedReference
	private List<Factura> facturas;
	
	
	//Prepersist hace que se llame el metodo antes de que se realice el insertado en la BBDD.
	@PrePersist
	public void prePersist() {
		//Creacion de nueva fecha 
		this.createAt= new Date();
	}
	

	
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	
	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	public void addFactura(Factura factura) {
		facturas.add(factura);
	}
	
	
	

	@Override
	//Similar a python con funcion __str__ para mostrar un objeto como string.
	public String toString() {
		return nombre + " " + apellido;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// Se usa serializacion en caso de que se tenga que convertir un objeto en
	// secuencia de bits para almacenarlo o transmitirlo en BBDD.
	private static final long serialVersionUID = 1L;

}
