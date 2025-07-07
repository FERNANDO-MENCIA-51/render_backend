package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.repository.CompraRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompraService {
    private final CompraRepository compraRepository;

    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    public List<Compra> findAllActive() {
        return compraRepository.findAll().stream().filter(c -> "A".equals(c.getEstado())).toList();
    }

    public List<Compra> findAllInactive() {
        return compraRepository.findAll().stream().filter(c -> "I".equals(c.getEstado())).toList();
    }

    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }

    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }

    public void deleteLogic(Long id) {
        compraRepository.findById(id).ifPresent(c -> {
            c.setEstado("I");
            compraRepository.save(c);
        });
    }

    public void restore(Long id) {
        compraRepository.findById(id).ifPresent(c -> {
            c.setEstado("A");
            compraRepository.save(c);
        });
    }
}
