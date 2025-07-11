package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "compra_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPRADETALLEID")
    private Long compraDetalleID;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, length = 1)
    private String estado = "A";

    @Column(name = "fk_producto_id", nullable = false)
    private Integer fkProductoId;

    @Column(name = "fk_compra_id", nullable = false)
    private Integer fkCompraId;

    @Column(name = "fk_supplier_id", nullable = false)
    private Integer fkSupplierId;

    @Column(nullable = false, length = 11)
    private String ruc;
}
