package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Respuesta al registrar un usuario.
 */
@Data
@AllArgsConstructor
public class RegisterResponse {
    private String username;
    private String rol;
    private String message;
}
