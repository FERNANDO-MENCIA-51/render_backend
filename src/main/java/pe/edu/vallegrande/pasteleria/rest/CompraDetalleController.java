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
    private final CompraDetalleService compraDetalleService;

    @GetMapping("/active")
    public List<CompraDetalle> getActive() { return compraDetalleService.findAllActive(); }

    @GetMapping("/inactive")
    public List<CompraDetalle> getInactive() { return compraDetalleService.findAllInactive(); }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDetalle> getById(@PathVariable Long id) {
        return compraDetalleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear detalle de compra")
    public CompraDetalle create(@RequestBody CompraDetalle compraDetalle) { return compraDetalleService.save(compraDetalle); }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle de compra")
    public ResponseEntity<CompraDetalle> update(@PathVariable Long id, @RequestBody CompraDetalle compraDetalle) {
        return compraDetalleService.findById(id)
                .map(existing -> {
                    compraDetalle.setCompraDetalleID(id);
                    return ResponseEntity.ok(compraDetalleService.save(compraDetalle));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        compraDetalleService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        compraDetalleService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
