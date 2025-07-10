package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;
import pe.edu.vallegrande.pasteleria.service.CompraDetalleService;
import java.util.List;

@RestController
@RequestMapping("/v1/api/compra-detalle")
@RequiredArgsConstructor
public class CompraDetalleController {
    @GetMapping
    @Operation(summary = "Listar todos los detalles de compra")
    public List<CompraDetalle> getAll() {
        return compraDetalleService.findAll();
    }
    private final CompraDetalleService compraDetalleService;

    @GetMapping("/active")
    @Operation(summary = "Listar detalles de compra activos")
    public List<CompraDetalle> getActive() { return compraDetalleService.findAllActive(); }

    @GetMapping("/inactive")
    @Operation(summary = "Listar detalles de compra inactivos")
    public List<CompraDetalle> getInactive() { return compraDetalleService.findAllInactive(); }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de compra por ID")
    public ResponseEntity<CompraDetalle> getById(@PathVariable Long id) {
        return compraDetalleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo detalle de compra")
    public CompraDetalle create(@RequestBody CompraDetalle compraDetalle) { return compraDetalleService.save(compraDetalle); }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un detalle de compra existente")
    public ResponseEntity<CompraDetalle> update(@PathVariable Long id, @RequestBody CompraDetalle compraDetalle) {
        return compraDetalleService.findById(id)
                .map(existing -> {
                    compraDetalle.setCompraDetalleID(id);
                    return ResponseEntity.ok(compraDetalleService.save(compraDetalle));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/delete/{id}")
    @Operation(summary = "Eliminar lógicamente un detalle de compra por ID")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        compraDetalleService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restore/{id}")
    @Operation(summary = "Restaurar un detalle de compra previamente eliminado por ID")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        compraDetalleService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
