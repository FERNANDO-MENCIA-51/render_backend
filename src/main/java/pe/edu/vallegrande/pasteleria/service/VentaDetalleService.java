package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.VentaDetalle;
import pe.edu.vallegrande.pasteleria.repository.VentaDetalleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaDetalleService {
    private final VentaDetalleRepository ventaDetalleRepository;

    public List<VentaDetalle> findAllActive() {
        return ventaDetalleRepository.findAll().stream().filter(v -> "A".equals(v.getEstado())).toList();
    }

    public List<VentaDetalle> findAllInactive() {
        return ventaDetalleRepository.findAll().stream().filter(v -> "I".equals(v.getEstado())).toList();
    }

    public Optional<VentaDetalle> findById(Long id) {
        return ventaDetalleRepository.findById(id);
    }

    public VentaDetalle save(VentaDetalle ventaDetalle) {
        return ventaDetalleRepository.save(ventaDetalle);
    }

    public void deleteLogic(Long id) {
        ventaDetalleRepository.findById(id).ifPresent(v -> {
            v.setEstado("I");
            ventaDetalleRepository.save(v);
        });
    }

    public void restore(Long id) {
        ventaDetalleRepository.findById(id).ifPresent(v -> {
            v.setEstado("A");
            ventaDetalleRepository.save(v);
        });
    }
}
