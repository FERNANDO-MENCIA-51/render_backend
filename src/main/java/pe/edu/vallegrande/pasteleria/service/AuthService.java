package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.UsuarioLogin;
import pe.edu.vallegrande.pasteleria.model.dto.LoginRequest;
import pe.edu.vallegrande.pasteleria.model.dto.LoginResponse;
import pe.edu.vallegrande.pasteleria.model.dto.RegisterRequest;
import pe.edu.vallegrande.pasteleria.model.dto.RegisterResponse;
import pe.edu.vallegrande.pasteleria.repository.UsuarioLoginRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioLoginRepository usuarioLoginRepository;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder;

    public RegisterResponse update(String username, RegisterRequest request) {
        UsuarioLogin usuario = usuarioLoginRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRol() != null && !request.getRol().isEmpty()) {
            usuario.setRol(request.getRol());
        }
        usuarioLoginRepository.save(usuario);
        return new RegisterResponse(usuario.getUsername(), usuario.getRol(), "Usuario actualizado exitosamente");
    }

    public void deleteLogic(String username) {
        UsuarioLogin usuario = usuarioLoginRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado("I");
        usuarioLoginRepository.save(usuario);
    }

    public void restore(String username) {
        UsuarioLogin usuario = usuarioLoginRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado("A");
        usuarioLoginRepository.save(usuario);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UsuarioLogin usuario = usuarioLoginRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol());
        return new LoginResponse(token, usuario.getUsername(), usuario.getRol());
    }


public RegisterResponse register(RegisterRequest request) {
    if (usuarioLoginRepository.findByUsername(request.getUsername()).isPresent()) {
        throw new RuntimeException("El usuario ya existe");
    }
    String hash = passwordEncoder.encode(request.getPassword());
    UsuarioLogin usuario = new UsuarioLogin();
    usuario.setUsername(request.getUsername());
    usuario.setPasswordHash(hash);
    usuario.setRol(request.getRol() != null ? request.getRol() : "usuario");
    usuario.setEstado("A");
    usuarioLoginRepository.save(usuario);
    return new RegisterResponse(usuario.getUsername(), usuario.getRol(), "Usuario registrado exitosamente");
}

}
