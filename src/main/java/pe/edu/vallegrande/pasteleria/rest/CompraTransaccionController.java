package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionRequest;
import pe.edu.vallegrande.pasteleria.service.CompraTransaccionService;

@RestController
@RequestMapping("/v1/api/compra-transaccion")
@RequiredArgsConstructor
public class CompraTransaccionController {
    private final CompraTransaccionService compraTransaccionService;

    @io.swagger.v3.oas.annotations.Operation(summary = "Registrar compra con detalles (cabecera y detalle)")
    @PostMapping
    public ResponseEntity<Compra> registrarCompraConDetalles(@RequestBody CompraTransaccionRequest request) {
        Compra compra = compraTransaccionService.registrarCompraConDetalles(request);
        return ResponseEntity.ok(compra);
    }

    // Listar todas las compras con detalles
    @io.swagger.v3.oas.annotations.Operation(summary = "Listar todas las compras con sus detalles")
    @GetMapping
    public ResponseEntity<java.util.List<pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse>> listarComprasConDetalles() {
        return ResponseEntity.ok(compraTransaccionService.listarComprasConDetalles());
    }

    // Obtener una compra por ID con detalles
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener una compra por ID con sus detalles")
    @GetMapping("/{id}")
    public ResponseEntity<pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse> obtenerCompraConDetalles(@PathVariable Long id) {
        return ResponseEntity.ok(compraTransaccionService.obtenerCompraConDetalles(id));
    }

    // Editar una compra y sus detalles
    @io.swagger.v3.oas.annotations.Operation(summary = "Editar una compra y sus detalles")
    @PutMapping("/{id}")
    public ResponseEntity<Void> editarCompraConDetalles(@PathVariable Long id, @RequestBody pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionRequest request) {
        compraTransaccionService.editarCompraConDetalles(id, request);
        return ResponseEntity.noContent().build();
    }

    // Eliminar (estado 'I') una compra y sus detalles
    @io.swagger.v3.oas.annotations.Operation(summary = "Eliminar (baja lógica) una compra y sus detalles")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        compraTransaccionService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar (estado 'A') una compra y sus detalles
    @io.swagger.v3.oas.annotations.Operation(summary = "Restaurar (alta lógica) una compra y sus detalles")
    @PutMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurarCompra(@PathVariable Long id) {
        compraTransaccionService.restaurarCompra(id);
        return ResponseEntity.noContent().build();
    }
}
