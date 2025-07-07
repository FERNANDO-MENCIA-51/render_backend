package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.Data;

/**
 * DTO para registrar un nuevo usuario.
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String rol; // 'admin' o 'usuario'
}
