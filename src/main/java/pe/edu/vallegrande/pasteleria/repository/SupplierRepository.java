package pe.edu.vallegrande.pasteleria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.vallegrande.pasteleria.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
