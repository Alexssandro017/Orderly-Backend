package mx.edu.utez.restaurantes.controller;

import jakarta.mail.MessagingException;
import mx.edu.utez.restaurantes.dto.FacturaResponse;
import mx.edu.utez.restaurantes.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PostMapping("/completadas/{numeroMesa}")
    public ResponseEntity<?> generarFactura(@PathVariable String numeroMesa, @RequestParam(required = false) String correo) {
        try {
            // Llamamos al servicio para generar la factura
            FacturaResponse facturaResponse = facturaService.generarFactura(numeroMesa, correo);
            return ResponseEntity.ok(facturaResponse);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint para obtener todas las facturas
    @GetMapping("/todas")
    public ResponseEntity<List<FacturaResponse>> obtenerTodasLasFacturas() {
        List<FacturaResponse> facturas = facturaService.obtenerTodasLasFacturas();
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }
}