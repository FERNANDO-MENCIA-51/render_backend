package pe.edu.vallegrande.pasteleria.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Sistema de Pastelería",
        version = "1.0.0",
        description = "API REST para la gestión integral de una pastelería: clientes, productos, ventas, compras y proveedores.",
        contact = @Contact(
            name = "Vallegrande Dev Team",
            email = "soporte@vallegrande.edu.pe",
            url = "https://github.com/vallegrande/AS232S4_T05-be"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        ),
        termsOfService = "https://vallegrande.edu.pe/terminos"
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor de Desarrollo Local"),
        @Server(url = "https://as232s4-t05-be.vallegrande.edu.pe", description = "Servidor de Producción Vallegrande")
    },
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = """
        Token JWT obtenido del endpoint de login.

        **Formato:** Bearer {token}

        **Ejemplo:** Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

        **Duración:** 24 horas

        **Roles soportados:** ADMIN, USUARIO
        """)
public class OpenApiConfig {
}
