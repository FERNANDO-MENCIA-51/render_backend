package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Producto;
import pe.edu.vallegrande.pasteleria.service.ProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/v1/api/productos")
@RequiredArgsConstructor
@Tag(name = "producto-controller"   )
public class ProductoController {
    private final ProductoService productoService;

    /**
     * Lista todos los productos (activos e inactivos).
     */
    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public List<Producto> getAll() { return productoService.findAll(); }

    @Operation(summary = "Listar productos activos")
    @GetMapping("/active")
    public List<Producto> getActive() { return productoService.findAllActive(); }

    @Operation(summary = "Listar productos inactivos")
    @GetMapping("/inactive")
    public List<Producto> getInactive() { return productoService.findAllInactive(); }

    @Operation(summary = "Obtener producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear producto")
    @PostMapping
    public Producto create(@RequestBody Producto producto) { return productoService.save(producto); }

    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.findById(id)
                .map(existing -> {
                    producto.setProductoID(id);
                    return ResponseEntity.ok(productoService.save(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar lógicamente producto")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        productoService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restaurar producto")
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        productoService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
