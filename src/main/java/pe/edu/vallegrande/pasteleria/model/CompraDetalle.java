package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa el detalle de una compra (producto, cantidad, precio, etc).
 * Relaciona cada línea de producto con la compra principal.
 */
@Entity
@Table(name = "compra_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compraDetalleID")
    private Long compraDetalleID;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

    @Column(name = "ruc", nullable = false, length = 11)
    private String ruc;

    @ManyToOne
    @JoinColumn(name = "fk_producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "fk_compra_id", nullable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "fk_supplier_id", nullable = false)
    private Supplier supplier;
}
