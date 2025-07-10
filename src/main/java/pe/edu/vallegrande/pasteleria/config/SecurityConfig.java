package pe.edu.vallegrande.pasteleria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.vallegrande.pasteleria.service.JwtUtil;
import pe.edu.vallegrande.pasteleria.repository.UsuarioLoginRepository;

import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioLoginRepository usuarioLoginRepository;

    @Value("${app.security.allowed-origins:http://localhost:3000,http://localhost:4200}")
    private String allowedOrigins;

    @Value("${app.security.max-age:3600}")
    private long corsMaxAge;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .contentTypeOptions(contentTypeOptions -> {
                        })
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true))) // Cambiar includeSubdomains por includeSubDomains
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v1/api/auth/**",
                                "/actuator/health")
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/v1/api/**").permitAll()
                        // Permitir GET de reportes a ambos roles
                        .requestMatchers(HttpMethod.GET, "/v1/api/reportes/**").authenticated()
                        // ADMIN puede todo
                        .requestMatchers("/v1/api/**").hasRole("ADMIN")
                        // USUARIO solo puede listar (GET)
                        .requestMatchers(HttpMethod.GET, "/v1/api/**").hasRole("USUARIO")
                        .anyRequest().denyAll())
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, usuarioLoginRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            logger.warn("Intento de acceso no autorizado desde IP: {} a URL: {}",
                                    getClientIP(request), request.getRequestURI());
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(
                                    "{\"error\":\"No autorizado\",\"message\":\"Credenciales inválidas\",\"timestamp\":\""
                                            +
                                            java.time.Instant.now() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            logger.warn("Acceso denegado para usuario: {} desde IP: {} a URL: {}",
                                    SecurityContextHolder.getContext().getAuthentication().getName(),
                                    getClientIP(request), request.getRequestURI());
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"error\":\"Acceso denegado\",\"message\":\"" +
                                    "No tiene permisos para acceder a este recurso\",\"timestamp\":\"" +
                                    java.time.Instant.now() + "\"}");
                        }));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String[] origins = allowedOrigins.split(",");
        for (String origin : origins) {
            configuration.addAllowedOrigin(origin.trim());
        }
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(corsMaxAge);
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("X-Total-Count");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}