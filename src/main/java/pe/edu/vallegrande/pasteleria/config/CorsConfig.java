package pe.edu.vallegrande.pasteleria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class CorsConfig {

    @Value("${app.security.allowed-origins:http://localhost:*,https://localhost:*}")
    private String allowedOrigins;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                if ("dev".equals(activeProfile)) {
                    // Configuración flexible para desarrollo (incluye Flutter)
                    registry.addMapping("/**")
                            .allowedOriginPatterns(
                                    "http://localhost:*",
                                    "https://localhost:*",
                                    "http://127.0.0.1:*",
                                    "http://192.168.*.*:*",
                                    "http://10.0.2.2:*", // Android Emulator
                                    "http://10.0.3.2:*", // Android Emulator (Genymotion)
                                    "http://172.16.*.*:*", // Docker/VM networks
                                    "capacitor://localhost", // Capacitor (si usas)
                                    "ionic://localhost", // Ionic (si usas)
                                    "tauri://localhost" // Tauri (si usas)
                    )
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                            .allowedHeaders("*")
                            .exposedHeaders("Authorization", "X-Total-Count", "Content-Type")
                            .allowCredentials(true)
                            .maxAge(3600);
                } else {
                    // Configuración restrictiva para producción
                    registry.addMapping("/**")
                            .allowedOrigins(
                                    "https://tudominio.com",
                                    "https://www.tudominio.com",
                                    "https://app.tudominio.com" // Para Flutter Web en producción
                    )
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                            .allowedHeaders("Authorization", "Content-Type", "X-Requested-With")
                            .exposedHeaders("Authorization", "X-Total-Count")
                            .allowCredentials(true)
                            .maxAge(86400); // 24 horas para producción
                }
            }
        };
    }
}
