package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.UsuarioLogin;
import pe.edu.vallegrande.pasteleria.repository.UsuarioLoginRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioLoginRepository usuarioLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Intentando login para: " + username);
        UsuarioLogin usuario = usuarioLoginRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        System.out.println("Hash obtenido de BD: " + usuario.getPasswordHash());
        System.out.println("Rol obtenido: " + usuario.getRol());
        return new User(
                usuario.getUsername(),
                usuario.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase())));
    }
}