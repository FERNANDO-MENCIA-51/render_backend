package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.dto.LoginRequest;
import pe.edu.vallegrande.pasteleria.model.dto.LoginResponse;
import pe.edu.vallegrande.pasteleria.service.AuthService;
import pe.edu.vallegrande.pasteleria.model.dto.RegisterRequest;
import pe.edu.vallegrande.pasteleria.model.dto.RegisterResponse;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @io.swagger.v3.oas.annotations.Operation(summary = "Actualizar usuario")
    @PutMapping("/{username}")
    public ResponseEntity<RegisterResponse> update(@PathVariable String username, @RequestBody RegisterRequest request) {
        // Implementa la lógica real en AuthService
        return ResponseEntity.ok(authService.update(username, request));
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Eliminar lógicamente usuario")
    @PatchMapping("/delete/{username}")
    public ResponseEntity<Void> deleteLogic(@PathVariable String username) {
        authService.deleteLogic(username);
        return ResponseEntity.noContent().build();
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Restaurar usuario")
    @PatchMapping("/restore/{username}")
    public ResponseEntity<Void> restore(@PathVariable String username) {
        authService.restore(username);
        return ResponseEntity.noContent().build();
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Registrar usuario")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
