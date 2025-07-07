package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
