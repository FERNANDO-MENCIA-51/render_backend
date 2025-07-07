package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Supplier;
import pe.edu.vallegrande.pasteleria.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public List<Supplier> findAllActive() {
        return supplierRepository.findAll().stream().filter(s -> "A".equals(s.getEstatus())).toList();
    }

    public List<Supplier> findAllInactive() {
        return supplierRepository.findAll().stream().filter(s -> "I".equals(s.getEstatus())).toList();
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public void deleteLogic(Long id) {
        supplierRepository.findById(id).ifPresent(s -> {
            s.setEstatus("I");
            supplierRepository.save(s);
        });
    }

    public void restore(Long id) {
        supplierRepository.findById(id).ifPresent(s -> {
            s.setEstatus("A");
            supplierRepository.save(s);
        });
    }
}
