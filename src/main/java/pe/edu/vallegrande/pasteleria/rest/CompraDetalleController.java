package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;
import pe.edu.vallegrande.pasteleria.service.CompraDetalleService;
import java.util.List;

@RestController
@RequestMapping("/v1/api/compra-detalle")
@RequiredArgsConstructor
public class CompraDetalleController {
    private final CompraDetalleService compraDetalleService;
    @org.springframework.beans.factory.annotation.Autowired
    private pe.edu.vallegrande.pasteleria.service.CompraTransaccionService compraTransaccionService;

    @GetMapping
    public List<CompraDetalle> getAll() {
        return compraDetalleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDetalle> getById(@PathVariable Long id) {
        return compraDetalleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CompraDetalle create(@RequestBody CompraDetalle compraDetalle) {
        return compraDetalleService.save(compraDetalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDetalle> update(@PathVariable Long id, @RequestBody CompraDetalle compraDetalle) {
        return compraDetalleService.findById(id)
                .map(existing -> {
                    compraDetalle.setCompraDetalleID(id);
                    return ResponseEntity.ok(compraDetalleService.save(compraDetalle));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        compraDetalleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint transaccional para registrar compra y detalles
    @PostMapping("/transaccional")
    public ResponseEntity<pe.edu.vallegrande.pasteleria.model.Compra> registrarCompraConDetalles(
            @RequestBody pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionRequest request
    ) {
        pe.edu.vallegrande.pasteleria.model.Compra compra = compraTransaccionService.registrarCompraConDetalles(request);
        return ResponseEntity.ok(compra);
    }
}
