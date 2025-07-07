package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para registrar o actualizar una venta completa (cabecera y detalles).
 */
@Data
public class VentaRequest {
    private Long clienteId;
    private LocalDate fechaVenta;
    private BigDecimal totalVenta;
    private List<VentaDetalleDTO> detalles;

    /**
     * DTO para el detalle de una venta (producto, cantidad, precio, etc).
     */
    @Data
    public static class VentaDetalleDTO {
        private Long productoId;
        private Double cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

    }
}
