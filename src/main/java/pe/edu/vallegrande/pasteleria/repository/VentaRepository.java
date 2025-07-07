package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
