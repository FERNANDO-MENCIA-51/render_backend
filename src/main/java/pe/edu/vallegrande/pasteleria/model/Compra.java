package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "compra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compraID;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "total_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCompra;

    private String observaciones;

    @Column(nullable = false, length = 1)
    private String estado = "A";

    @Column(name = "fk_supplier_id", nullable = false)
    private Integer fkSupplierId;

    @Column(nullable = false)
    private Integer cantidad;
}
