package pe.edu.vallegrande.pasteleria.model.dto;

import lombok.Data;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;
import java.util.List;

@Data
public class CompraTransaccionRequest {
    private Compra compra;
    private List<CompraDetalle> detalles;
}
