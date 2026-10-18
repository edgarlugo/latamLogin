package cl.example.config;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Configuración de la API externa de login ({@code latam.login-api.*}).
 */
@Validated
@ConfigurationProperties("latam.login-api")
public record LoginApiProperties(
		@NotNull URI url,
		@NotBlank @DefaultValue("ES-es") String locale,
		@DefaultValue("5s") Duration connectTimeout,
		@DefaultValue("10s") Duration readTimeout) {
}
