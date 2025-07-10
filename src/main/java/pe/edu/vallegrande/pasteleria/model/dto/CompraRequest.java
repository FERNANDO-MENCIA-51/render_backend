package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para registrar o actualizar una compra completa (cabecera y detalles).
 */
@Data
public class CompraRequest {
    private LocalDate fechaCompra;
    private Double totalCompra;
    private String observaciones;
    private String estado;
    private Integer cantidad;
    private Long supplierId;
    private List<CompraDetalleDTO> detalles;
}