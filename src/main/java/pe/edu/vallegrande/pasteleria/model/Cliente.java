package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un cliente del sistema (persona que realiza compras o ventas).
 */
@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteID;

    @Column(nullable = false, length = 50)
    private String nombres;

    @Column(nullable = false, length = 50)
    private String apellidos;

    @Column(name = "tipo_documento", nullable = false, length = 10)
    private String tipoDocumento;

    @Column(name = "nro_documento", nullable = false, unique = true, length = 20)
    private String nroDocumento;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(nullable = false, length = 1)
    private String estado;
}
