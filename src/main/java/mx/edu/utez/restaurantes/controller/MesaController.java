package mx.edu.utez.restaurantes.controller;

import jakarta.persistence.EntityNotFoundException;
import mx.edu.utez.restaurantes.model.Mesa;
import mx.edu.utez.restaurantes.repository.MesaRepository;
import mx.edu.utez.restaurantes.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private MesaRepository mesaRepository;

    @PostMapping
    public ResponseEntity<?> guardarMesa(@RequestBody Mesa mesa) {
        try {
            Mesa nuevaMesa = mesaService.guardarMesa(mesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la mesa");
        }
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> obtenerTodasLasMesas() {
        List<Mesa> mesas = mesaService.obtenerTodasLasMesas();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerMesaPorId(@PathVariable Long id) {
        return mesaService.obtenerMesaPorId(id)
                .map(mesa -> ResponseEntity.ok(mesa))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMesa(@PathVariable Long id, @RequestBody Mesa mesa) {
        try {
            Mesa mesaActualizada = mesaService.actualizarMesa(id, mesa);
            return ResponseEntity.ok(mesaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la mesa");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMesa(@PathVariable Long id) {
        try {
            mesaService.eliminarMesa(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la mesa");
        }
    }


}
