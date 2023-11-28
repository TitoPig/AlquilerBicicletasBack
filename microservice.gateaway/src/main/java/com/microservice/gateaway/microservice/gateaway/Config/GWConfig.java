package com.microservice.gateaway.microservice.gateaway.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class GWConfig {

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${tpi.url-microservicio-estaciones}") String uriEstaciones,
                                        @Value("${tpi.url-microservicio-alquileres}") String uriAlquileres) {
        return builder.routes()
                // Ruteo al Microservicio de estaciones
                .route(p -> p.path("/api/estaciones/**").uri(uriEstaciones))
                // Ruteo al Microservicio de alquileres
                .route(p -> p.path("/api/alquileres/**").uri(uriAlquileres))
                .build();
    }
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange(exchanges -> exchanges
                        // Establecer permisos para endpoints de estaciones
                        .pathMatchers(HttpMethod.GET, "/api/estaciones/**")
                        .hasAnyRole("CLIENTE", "ADMINISTRADOR")

                        .pathMatchers(HttpMethod.POST, "/api/estaciones/**")
                        .hasRole("ADMINISTRADOR")

                        // Establecer permisos para endpoints de alquileres
                        .pathMatchers(HttpMethod.GET, "/api/alquileres/**")
                        .hasRole("ADMINISTRADOR")

                        .pathMatchers(HttpMethod.POST, "/api/alquileres/**")
                        .hasAnyRole("CLIENTE", "ADMINISTRADOR")

                        // Cualquier otra petición...
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable);

        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));
        // jwtAuthenticationConverter.setPrincipalClaimName("user_name");

        return jwtAuthenticationConverter;
    }
}
