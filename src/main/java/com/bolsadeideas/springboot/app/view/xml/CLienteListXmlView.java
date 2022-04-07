package com.bolsadeideas.springboot.app.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.xml")
public class CLienteListXmlView extends MarshallingView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.remove("titulo");
		model.remove("page");
		
		@SuppressWarnings("unchecked")
		//Obteniendo lista de clientes paginados.
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

		model.remove("clientes");
		model.put("clienteList", new ClienteList(clientes.getContent()));
		
		super.renderMergedOutputModel(model, request, response);
	}

	@Autowired //Inyectando en el metodo... Se podria haber inyectado el jaxb2marshaller fuera del metodo, y despues solo pasar como atributo el marshaller.
	public CLienteListXmlView(Jaxb2Marshaller marshaller) {
		
		super(marshaller);
		
	}
		
}
