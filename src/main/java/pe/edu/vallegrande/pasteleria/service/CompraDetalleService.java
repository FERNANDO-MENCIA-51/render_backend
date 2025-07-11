package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;
import pe.edu.vallegrande.pasteleria.repository.CompraDetalleRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompraDetalleService {
    private final CompraDetalleRepository compraDetalleRepository;

    public List<CompraDetalle> findAll() {
        return compraDetalleRepository.findAll();
    }

    public Optional<CompraDetalle> findById(Long id) {
        return compraDetalleRepository.findById(id);
    }

    public CompraDetalle save(CompraDetalle compraDetalle) {
        return compraDetalleRepository.save(compraDetalle);
    }

    public void deleteById(Long id) {
        compraDetalleRepository.deleteById(id);
    }
}
