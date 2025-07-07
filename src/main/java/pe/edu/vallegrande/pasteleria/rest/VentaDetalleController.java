package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import pe.edu.vallegrande.pasteleria.model.VentaDetalle;
import pe.edu.vallegrande.pasteleria.service.VentaDetalleService;
import java.util.List;

@RestController
@RequestMapping("/v1/api/venta-detalle")
@RequiredArgsConstructor
public class VentaDetalleController {
    private final VentaDetalleService ventaDetalleService;

    @GetMapping("/active")
    public List<VentaDetalle> getActive() { return ventaDetalleService.findAllActive(); }

    @GetMapping("/inactive")
    public List<VentaDetalle> getInactive() { return ventaDetalleService.findAllInactive(); }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDetalle> getById(@PathVariable Long id) {
        return ventaDetalleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear detalle de venta")
    public VentaDetalle create(@RequestBody VentaDetalle ventaDetalle) { return ventaDetalleService.save(ventaDetalle); }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar detalle de venta")
    public ResponseEntity<VentaDetalle> update(@PathVariable Long id, @RequestBody VentaDetalle ventaDetalle) {
        return ventaDetalleService.findById(id)
                .map(existing -> {
                    ventaDetalle.setDetalleID(id);
                    return ResponseEntity.ok(ventaDetalleService.save(ventaDetalle));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        ventaDetalleService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        ventaDetalleService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
