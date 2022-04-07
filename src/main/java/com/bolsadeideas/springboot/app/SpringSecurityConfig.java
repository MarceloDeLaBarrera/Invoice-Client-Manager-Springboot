package com.bolsadeideas.springboot.app;


import javax.management.loading.PrivateClassLoader;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

//EnableGlobalMethodSecurity es una anotacion necesaria para poder hacer uso de la anotacion Secured y derivados en el controlador. PrePostEnabled permite usar la anotacion PreAuthorize en controlador.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
		
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
		
		//Forma 1: Conexion a BBDD con JDBC
		
		/*
		//Se hace la configuracion pasando el datasource, y el passw
		builder.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("SELECT username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a INNER JOIN users u ON (a.user_id= u.id) WHERE u.username=?");
		*/
		
		
		//Forma 2: Conexion a BBDD con mapeo de objetos, JPA.
		
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		
		//Forma 3: En memoria.
		
		/*
		//Con cada usuario que se genere, se produce un evento que va a encriptar la contraseña.
		UserBuilder users = User.builder().passwordEncoder(password -> {
			return encoder.encode(password); //Tambien dentro de passwordEncoder puede ir: encoder::encode ... Es lo mismo pero de otra forma.
		});
		
		builder.inMemoryAuthentication()
		.withUser(users.username("admin").password("sasasa").roles("ADMIN","USER"))
		.withUser(users.username("marcelo").password("sasasa").roles("USER"));
	
		*/
	}
	
	//Inyeccion de dependencias de loginsucceshandler del package app.auth.handler
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**","/listar").permitAll()
		.antMatchers("/ver/**").hasAnyRole("USER") //antMatchers da seguridad a las rutas segun rol.
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		//Para añadir login y logout a paginas que requieren autenficacion. Spring ya cuenta con formulario de login por defecto. Si se especifica ruta, significa que esta personalizado.
		.formLogin().successHandler(loginSuccessHandler).loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}
	*/
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**","/listar", "/listar-rest", "/api/clientes/listar/", "/locale").permitAll()
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.anyRequest().authenticated()
		.and()
		//Para añadir login y logout a paginas que requieren autenficacion. Spring ya cuenta con formulario de login por defecto. Si se especifica ruta, significa que esta personalizado.
		.formLogin().successHandler(loginSuccessHandler).loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}
}