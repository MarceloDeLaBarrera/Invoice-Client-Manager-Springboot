package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private IUsuarioDao usuariodao; //Inyeccion de dependencias de interfaz IUsuarioDao
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional(readOnly = true)
	//Cargar usuario por medio del username. UserDetails es una interfaz que representa un usuario autenticado.

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//Se obtiene usuario por su username por medio del metodo de  la interfaz del Dao.
		Usuario usuario= usuariodao.findByUsername(username);
		
		//Si usuario no existe.
		if (usuario==null) {
			logger.error("Error login: no existe el usuario ".concat(username));
			throw new UsernameNotFoundException("Username ".concat(username).concat(" no existe en el sistema"));
		}
		
		//Transformando los roles de usuarios en roles GrantedAuthorithy de spring security y asi poder pasarlos al objeto User.
		List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
		
		for (Role role: usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
			logger.info("Role ".concat(role.getAuthority()));
		}
		
		//Si usuario existe pero no tiene roles asignados.
		if (authorities.isEmpty()) {
			
			logger.error("Error login: usuario ".concat(username.concat(" no tiene roles asignados. ")));
			throw new UsernameNotFoundException("Username ".concat(username).concat(" no tiene roles en asignados."));
		}
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true , authorities);
	}

	
	
}
