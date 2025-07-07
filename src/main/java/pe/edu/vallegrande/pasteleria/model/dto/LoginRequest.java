package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
