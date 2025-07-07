package pe.edu.vallegrande.pasteleria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Cliente;
import pe.edu.vallegrande.pasteleria.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public List<Cliente> findAllActive() {
        return clienteRepository.findAll().stream().filter(c -> "A".equals(c.getEstado())).toList();
    }

    public List<Cliente> findAllInactive() {
        return clienteRepository.findAll().stream().filter(c -> "I".equals(c.getEstado())).toList();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deleteLogic(Long id) {
        clienteRepository.findById(id).ifPresent(c -> {
            c.setEstado("I");
            clienteRepository.save(c);
        });
    }

    public void restore(Long id) {
        clienteRepository.findById(id).ifPresent(c -> {
            c.setEstado("A");
            clienteRepository.save(c);
        });
    }
}
