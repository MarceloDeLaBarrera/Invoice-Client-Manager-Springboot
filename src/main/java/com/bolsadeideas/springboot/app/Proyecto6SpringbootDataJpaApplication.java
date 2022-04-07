package com.bolsadeideas.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.service.JpaUserDetailsService;

@SpringBootApplication
public class Proyecto6SpringbootDataJpaApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	public static void main(String[] args) {
		SpringApplication.run(Proyecto6SpringbootDataJpaApplication.class, args);
	}
	
	@Override
	//Metodo a implementar de la interfaz CommandLineRunner
	public void run(String... args) throws Exception {
		
		
		String password= "sasasa";
		
		for(int i=0;i<2; i++) {
			//Se generan 2 encriptaciones diferentes para un mismo password.
			String bcryptPassword= passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}
		
	}	

}
