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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name= "facturas")
public class Factura implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String descripcion;
	private String observacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;
	
	//1 cliente -> muchas facturas. 1 factura -> 1 cliente.
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	
	//1 factura y muchos itemfactura.
	//orphan remove para eliminar lineaitems que no esten asociados a ninguna factura.
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//JoinColumn permite crear la llave foranea factura_id en la tabla facturaItems. Se usa cuando la relacion es unidireccional. Sirve solo para indicar el nombre que tendrá, ya que por defecto, igual se creará.
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;
	
	
	//METODOS
	
	//Metodo constructor
	public Factura() {
		
		//Inicializando itemsFactura
		this.items= new ArrayList<ItemFactura>();
	}

	//Prepersist hace que se llame el metodo antes de que se realice el insertado en la BBDD.
	@PrePersist
	public void prePersist() {
		//Creacion de nueva fecha 
		this.createAt= new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public String getObservacion() {
		return observacion;
	}



	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}



	public Date getCreateAt() {
		return createAt;
	}



	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}


	@XmlTransient // Para corregir error del xml y solo pueda acceder a Cliente 
	@JsonBackReference
	public Cliente getCliente() {
		return cliente;
	}



	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	public void addItemFactura(ItemFactura item) {
		this.items.add(item);
	}
	
	public Double getTotal() {
		Double total= 0.0;
		
		//Cantidad de items factura relacionados a una factura, es decir, el tamaño de la lista.
		int size= items.size();
		
		for(int i=0; i< size; i++) {
			//Con get accedemos al elemento/objeto 0,1,2,3 de la lista, y llamamos a su metodo calcular importe.
			total+= items.get(i).calcularImporte();
			}
		
		return total;
	}



	private static final long serialVersionUID = 1L;
}
