package com.bolsadeideas.springboot.app;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Value("${upload.path}")
	private String rootPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		//Resourcehandler seria lo mismo que tengamos como ruta en ver.html ya que se añade como directorio para recursos estaticos. Y la locacion, la ruta real fisica dentro del pc.
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:/".concat(rootPath));
	}
	
	
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error/error_403");
	}
	
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		//Registro del password encoder como componente.
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		//Se asigna localizacion por default y se determina que se almacena en la sesion, con "Session"LocaleResolver.
		localeResolver.setDefaultLocale(new Locale("es","ES"));
		
		return localeResolver;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor () {
		
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		//Lang hace referencia al lenguaje, e intercepta las rutas modificando el lenguaje, cada vez que se pase el parametro lang
		localeChangeInterceptor.setParamName("lang");
		
		return localeChangeInterceptor;
	}

	//Registro del interceptor.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	//Conversor que se encarga de sarializar el objeto a XML.
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {com.bolsadeideas.springboot.app.view.xml.ClienteList.class});
		return marshaller;
	}

}
