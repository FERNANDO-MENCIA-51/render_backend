package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa el detalle de una venta (producto, cantidad, precio, etc).
 * Relaciona cada línea de producto con la venta principal.
 */
@Entity
@Table(name = "venta_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {
    /** ID único del detalle de venta */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detalleID;

    /** Cantidad del producto vendido */
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** Precio unitario del producto */
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    /** Subtotal de la línea (cantidad * precio unitario) */
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    /** Estado lógico ('A'=Activo, 'I'=Inactivo) */
    @Column(nullable = false, length = 1)
    private String estado;

    /** Venta asociada a este detalle */
    @ManyToOne
    @JoinColumn(name = "fk_venta_id", nullable = false)
    private Venta venta;

    /** Producto vendido */
    @ManyToOne
    @JoinColumn(name = "fk_producto_id", nullable = false)
    private Producto producto;
}
