package mx.edu.utez.restaurantes.controller;

import mx.edu.utez.restaurantes.dto.OrdenRequest;
import mx.edu.utez.restaurantes.dto.OrdenResponse;
import mx.edu.utez.restaurantes.model.Orden;
import mx.edu.utez.restaurantes.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    // Crear una nueva orden
    @PostMapping
    public ResponseEntity<Orden> crearOrden(@RequestBody OrdenRequest ordenRequest) {
        // Crear la nueva orden
        Orden orden = ordenService.crearOrden(ordenRequest.getArticulosIds(), ordenRequest.getMesero(), ordenRequest.getMesa());
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    /*
    // Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<List<Orden>> listarOrdenes() {
        return ResponseEntity.ok(ordenService.listarOrdenes());
    }

    // Obtener una orden por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtenerOrden(@PathVariable Long id) {
        return ordenService.obtenerOrdenPorId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

     */

    // Actualizar el estatus de una orden
    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizarEstatus(@PathVariable Long id, @RequestBody String estatus) {
        Orden ordenActualizada = ordenService.actualizarEstatus(id, estatus);
        return (ordenActualizada != null) ? ResponseEntity.ok(ordenActualizada) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<OrdenResponse>> listarOrdenes() {
        List<Orden> ordenes = ordenService.listarOrdenes();
        List<OrdenResponse> respuesta = ordenes.stream()
                .map(this::convertirOrdenAResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponse> obtenerOrden(@PathVariable Long id) {
        return ordenService.obtenerOrdenPorId(id)
                .map(this::convertirOrdenAResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Método para convertir Orden a OrdenResponse
    private OrdenResponse convertirOrdenAResponse(Orden orden) {
        OrdenResponse response = new OrdenResponse();
        response.setId(orden.getId());
        response.setMesero(orden.getMesero());
        response.setMesa(orden.getMesa());
        response.setEstatus(orden.getEstatus());
        response.setPrecioTotal(orden.getPrecioTotal());

        List<OrdenResponse.ArticuloResponse> articulosResponse = new ArrayList<>();
        for (int i = 0; i < orden.getArticulos().size(); i++) {
            OrdenResponse.ArticuloResponse articuloResponse = new OrdenResponse.ArticuloResponse();
            articuloResponse.setId(orden.getArticulos().get(i).getId());
            articuloResponse.setNombre(orden.getArticulos().get(i).getNombre());
            articuloResponse.setCantidad(orden.getCantidades().get(i));
            articuloResponse.setPrecioUnitario(orden.getArticulos().get(i).getPrecio());
            articuloResponse.setPrecioTotal(orden.getArticulos().get(i).getPrecio() * orden.getCantidades().get(i));
            articulosResponse.add(articuloResponse);
        }

        response.setArticulos(articulosResponse);
        return response;
    }
}
