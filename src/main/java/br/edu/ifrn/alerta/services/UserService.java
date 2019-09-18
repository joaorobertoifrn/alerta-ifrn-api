package br.edu.ifrn.alerta.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.edu.ifrn.alerta.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}
