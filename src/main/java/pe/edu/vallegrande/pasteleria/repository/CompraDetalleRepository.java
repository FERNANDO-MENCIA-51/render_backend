package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;

public interface CompraDetalleRepository extends JpaRepository<CompraDetalle, Long> {
}
