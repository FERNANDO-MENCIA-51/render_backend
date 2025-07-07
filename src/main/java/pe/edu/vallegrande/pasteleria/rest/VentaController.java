package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Venta;
import pe.edu.vallegrande.pasteleria.service.VentaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/v1/api/ventas")
@RequiredArgsConstructor
@Tag(name = "venta-controller")
public class VentaController {
    private final VentaService ventaService;

    /**
     * Lista todas las ventas (activas e inactivas).
     */
    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public List<Venta> getAll() { return ventaService.findAll(); }

    /**
     * Lista todas las ventas activas.
     */
    @Operation(summary = "Listar ventas activas")
    @GetMapping("/active")
    public List<Venta> getActive() { return ventaService.findAllActive(); }

    /**
     * Lista todas las ventas inactivas.
     */
    @Operation(summary = "Listar ventas inactivas")
    @GetMapping("/inactive")
    public List<Venta> getInactive() { return ventaService.findAllInactive(); }

    /**
     * Obtiene una venta por su ID.
     */
    @Operation(summary = "Obtener venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getById(@PathVariable Long id) {
        return ventaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva venta.
     */
    @Operation(summary = "Crear venta")
    @PostMapping
    public Venta create(@RequestBody Venta venta) { return ventaService.save(venta); }

    /**
     * Actualiza una venta existente.
     */
    @Operation(summary = "Actualizar venta")
    @PutMapping("/{id}")
    public ResponseEntity<Venta> update(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.findById(id)
                .map(existing -> {
                    venta.setVentaID(id);
                    return ResponseEntity.ok(ventaService.save(venta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina lógicamente una venta (pasa a estado inactivo).
     */
    @Operation(summary = "Eliminar lógicamente venta")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        ventaService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una venta eliminada lógicamente (pasa a estado activo).
     */
    @Operation(summary = "Restaurar venta")
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        ventaService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
