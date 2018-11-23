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
		//从request请求中获取keycloak验证信息主体
		KeycloakSecurityContext keycloakSecurityContext = getKeycloakSecurityContext(request);
		//从验证信息中获取id_token，并从token中获取用户名。token中也包含其他用户信息
		String username = keycloakSecurityContext.getIdToken().getPreferredUsername();
		//从验证信息中获取id_token，并从token中获取realm访问权限的角色信息，判断用户是否拥有user角色
		if(keycloakSecurityContext.getToken().getRealmAccess().getRoles().contains("user")){
			return "hello user " + username;
		}
		return "permission denied";
	}

	@RequestMapping(value = "level1")
	@ResponseBody
	public String admin(HttpServletRequest request) {
		//从request请求中获取keycloak验证信息主体
		KeycloakSecurityContext keycloakSecurityContext = getKeycloakSecurityContext(request);
		//从验证信息中获取id_token，并从token中获取用户名。token中也包含其他用户信息
		String username = keycloakSecurityContext.getIdToken().getPreferredUsername();
		//从验证信息中获取id_token，并从token中获取realm访问权限的角色信息，判断用户是否拥有admin角色
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
