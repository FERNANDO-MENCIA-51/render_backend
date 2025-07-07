package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
