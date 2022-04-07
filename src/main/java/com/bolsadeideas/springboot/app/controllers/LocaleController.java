package com.bolsadeideas.springboot.app.controllers;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {
	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		
		//referer entrega la referencia de la ultima url.
		String ultimaUrl= request.getHeader("referer");
		
		return "redirect:".concat(ultimaUrl);
	}
}
