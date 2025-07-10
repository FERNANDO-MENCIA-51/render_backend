package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.Data;

/**
 * DTO para el detalle de una compra (producto, cantidad, precio, etc).
 */
@Data
public class CompraDetalleDTO {
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private String estado;
    private String ruc;
    private Long productoId;
}