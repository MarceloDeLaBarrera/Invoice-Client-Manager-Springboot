package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import javax.validation.Path.ReturnValueNode;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	//Principal contiene al usuario logeado, y permite validar si el usuario inicio o no sesion.
	public String login(@RequestParam(value = "error", required = false) String error,  
						@RequestParam(value = "logout", required = false) String logout,	Model model, Principal principal, RedirectAttributes flash) {
		
		if (principal != null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesión previamente.");
			return "redirect:/";
		}
		
		if (error != null) {
			model.addAttribute("error", "Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo.");
		}
		
		if (logout != null) {
			model.addAttribute("success", "Ha cerrado sesión.");
		}
		
		return "accounts/login";
	}
}
