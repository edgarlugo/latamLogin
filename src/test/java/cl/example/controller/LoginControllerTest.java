package cl.example.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.example.config.SecurityConfig;
import cl.example.model.LoginResult;
import cl.example.service.LoginService;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
class LoginControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private LoginService service;

	@Test
	void muestraElFormulario() throws Exception {
		mvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"))
				.andExpect(content().string(containsString("name=\"_csrf\"")));
	}

	@Test
	void loginValidoMuestraLaRespuestaYNoDevuelveLaContrasena() throws Exception {
		given(service.login("juan", "secreta")).willReturn(new LoginResult(true, "0", "Bienvenido"));

		mvc.perform(post("/login").with(csrf()).param("login", "juan").param("password", "secreta"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("result", new LoginResult(true, "0", "Bienvenido")))
				.andExpect(content().string(containsString("Bienvenido")))
				.andExpect(content().string(not(containsString("secreta"))));
	}

	@Test
	void camposVaciosMuestranErroresSinLlamarALaApi() throws Exception {
		mvc.perform(post("/login").with(csrf()).param("login", "").param("password", ""))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("loginForm", "login", "password"))
				.andExpect(model().attributeDoesNotExist("result"));

		verify(service, never()).login(any(), any());
	}

	@Test
	void rechazaPostSinTokenCsrf() throws Exception {
		mvc.perform(post("/login").param("login", "juan").param("password", "secreta"))
				.andExpect(status().isForbidden());

		verify(service, never()).login(any(), any());
	}

}
