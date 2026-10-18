package cl.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

	@NotBlank(message = "Ingresa tu usuario")
	@Size(max = 100)
	private String login;

	@NotBlank(message = "Ingresa tu contraseña")
	@Size(max = 200)
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
