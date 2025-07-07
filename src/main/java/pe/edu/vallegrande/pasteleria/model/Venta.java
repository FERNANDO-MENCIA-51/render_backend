package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Entidad que representa la cabecera de una venta.
 * Contiene información general de la venta y su relación con el cliente.
 */
@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    /** ID único de la venta */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ventaID;

    /** Fecha de la venta */
    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    /** Monto total de la venta */
    @Column(name = "total_venta", nullable = false)
    private Double totalVenta;

    /** Estado lógico ('A'=Activo, 'I'=Inactivo) */
    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

    /** Cliente asociado a la venta */
    @ManyToOne
    @JoinColumn(name = "fk_cliente_id", nullable = false)
    private Cliente cliente;
}