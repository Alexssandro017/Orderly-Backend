package mx.edu.utez.restaurantes.service;

import jakarta.mail.MessagingException;
import mx.edu.utez.restaurantes.config.EmailService;
import mx.edu.utez.restaurantes.dto.FacturaResponse;
import mx.edu.utez.restaurantes.dto.OrdenResponse;
import mx.edu.utez.restaurantes.model.Factura;
import mx.edu.utez.restaurantes.model.Orden;
import mx.edu.utez.restaurantes.repository.FacturaRepository;
import mx.edu.utez.restaurantes.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacturaService {


        @Autowired
        private FacturaRepository facturaRepository;

        @Autowired
        private OrdenRepository ordenRepository;

        @Autowired
        private EmailService emailService; // Inyectamos el servicio de correo electrónico

        public FacturaResponse generarFactura(String numeroMesa, String correo) throws MessagingException {
            List<Orden> ordenes = ordenRepository.findByMesaAndEstatusIgnoreCase(String.valueOf(Long.valueOf(numeroMesa)), "completada");

            // Obtener las órdenes completadas para la mesa especificada
            List<Orden> ordenesCompletadas = ordenRepository.findByMesaAndEstatusIgnoreCase(String.valueOf(Long.valueOf(numeroMesa)), "completada");

            // Validar si hay órdenes
            if (ordenesCompletadas.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay órdenes completadas para la mesa especificada.");
            }

            double totalFactura = ordenes.stream().mapToDouble(Orden::getPrecioTotal).sum();

            // Crear y guardar la factura
            Factura factura = new Factura();
            factura.setMesa(numeroMesa);
            factura.setMesero(ordenes.get(0).getMesero()); // Suponiendo que es el mismo mesero
            factura.setFecha(new Date());
            factura.setTotal(totalFactura);
            factura.setOrdenes(ordenes);

            facturaRepository.save(factura);

            // Cambiar el estatus de las órdenes a "pagado"
            ordenes.forEach(orden -> {
                orden.setEstatus("pagado");
                ordenRepository.save(orden);
            });

            // Convertir Factura a FacturaResponse
            FacturaResponse response = new FacturaResponse();
            response.setId(factura.getId());
            response.setMesa(factura.getMesa());
            response.setMesero(factura.getMesero());
            response.setFecha(factura.getFecha());
            response.setTotal(factura.getTotal());
            response.setCorreo(correo);
            response.setOrdenes(ordenes.stream().map(orden -> {
                return convertirOrdenAResponse(orden);
            }).collect(Collectors.toList()));

            // Si se proporcionó un correo, enviamos la factura
            if (correo != null && !correo.isEmpty()) {
                String subject = "Factura Restaurante";
                String htmlContent = "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<h2 style='color: #4CAF50;'>Factura del Restaurante</h2>" +
                        "<p><strong>Estimado cliente,</strong></p>" +
                        "<p>Gracias por su visita. Aquí está su factura:</p>" +
                        "<table style='width: 100%; border-collapse: collapse;'>" +
                        "<tr>" +
                        "<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Mesa</th>" +
                        "<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Mesero</th>" +
                        "<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Total</th>" +
                        "<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Fecha</th>" +
                        "</tr>" +
                        "<tr>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>" + factura.getMesa() + "</td>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>" + factura.getMesero() + "</td>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>$" + factura.getTotal() + "</td>" +
                        "<td style='border: 1px solid #ddd; padding: 8px;'>" + factura.getFecha() + "</td>" +
                        "</tr>" +
                        "</table>" +
                        "<br>" +
                        "<p><strong>Detalles de las órdenes:</strong></p>" +
                        "<ul>";

                for (OrdenResponse ordenResponse : response.getOrdenes()) {
                    htmlContent += "<li>Orden #" + ordenResponse.getId() + " - $ " + ordenResponse.getPrecioTotal() + "</li>";
                }

                htmlContent += "</ul>" +
                        "<p>¡Gracias por elegirnos! Esperamos que haya disfrutado su comida.</p>" +
                        "<footer style='font-size: 12px; color: #888;'>Restaurante XYZ - Todos los derechos reservados</footer>" +
                        "</body>" +
                        "</html>";

                emailService.sendHtmlEmail(correo, subject, htmlContent);
            }

            return response;
        }

        private OrdenResponse convertirOrdenAResponse(Orden orden) {
            OrdenResponse response = new OrdenResponse();
            response.setId(orden.getId());
            response.setMesero(orden.getMesero());
            response.setMesa(orden.getMesa());
            response.setEstatus(orden.getEstatus());
            response.setPrecioTotal(orden.getPrecioTotal());
            return response;
        }

    // Método para obtener todas las facturas
    public List<FacturaResponse> obtenerTodasLasFacturas() {
        List<Factura> facturas = facturaRepository.findAll();  // Obtener todas las facturas
        List<FacturaResponse> response = new ArrayList<>();

        // Convertir Factura a FacturaResponse para enviar solo los datos necesarios
        for (Factura factura : facturas) {
            FacturaResponse facturaResponse = new FacturaResponse();
            facturaResponse.setId(factura.getId());
            facturaResponse.setMesa(factura.getMesa());
            facturaResponse.setMesero(factura.getMesero());
            facturaResponse.setFecha(factura.getFecha());
            facturaResponse.setTotal(factura.getTotal());

            // Agregar las órdenes relacionadas con esta factura
            facturaResponse.setOrdenes(factura.getOrdenes().stream()
                    .map(orden -> convertirOrdenAResponse(orden))
                    .collect(Collectors.toList()));
            response.add(facturaResponse);
        }

        return response;
    }
}