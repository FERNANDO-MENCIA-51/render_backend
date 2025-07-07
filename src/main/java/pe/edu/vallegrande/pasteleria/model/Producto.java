package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Entidad que representa un producto disponible para la venta o compra.
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    /**
     * Identificador único del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoID;

    @Column(name = "code_barra", unique = true, length = 20)
    private String codeBarra;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "estatus", nullable = false, length = 1)
    private String estatus;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @ManyToOne
    @JoinColumn(name = "fk_suppliers_id", nullable = false)
    private Supplier supplier;
}
