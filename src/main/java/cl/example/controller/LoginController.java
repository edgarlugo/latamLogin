package cl.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import cl.example.model.Response;
import cl.example.model.User;
import cl.example.service.LoginService;

@Controller
public class LoginController {
	@Autowired
	private LoginService service;

	@PostMapping("/login")
	public Response Login(@ModelAttribute User user) {
		System.out.println("login:" + user.getLogin());
		System.out.println("password:" + user.getPassword());
		Response response = service.Login(user.getLogin(), user.getPassword());
		System.out.println("Respuesta de la Api de Login es: "+response);
		return response;
	}

	@GetMapping(value = "/")
	public String index(Model model) {
		return "login";
	}

	@ModelAttribute(value = "user")
	public User user() {
		return new User();
	}

}
