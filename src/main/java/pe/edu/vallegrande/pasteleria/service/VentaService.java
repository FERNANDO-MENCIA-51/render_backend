package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Venta;
import pe.edu.vallegrande.pasteleria.repository.VentaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaService {
    private final VentaRepository ventaRepository;

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public List<Venta> findAllActive() {
        return ventaRepository.findAll().stream().filter(v -> "A".equals(v.getEstado())).toList();
    }

    public List<Venta> findAllInactive() {
        return ventaRepository.findAll().stream().filter(v -> "I".equals(v.getEstado())).toList();
    }

    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void deleteLogic(Long id) {
        ventaRepository.findById(id).ifPresent(v -> {
            v.setEstado("I");
            ventaRepository.save(v);
        });
    }

    public void restore(Long id) {
        ventaRepository.findById(id).ifPresent(v -> {
            v.setEstado("A");
            ventaRepository.save(v);
        });
    }
}
