package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.service.CompraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/v1/api/compras")
@RequiredArgsConstructor
@Tag(name = "compra-controller")
public class CompraController {
    private final CompraService compraService;

    /**
     * Lista todas las compras (activas e inactivas).
     */
    @Operation(summary = "Listar todas las compras")
    @GetMapping
    public List<Compra> getAll() { return compraService.findAll(); }

    /**
     * Lista todas las compras activas.
     */
    @Operation(summary = "Listar compras activas")
    @GetMapping("/active")
    public List<Compra> getActive() { return compraService.findAllActive(); }

    /**
     * Lista todas las compras inactivas.
     */
    @Operation(summary = "Listar compras inactivas")
    @GetMapping("/inactive")
    public List<Compra> getInactive() { return compraService.findAllInactive(); }

    /**
     * Obtiene una compra por su ID.
     */
    @Operation(summary = "Obtener compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Compra> getById(@PathVariable Long id) {
        return compraService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear compra")
    @PostMapping
    public Compra create(@RequestBody Compra compra) { return compraService.save(compra); }

    @Operation(summary = "Actualizar compra")
    @PutMapping("/{id}")
    public ResponseEntity<Compra> update(@PathVariable Long id, @RequestBody Compra compra) {
        return compraService.findById(id)
                .map(existing -> {
                    compra.setCompraID(id);
                    return ResponseEntity.ok(compraService.save(compra));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina lógicamente una compra (pasa a estado inactivo).
     */
    @Operation(summary = "Eliminar lógicamente compra")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        compraService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una compra eliminada lógicamente (pasa a estado activo).
     */
    @Operation(summary = "Restaurar compra")
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        compraService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
