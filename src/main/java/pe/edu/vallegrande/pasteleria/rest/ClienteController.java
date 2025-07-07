package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Cliente;
import pe.edu.vallegrande.pasteleria.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/v1/api/clientes")
@RequiredArgsConstructor
@Tag(name = "cliente-controller")
public class ClienteController {
    private final ClienteService clienteService;

    /**
     * Lista todos los clientes (activos e inactivos).
     */
    @io.swagger.v3.oas.annotations.Operation(summary = "Listar todos los clientes")
    @GetMapping
    public List<Cliente> getAll() { return clienteService.findAll(); }

    /**
     * Lista todos los clientes activos.
     */
    @Operation(summary = "Listar clientes activos")
    @GetMapping("/active")
    public List<Cliente> getActive() { return clienteService.findAllActive(); }

    /**
     * Lista todos los clientes inactivos.
     */
    @Operation(summary = "Listar clientes inactivos")
    @GetMapping("/inactive")
    public List<Cliente> getInactive() { return clienteService.findAllInactive(); }

    /**
     * Obtiene un cliente por su ID.
     */
    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear cliente")
    @PostMapping
    public Cliente create(@RequestBody Cliente cliente) { return clienteService.save(cliente); }

    @Operation(summary = "Actualizar cliente")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.findById(id)
                .map(existing -> {
                    cliente.setClienteID(id);
                    return ResponseEntity.ok(clienteService.save(cliente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar lógicamente cliente")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        clienteService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restaurar cliente")
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        clienteService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
