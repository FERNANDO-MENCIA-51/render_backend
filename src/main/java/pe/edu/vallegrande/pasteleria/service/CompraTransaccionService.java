package pe.edu.vallegrande.pasteleria.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;
import pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionRequest;
import pe.edu.vallegrande.pasteleria.repository.CompraDetalleRepository;
import pe.edu.vallegrande.pasteleria.repository.CompraRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraTransaccionService {
    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;

    @Transactional
    public Compra registrarCompraConDetalles(CompraTransaccionRequest request) {
        Compra compraGuardada = compraRepository.save(request.getCompra());
        List<CompraDetalle> detalles = request.getDetalles();
        for (CompraDetalle detalle : detalles) {
            detalle.setFkCompraId(compraGuardada.getCompraID().intValue());
            compraDetalleRepository.save(detalle);
        }
        return compraGuardada;
    }

    // Listar todas las compras con detalles
    public List<pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse> listarComprasConDetalles() {
        List<Compra> compras = compraRepository.findAll();
        List<pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse> result = new java.util.ArrayList<>();
        for (Compra compra : compras) {
            pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse dto = new pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse();
            dto.setCompra(compra);
            dto.setDetalles(compraDetalleRepository.findAll().stream()
                .filter(d -> d.getFkCompraId().equals(compra.getCompraID().intValue()))
                .toList());
            result.add(dto);
        }
        return result;
    }

    // Obtener una compra por ID con detalles
    public pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse obtenerCompraConDetalles(Long compraId) {
        Compra compra = compraRepository.findById(compraId).orElseThrow();
        pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse dto = new pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionResponse();
        dto.setCompra(compra);
        dto.setDetalles(compraDetalleRepository.findAll().stream()
            .filter(d -> d.getFkCompraId().equals(compraId.intValue()))
            .toList());
        return dto;
    }

    // Editar una compra y sus detalles
    @Transactional
    public void editarCompraConDetalles(Long compraId, pe.edu.vallegrande.pasteleria.model.dto.CompraTransaccionRequest request) {
        Compra compra = compraRepository.findById(compraId).orElseThrow();
        Compra nuevaCompra = request.getCompra();
        compra.setFechaCompra(nuevaCompra.getFechaCompra());
        compra.setTotalCompra(nuevaCompra.getTotalCompra());
        compra.setObservaciones(nuevaCompra.getObservaciones());
        compra.setEstado(nuevaCompra.getEstado());
        compra.setFkSupplierId(nuevaCompra.getFkSupplierId());
        compra.setCantidad(nuevaCompra.getCantidad());
        compraRepository.save(compra);
        // Eliminar detalles existentes
        List<CompraDetalle> existentes = compraDetalleRepository.findAll().stream()
            .filter(d -> d.getFkCompraId().equals(compraId.intValue()))
            .toList();
        for (CompraDetalle d : existentes) {
            compraDetalleRepository.deleteById(d.getCompraDetalleID());
        }
        // Insertar nuevos detalles
        for (CompraDetalle detalle : request.getDetalles()) {
            detalle.setFkCompraId(compraId.intValue());
            compraDetalleRepository.save(detalle);
        }
    }

    // Eliminar (estado 'I') una compra y sus detalles
    @Transactional
    public void eliminarCompra(Long compraId) {
        Compra compra = compraRepository.findById(compraId).orElseThrow();
        compra.setEstado("I");
        compraRepository.save(compra);
        List<CompraDetalle> detalles = compraDetalleRepository.findAll().stream()
            .filter(d -> d.getFkCompraId().equals(compraId.intValue()))
            .toList();
        for (CompraDetalle detalle : detalles) {
            detalle.setEstado("I");
            compraDetalleRepository.save(detalle);
        }
    }

    // Restaurar (estado 'A') una compra y sus detalles
    @Transactional
    public void restaurarCompra(Long compraId) {
        Compra compra = compraRepository.findById(compraId).orElseThrow();
        compra.setEstado("A");
        compraRepository.save(compra);
        List<CompraDetalle> detalles = compraDetalleRepository.findAll().stream()
            .filter(d -> d.getFkCompraId().equals(compraId.intValue()))
            .toList();
        for (CompraDetalle detalle : detalles) {
            detalle.setEstado("A");
            compraDetalleRepository.save(detalle);
        }
    }
}

