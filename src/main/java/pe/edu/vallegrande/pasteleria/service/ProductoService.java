package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Producto;
import pe.edu.vallegrande.pasteleria.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public List<Producto> findAllActive() {
        return productoRepository.findAll().stream().filter(p -> "A".equals(p.getEstatus())).toList();
    }

    public List<Producto> findAllInactive() {
        return productoRepository.findAll().stream().filter(p -> "I".equals(p.getEstatus())).toList();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteLogic(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setEstatus("I");
            productoRepository.save(p);
        });
    }

    public void restore(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setEstatus("A");
            productoRepository.save(p);
        });
    }
}
