package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un proveedor del sistema (empresa/persona que
 * suministra productos).
 */
@Entity
@Table(name = "supplier")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierID;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "ruc", nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", length = 9)
    private String phone;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @Column(name = "estatus", nullable = false, length = 1)
    private String estatus;
}