package cl.example.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cl.example.model.Response;

@Service
public class LoginService {
	@Autowired
	private RestTemplate restTemplate;
	String url = "https://www.ficcion.cl/ws/api/login";

	public Response Login(String login, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		headers.add("Locale", "ES-es");

		
		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		ResponseEntity<Response> response = restTemplate.exchange(
		        url,
		        HttpMethod.POST,
		        request,
		        Response.class
		);		
		return response.getBody();
	}

}
