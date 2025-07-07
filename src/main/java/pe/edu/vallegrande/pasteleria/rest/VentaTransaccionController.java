package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Venta;
import pe.edu.vallegrande.pasteleria.model.dto.VentaRequest;
import pe.edu.vallegrande.pasteleria.service.VentaTransaccionService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/ventas/transaccion")
@RequiredArgsConstructor
@Tag(name = "venta-transaccion-controller")
public class VentaTransaccionController {
    private final VentaTransaccionService ventaTransaccionService;

    // Crear venta completa
    @Operation(summary = "Registrar venta completa")
    @PostMapping
    public ResponseEntity<Venta> registrarVentaCompleta(@RequestBody VentaRequest request) {
        Venta venta = ventaTransaccionService.registrarVentaCompleta(request);
        return ResponseEntity.ok(venta);
    }

    // Listar todas las ventas con sus detalles
    @Operation(summary = "Listar todas las ventas")
    @GetMapping("/all")
    public ResponseEntity<List<Venta>> listarTodasLasVentas() {
        List<Venta> ventas = ventaTransaccionService.listarTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    // Consultar venta completa por ID
    @Operation(summary = "Obtener venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaCompleta(@PathVariable Long id) {
        Venta venta = ventaTransaccionService.obtenerVentaCompleta(id);
        return ResponseEntity.ok(venta);
    }

    // Actualizar venta completa
    /**
     * Actualiza una venta completa (cabecera y detalles) por su ID.
     */
    @Operation(summary = "Actualizar venta completa")
    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizarVentaCompleta(@PathVariable Long id, @RequestBody VentaRequest request) {
        Venta venta = ventaTransaccionService.actualizarVentaCompleta(id, request);
        return ResponseEntity.ok(venta);
    }

    // Eliminar lógica de venta completa
    /**
     * Elimina lógicamente una venta completa (cabecera y detalles) por su ID.
     */
    @Operation(summary = "Eliminar venta completa (lógico)")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarVentaCompleta(@PathVariable Long id) {
        ventaTransaccionService.eliminarVentaCompleta(id);
        return ResponseEntity.noContent().build();
    }

    // Restaurar venta completa
    /**
     * Restaura una venta completa (cabecera y detalles) por su ID.
     */
    @Operation(summary = "Restaurar venta completa")
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restaurarVentaCompleta(@PathVariable Long id) {
        ventaTransaccionService.restaurarVentaCompleta(id);
        return ResponseEntity.noContent().build();
    }
}

