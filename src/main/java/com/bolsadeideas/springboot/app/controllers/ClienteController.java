package com.bolsadeideas.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.management.loading.PrivateClassLoader;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import net.bytebuddy.matcher.LatentMatcher.ForFieldToken;



@Controller
//Se guardará en atributos de la session,el objeto cliente, dando persistencia a los datos 
@SessionAttributes("cliente")
public class ClienteController {
	
	//Inyeccion de dependencia, inyectando interfaz.
	@Autowired
	//@Qualifier("clienteDaoJPA")
	//private IClienteDao clienteDao;
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService iUploadFileService;
	
	@Value("${upload.path}")
	private String rootPath;
	
	@Autowired
	private MessageSource messageSource;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value= {"/listar", "/"}, method= RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
						HttpServletRequest request,
						Locale locale) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		
		//Accediendo al usuario desde el controller.
		if (auth != null) {
			logger.info("Hola usuario autentificado, tu username es ".concat(auth.getName()));
		}
		
		//Forma 1
		if (hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso."));
		}else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso."));
		}
		
		//Forma 2 (La mejor)
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("Forma usando Security Context: Hola ".concat(auth.getName()).concat(" tienes acceso."));
		}else {
			logger.info("Forma usando Security Context: Hola ".concat(auth.getName()).concat(" NO tienes acceso."));
		}
		
		
		
		
		
		
		
		//5 registros por pagina.
		Pageable pageRequest= PageRequest.of(page, 5);
		Page<Cliente> clientesPaginado= clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender= new PageRender<>("/listar", clientesPaginado);
		
		
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		//model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("clientes", clientesPaginado);
		model.addAttribute("page",pageRender);
		
		return "listar";
	}
	
	//Listar version REST.
	//Nota ResponseBody responde en formato Json, se utiliza cuando no todos los metodos son REST. Si todos son REST, se utiliza un controlador de tipo @RestController.
	//Nota 2, si se quiere que sea XML mas Json, se debe añadir el wraper de ClienteList, remplazando List<Cliente> por ClienteList, y retornando un new ClienteList(clienteservice......)
	@GetMapping(value="/listar-rest")
	public @ResponseBody List<Cliente> listarRest(){
		//retorno todos los clientes en formato Json.
		return clienteService.findAll();
	}
	
	//Secured sirve para dar seguridad al controlador mediante anotaciones en vez de hacerlo por la clase SpringSecurity por medio de la rutas.
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		/*
		 * Se puede usar tanto Map (modelmap) asi como Model model. Ya que Model es una interfaz, y ModelMap implementa la interfaz Model, por lo que implementa sus metodos.
		 */
		
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		model.put("botonEnviar", "Crear Cliente");
		
		return "form"; 
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		/*Se recibe objeto cliente que viene con los datos poblados del formulario.
		Posteriormente se guarda con el metodo save del DAO.*/
		
		/* Con @valid se habilita la validacion, y con Binding result, se verifica si hay errores.
		 */
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			if(cliente.getId()!=null) {
				model.addAttribute("botonEnviar", "Editar Cliente");
				
			}else {
				model.addAttribute("botonEnviar", "Crear Cliente");
			}
			
			return "form";
		}
		
		if(!foto.isEmpty()) {
			//Obteniendo la ruta donde se guardara la imagen, uploads. En string rootPath almacenamos la ruta absoluta. Es decir, desde c://.
			//Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
			//String rootPath= directorioRecursos.toFile().getAbsolutePath();
			
			//Si existe cliente y si existe foto, se elimina foto, ya que se estaria editando si se cumplen ambas condiciones.
			if(cliente.getId() !=null && cliente.getId() >0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				
				iUploadFileService.delete(cliente.getFoto());
			}
			String uniqueFilename = null;
			try {
				uniqueFilename = iUploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Ha subido correctamente '".concat(uniqueFilename).concat("'"));
			cliente.setFoto(uniqueFilename);
		
		}
		
		//Es cliente.getid distinto de null? Almacena primer texto en mensajeFlash si es true, si no, almacena el segundo.
		String mensajeFlash= (cliente.getId()!=null)? "Cliente editado con exito" : "Cliente creado con exito";
		
		//Guardando cliente en BBDD. Despues de guardarlo, se elimina el objeto de la sesion.
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		 
		 return "redirect:listar"; 
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id>0) {
			cliente= clienteService.findOne(id);
			if(cliente==null) {
				flash.addFlashAttribute("error", "El cliente no existe en BBDD");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		model.put("botonEnviar", "Editar Cliente");
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){
		if(id > 0) {
			Cliente cliente = clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito");
			
			//Si existe foto
			if (cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				
				//Borra archivo, y si archivo es borrado lanza mensaje.
				if(iUploadFileService.delete(cliente.getFoto())) {
					flash.addFlashAttribute("info", "Foto ".concat(cliente.getFoto().concat(" eliminada con exito.")));
				}
			}	
		}
		return "redirect:/listar";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash ) {
		
		//Cliente cliente= clienteService.findOne(id);
		Cliente cliente= clienteService.fetchByIdWithFacturas(id);
		
		if (cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente".concat(cliente.getNombre()));
		
		return "ver";
	}
	
	private boolean hasRole(String role) {
		
		// SecurityContext contiene el contexto completo de autenticacion de spring
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context == null) {
			return false;
		}
		
		//Verifica que usuario este autentificado, si no lo esta, el objeto es nulo pues no contiene nada y por ende retorna false, es decir, no tiene un rol.
		Authentication auth= context.getAuthentication();
		if (auth == null) {
			return false;
		}
		//Cualquier clase que representa un Role o auth tiene implementada la interfaz GrantedAuthority, dado que esta interfaz es la que declara el metodo getAuthority.
		// Con getaAuthorities se obtiene una lista con la coleccion de roles, y se almacena en la variable authorities que definimos.
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		//Se recorre con un for las authorities, y se utiliza el metodo getAuthority para obtener el string del rol.
		//Si el role del string es igual al rol que retorna el metodo getAuthority, es decir, si el usuario tiene rol, entonces tiene permiso y retorna true.
		for(GrantedAuthority authority: authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info("Hola usuario ".concat(auth.getName()).concat(" tu rol es: ").concat(authority.getAuthority()));
				return true;
			}
		}
		//Si no tiene rol, retorna falso.
		return false;
	}
	
	/* Otra forma por http response.
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		Resource recurso = null;
		
		try{
			recurso=  uploadFileService.load(filename);
	     }catch(MalFormedURLException e){
	     	e.printStackTrace();
	     }
		
	     return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() +"\"").body(recurso);
	
	}
	*/

}	
