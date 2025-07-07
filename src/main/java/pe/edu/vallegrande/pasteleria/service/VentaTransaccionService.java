package pe.edu.vallegrande.pasteleria.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Venta;
import pe.edu.vallegrande.pasteleria.model.VentaDetalle;
import pe.edu.vallegrande.pasteleria.model.Producto;
import pe.edu.vallegrande.pasteleria.model.Cliente;
import pe.edu.vallegrande.pasteleria.model.dto.VentaRequest;
import pe.edu.vallegrande.pasteleria.repository.VentaRepository;
import pe.edu.vallegrande.pasteleria.repository.VentaDetalleRepository;
import pe.edu.vallegrande.pasteleria.repository.ProductoRepository;
import pe.edu.vallegrande.pasteleria.repository.ClienteRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaTransaccionService {
    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public List<Venta> listarTodasLasVentas() {
        // Devuelve todas las ventas (cabecera), los detalles se pueden obtener por relación JPA
        return ventaRepository.findAll();
    }

    @Transactional
    public Venta obtenerVentaCompleta(Long id) {
        // Busca la venta y carga detalles (lazy, pero JPA los trae si se accede)
        return ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
    }

    @Transactional
    public Venta actualizarVentaCompleta(Long id, VentaRequest request) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        venta.setCliente(cliente);
        venta.setFechaVenta(request.getFechaVenta());
        venta.setTotalVenta(request.getTotalVenta().doubleValue());
        // Actualiza cabecera
        ventaRepository.save(venta);
        // Elimina detalles previos (lógico)
        List<VentaDetalle> detallesPrevios = ventaDetalleRepository.findAll()
                .stream().filter(d -> d.getVenta().getVentaID().equals(id)).toList();
        for (VentaDetalle d : detallesPrevios) {
            d.setEstado("I");
        }
        ventaDetalleRepository.saveAll(detallesPrevios);
        // Inserta nuevos detalles
        List<VentaDetalle> nuevosDetalles = new ArrayList<>();
        for (VentaRequest.VentaDetalleDTO detalleDTO : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            VentaDetalle detalle = new VentaDetalle();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad().intValue());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario().doubleValue());
            detalle.setSubtotal(detalleDTO.getSubtotal().doubleValue());
            detalle.setEstado("A");
            nuevosDetalles.add(detalle);
        }
        ventaDetalleRepository.saveAll(nuevosDetalles);
        return venta;
    }

    @Transactional
    public void eliminarVentaCompleta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        venta.setEstado("I");
        ventaRepository.save(venta);
        // Eliminar lógico detalles
        List<VentaDetalle> detalles = ventaDetalleRepository.findAll()
                .stream().filter(d -> d.getVenta().getVentaID().equals(id)).toList();
        for (VentaDetalle d : detalles) {
            d.setEstado("I");
        }
        ventaDetalleRepository.saveAll(detalles);
    }

    @Transactional
    public void restaurarVentaCompleta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        venta.setEstado("A");
        ventaRepository.save(venta);
        // Restaurar detalles
        List<VentaDetalle> detalles = ventaDetalleRepository.findAll()
                .stream().filter(d -> d.getVenta().getVentaID().equals(id)).toList();
        for (VentaDetalle d : detalles) {
            d.setEstado("A");
        }
        ventaDetalleRepository.saveAll(detalles);
    }

    @Transactional
    public Venta registrarVentaCompleta(VentaRequest request) {
        // 1. Obtener cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // 2. Crear entidad Venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFechaVenta(request.getFechaVenta());
        venta.setTotalVenta(request.getTotalVenta().doubleValue());

        venta.setEstado("A");
        Venta ventaGuardada = ventaRepository.save(venta);

        // 3. Crear detalles y asociar
        List<VentaDetalle> detalles = new ArrayList<>();
        for (VentaRequest.VentaDetalleDTO detalleDTO : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            VentaDetalle detalle = new VentaDetalle();
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad().intValue());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario().doubleValue());
            detalle.setSubtotal(detalleDTO.getSubtotal().doubleValue());

            detalle.setEstado("A");

            detalles.add(detalle);
        }
        ventaDetalleRepository.saveAll(detalles);
        return ventaGuardada;
    }
}
