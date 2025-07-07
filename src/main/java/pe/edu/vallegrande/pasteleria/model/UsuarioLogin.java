package pe.edu.vallegrande.pasteleria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios_login")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "rol", nullable = false, length = 30)
    private String rol;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

    @Column(name = "creado_en")
    private LocalDate creadoEn;
}
