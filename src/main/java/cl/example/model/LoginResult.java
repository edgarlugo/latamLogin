package cl.example.model;

/**
 * Resultado que se muestra en la vista.
 *
 * @param respondida {@code true} si la API contestó (con éxito o rechazo);
 *                   {@code false} si no fue posible comunicarse con ella.
 */
public record LoginResult(boolean respondida, String codigo, String mensaje) {

	public static LoginResult from(LoginApiResponse response) {
		return new LoginResult(true, response.codigo(), response.mensaje());
	}

	public static LoginResult sinRespuesta() {
		return new LoginResult(false, null, "No fue posible contactar el servicio de login. Intenta nuevamente.");
	}

}
