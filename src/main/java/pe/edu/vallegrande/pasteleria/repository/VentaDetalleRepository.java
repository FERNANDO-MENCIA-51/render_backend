package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.VentaDetalle;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
}
