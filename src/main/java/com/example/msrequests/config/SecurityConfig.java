package com.example.msrequests.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Habilitar CORS usando la configuración definida en el bean corsConfigurationSource
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 2. Deshabilitar CSRF porque estamos usando una API REST sin estado (comunicada por JSON)
            .csrf(csrf -> csrf.disable())

            // 3. Definir las reglas de autorización para las peticiones HTTP
            .authorizeHttpRequests(auth -> auth
                // Permitir peticiones GET a la ruta de medicamentos sin autenticación
                .requestMatchers(HttpMethod.GET, "/api/v1/medications/**").permitAll()
                
                // Permitir TODAS las peticiones a la ruta de requests para facilitar las pruebas locales
                .requestMatchers("/api/v1/requests/**").permitAll()

                // Para cualquier otra petición, se requiere autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Orígenes permitidos (URL del frontend). Añade aquí la URL de tu frontend.
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000", "http://localhost:5173"));
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cabeceras permitidas (importante para Authorization y nuestra cabecera X-User-Id)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-Id"));
        // Permitir que el navegador envíe credenciales (como cookies o tokens de autorización)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar esta configuración a todas las rutas de la API
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
