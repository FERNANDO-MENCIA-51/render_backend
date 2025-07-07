package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Entidad que representa la cabecera de una compra.
 * Incluye información del proveedor, fecha, total y estado.
 */
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

    @Column(name = "total_compra", nullable = false)
    private Double totalCompra;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "fk_supplier_id", nullable = false)
    private Supplier supplier;
}
