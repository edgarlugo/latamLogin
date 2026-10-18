package cl.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cl.example.model.LoginForm;
import cl.example.service.LoginService;
import jakarta.validation.Valid;

@Controller
public class LoginController {

	private final LoginService service;

	public LoginController(LoginService service) {
		this.service = service;
	}

	@GetMapping("/")
	public String index(@ModelAttribute("loginForm") LoginForm loginForm) {
		return "login";
	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors()) {
			model.addAttribute("result", service.login(loginForm.getLogin(), loginForm.getPassword()));
		}
		// Nunca devolver la contraseña a la vista
		loginForm.setPassword(null);
		return "login";
	}

}
