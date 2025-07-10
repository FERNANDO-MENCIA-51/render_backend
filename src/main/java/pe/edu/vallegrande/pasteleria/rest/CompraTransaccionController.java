package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.model.dto.CompraRequest;
import pe.edu.vallegrande.pasteleria.service.CompraTransaccionService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/api/compras/transaccion")
@RequiredArgsConstructor
@Tag(name = "compra-transaccion-controller")
public class CompraTransaccionController {
        private final CompraTransaccionService compraTransaccionService;

        // Crear compra completa
        @Operation(summary = "Registrar compra completa")
        @PostMapping
        public ResponseEntity<Compra> registrarCompra(@RequestBody CompraRequest request) {
                Compra compra = compraTransaccionService.registrarCompraTransaccional(request);
                return ResponseEntity.ok(compra);
        }

        // Listar todas las compras con sus detalles
        @Operation(summary = "Listar todas las compras")
        @GetMapping("/all")
        public ResponseEntity<List<Compra>> listarTodasLasCompras() {
                List<Compra> compras = compraTransaccionService.listarTodasLasCompras();
                return ResponseEntity.ok(compras);
        }

        // Consultar compra completa por ID
        @Operation(summary = "Obtener compra por ID")
        @GetMapping("/{id}")
        public ResponseEntity<Compra> obtenerCompraCompleta(@PathVariable Long id) {
                Compra compra = compraTransaccionService.obtenerCompraCompleta(id);
                return ResponseEntity.ok(compra);
        }

        // Actualizar compra completa
        /**
         * Actualiza una compra completa (cabecera y detalles) por su ID.
         */
        @Operation(summary = "Actualizar compra completa")
        @PutMapping("/{id}")
        public ResponseEntity<Compra> actualizarCompraCompleta(@PathVariable Long id,
                        @RequestBody CompraRequest request) {
                Compra compra = compraTransaccionService.actualizarCompraCompleta(id, request);
                return ResponseEntity.ok(compra);
        }

        // Eliminar lógica de compra completa
        /**
         * Elimina lógicamente una compra completa (cabecera y detalles) por su ID.
         */
        @Operation(summary = "Eliminar compra completa (lógico)")
        @PatchMapping("/delete/{id}")
        public ResponseEntity<Void> eliminarCompraCompleta(@PathVariable Long id) {
                compraTransaccionService.eliminarCompraCompleta(id);
                return ResponseEntity.noContent().build();
        }

        // Restaurar compra completa
        @Operation(summary = "Restaurar compra completa")
        @PatchMapping("/restore/{id}")
        public ResponseEntity<Void> restaurarCompraCompleta(@PathVariable Long id) {
                compraTransaccionService.restaurarCompraCompleta(id);
                return ResponseEntity.noContent().build();
        }
}