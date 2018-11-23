package com.example.demo;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@SpringBootApplication
@Configuration
public class DemoApplication {

	@RequestMapping(value = "level2")
	@ResponseBody
	public String user(HttpServletRequest request) {
		KeycloakSecurityContext keycloakSecurityContext = getKeycloakSecurityContext(request);
		String username = keycloakSecurityContext.getIdToken().getPreferredUsername();
		if(keycloakSecurityContext.getToken().getRealmAccess().getRoles().contains("user")){
			return "hello user " + username;
		}
		return "permission denied";
	}

	@RequestMapping(value = "level1")
	@ResponseBody
	public String admin(HttpServletRequest request) {
		KeycloakSecurityContext keycloakSecurityContext = getKeycloakSecurityContext(request);
		String username = keycloakSecurityContext.getIdToken().getPreferredUsername();
		if(keycloakSecurityContext.getToken().getRealmAccess().getRoles().contains("admin")){
			return "hello admin " + username;
		}
		return "permission denied";
	}

    @RequestMapping(value = "logout")
    @ResponseBody
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "logout";
    }

	private KeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request) {
		return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
