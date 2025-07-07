package pe.edu.vallegrande.pasteleria.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.pasteleria.model.Supplier;
import pe.edu.vallegrande.pasteleria.service.SupplierService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/v1/api/suppliers")
@RequiredArgsConstructor
@Tag(name = "supplier-controller")
public class SupplierController {
    private final SupplierService supplierService;

    /**
     * Lista todos los proveedores (activos e inactivos).
     */
    @Operation(summary = "Listar todos los proveedores")
    @GetMapping
    public List<Supplier> getAll() { return supplierService.findAll(); }

    /**
     * Lista todos los proveedores activos.
     */
    @Operation(summary = "Listar proveedores activos")
    @GetMapping("/active")
    public List<Supplier> getActive() { return supplierService.findAllActive(); }

    /**
     * Lista todos los proveedores inactivos.
     */
    @Operation(summary = "Listar proveedores inactivos")
    @GetMapping("/inactive")
    public List<Supplier> getInactive() { return supplierService.findAllInactive(); }

    /**
     * Obtiene un proveedor por su ID.
     */
    @Operation(summary = "Obtener proveedor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable Long id) {
        return supplierService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear proveedor")
    @PostMapping
    public Supplier create(@RequestBody Supplier supplier) { return supplierService.save(supplier); }

    @Operation(summary = "Actualizar proveedor")
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable Long id, @RequestBody Supplier supplier) {
        return supplierService.findById(id)
                .map(existing -> {
                    supplier.setSupplierID(id);
                    return ResponseEntity.ok(supplierService.save(supplier));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar lógicamente proveedor")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLogic(@PathVariable Long id) {
        supplierService.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restaurar proveedor")
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        supplierService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
