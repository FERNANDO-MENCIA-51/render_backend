package pe.edu.vallegrande.pasteleria.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.pasteleria.model.Compra;
import pe.edu.vallegrande.pasteleria.model.CompraDetalle;
import pe.edu.vallegrande.pasteleria.model.Producto;
import pe.edu.vallegrande.pasteleria.model.Supplier;
import pe.edu.vallegrande.pasteleria.model.dto.CompraRequest;
import pe.edu.vallegrande.pasteleria.model.dto.CompraDetalleDTO;
import pe.edu.vallegrande.pasteleria.repository.CompraRepository;
import pe.edu.vallegrande.pasteleria.repository.CompraDetalleRepository;
import pe.edu.vallegrande.pasteleria.repository.ProductoRepository;
import pe.edu.vallegrande.pasteleria.repository.SupplierRepository;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CompraTransaccionService {
        private final CompraRepository compraRepository;
        private final CompraDetalleRepository compraDetalleRepository;
        private final ProductoRepository productoRepository;
        private final SupplierRepository supplierRepository;

        @Transactional
        public List<Compra> listarTodasLasCompras() {
                return compraRepository.findAll();
        }

        @Transactional
        public Compra obtenerCompraCompleta(Long id) {
                return compraRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
        }

        @Transactional
        public Compra actualizarCompraCompleta(Long id, CompraRequest request) {
                Compra compra = compraRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
                compra.setFechaCompra(request.getFechaCompra());
                compra.setTotalCompra(request.getTotalCompra());
                compra.setObservaciones(request.getObservaciones());
                compra.setEstado(request.getEstado());
                compra.setCantidad(request.getCantidad());
                compra.setSupplier(supplier);
                compraRepository.save(compra);
                // Elimina detalles previos (lógico)
                List<CompraDetalle> detallesPrevios = compraDetalleRepository.findAll()
                                .stream().filter(d -> d.getCompra().getCompraID().equals(id)).toList();
                for (CompraDetalle d : detallesPrevios) {
                        d.setEstado("I");
                }
                compraDetalleRepository.saveAll(detallesPrevios);
                // Inserta nuevos detalles
                List<CompraDetalle> nuevosDetalles = new ArrayList<>();
                for (pe.edu.vallegrande.pasteleria.model.dto.CompraDetalleDTO detalleDTO : request.getDetalles()) {
                        Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                        CompraDetalle detalle = CompraDetalle.builder()
                                        .cantidad(detalleDTO.getCantidad())
                                        .precioUnitario(detalleDTO.getPrecioUnitario())
                                        .subtotal(detalleDTO.getSubtotal())
                                        .estado("A")
                                        .producto(producto)
                                        .compra(compra)
                                        .supplier(supplier)
                                        .build();
                        nuevosDetalles.add(detalle);
                }
                compraDetalleRepository.saveAll(nuevosDetalles);
                return compra;
        }

        @Transactional
        public void eliminarCompraCompleta(Long id) {
                Compra compra = compraRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
                compra.setEstado("I");
                compraRepository.save(compra);
                List<CompraDetalle> detalles = compraDetalleRepository.findAll()
                                .stream().filter(d -> d.getCompra().getCompraID().equals(id)).toList();
                for (CompraDetalle d : detalles) {
                        d.setEstado("I");
                }
                compraDetalleRepository.saveAll(detalles);
        }

        @Transactional
        public void restaurarCompraCompleta(Long id) {
                Compra compra = compraRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));
                compra.setEstado("A");
                compraRepository.save(compra);
                List<CompraDetalle> detalles = compraDetalleRepository.findAll()
                                .stream().filter(d -> d.getCompra().getCompraID().equals(id)).toList();
                for (CompraDetalle d : detalles) {
                        d.setEstado("A");
                }
                compraDetalleRepository.saveAll(detalles);
        }

        @Transactional
        public Compra registrarCompraTransaccional(CompraRequest request) {
                Supplier supplier = supplierRepository.findById(request.getSupplierId())
                                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
                Compra compra = Compra.builder()
                                .fechaCompra(request.getFechaCompra())
                                .totalCompra(request.getTotalCompra())
                                .observaciones(request.getObservaciones())
                                .estado(request.getEstado())
                                .cantidad(request.getCantidad())
                                .supplier(supplier)
                                .build();
                compra = compraRepository.save(compra);
                for (CompraDetalleDTO det : request.getDetalles()) {
                        Producto producto = productoRepository.findById(det.getProductoId())
                                        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
                        CompraDetalle detalle = CompraDetalle.builder()
                                        .cantidad(det.getCantidad())
                                        .precioUnitario(det.getPrecioUnitario())
                                        .subtotal(det.getSubtotal())
                                        .estado(det.getEstado())
                                        .ruc(det.getRuc())
                                        .producto(producto)
                                        .compra(compra)
                                        .supplier(supplier)
                                        .build();
                        compraDetalleRepository.save(detalle);
                }
                return compra;
        }
}