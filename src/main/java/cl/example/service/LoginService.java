package cl.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import cl.example.config.LoginApiProperties;
import cl.example.model.LoginApiRequest;
import cl.example.model.LoginApiResponse;
import cl.example.model.LoginResult;

@Service
public class LoginService {

	private static final Logger log = LoggerFactory.getLogger(LoginService.class);

	private final RestClient restClient;
	private final LoginApiProperties properties;

	public LoginService(RestClient restClient, LoginApiProperties properties) {
		this.restClient = restClient;
		this.properties = properties;
	}

	public LoginResult login(String login, String password) {
		try {
			LoginApiResponse response = restClient.post()
					.uri(properties.url())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.header("Locale", properties.locale())
					.body(new LoginApiRequest(login, password))
					.retrieve()
					.body(LoginApiResponse.class);
			return response != null ? LoginResult.from(response) : LoginResult.sinRespuesta();
		}
		catch (HttpClientErrorException ex) {
			// Credenciales rechazadas: la API puede devolver código y mensaje en el cuerpo del 4xx
			log.debug("Login rechazado por la API con estado {}", ex.getStatusCode());
			LoginApiResponse body = ex.getResponseBodyAs(LoginApiResponse.class);
			return body != null ? LoginResult.from(body) : LoginResult.sinRespuesta();
		}
		catch (RestClientException ex) {
			log.warn("Error al invocar la API de login: {}", ex.getMessage());
			return LoginResult.sinRespuesta();
		}
	}

}
