package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.UsuarioLogin;
import java.util.Optional;

public interface UsuarioLoginRepository extends JpaRepository<UsuarioLogin, Long> {
    Optional<UsuarioLogin> findByUsername(String username);
}
