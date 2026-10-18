package cl.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import cl.example.config.LoginApiProperties;
import cl.example.model.LoginResult;

class LoginServiceTest {

	private static final String API_URL = "https://api.test/login";

	private MockRestServiceServer server;
	private LoginService service;

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		server = MockRestServiceServer.bindTo(builder).build();
		LoginApiProperties properties = new LoginApiProperties(URI.create(API_URL), "ES-es", Duration.ofSeconds(1),
				Duration.ofSeconds(1));
		service = new LoginService(builder.build(), properties);
	}

	@Test
	void enviaCredencialesEnElCuerpoYDevuelveLaRespuesta() {
		server.expect(requestTo(API_URL))
				.andExpect(method(HttpMethod.POST))
				.andExpect(header("Locale", "ES-es"))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json("""
						{"login":"juan","password":"secreta"}
						""", JsonCompareMode.STRICT))
				.andRespond(withSuccess("""
						{"codigo":"0","mensaje":"Bienvenido"}
						""", MediaType.APPLICATION_JSON));

		LoginResult result = service.login("juan", "secreta");

		assertThat(result).isEqualTo(new LoginResult(true, "0", "Bienvenido"));
		server.verify();
	}

	@Test
	void credencialesRechazadasMuestranElMensajeDeLaApi() {
		server.expect(requestTo(API_URL))
				.andRespond(withStatus(HttpStatus.UNAUTHORIZED)
						.contentType(MediaType.APPLICATION_JSON)
						.body("""
								{"codigo":"401","mensaje":"Usuario o contraseña inválidos"}
								"""));

		LoginResult result = service.login("juan", "mala");

		assertThat(result).isEqualTo(new LoginResult(true, "401", "Usuario o contraseña inválidos"));
	}

	@Test
	void errorDelServidorDevuelveResultadoSinRespuesta() {
		server.expect(requestTo(API_URL)).andRespond(withServerError());

		LoginResult result = service.login("juan", "secreta");

		assertThat(result.respondida()).isFalse();
		assertThat(result.mensaje()).isNotBlank();
	}

}
