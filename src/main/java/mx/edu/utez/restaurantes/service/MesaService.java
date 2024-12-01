package mx.edu.utez.restaurantes.service;

import jakarta.persistence.EntityNotFoundException;
import mx.edu.utez.restaurantes.model.Mesa;
import mx.edu.utez.restaurantes.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;



    public Mesa guardarMesa(Mesa mesa) {
        Optional<Mesa> mesaExistente = mesaRepository.findByNumero(mesa.getNumero());
        if (mesaExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una mesa con el número " + mesa.getNumero());
        }
        return mesaRepository.save(mesa);
    }

    public List<Mesa> obtenerTodasLasMesas() {
        return mesaRepository.findAll();
    }

    public Optional<Mesa> obtenerMesaPorId(Long id) {
        return mesaRepository.findById(id);
    }

    public Mesa actualizarMesa(Long id, Mesa mesaActualizada) {
        Optional<Mesa> mesaExistente = mesaRepository.findById(id);
        if (mesaExistente.isPresent()) {
            Mesa mesa = mesaExistente.get();
            mesa.setCapacidad(mesaActualizada.getCapacidad());
            mesa.setDisponible(mesaActualizada.getDisponible());
            mesa.setNumero(mesaActualizada.getNumero());
            return mesaRepository.save(mesa);
        } else {
            throw new IllegalArgumentException("No se encontró la mesa con id " + id);
        }
    }

    public void eliminarMesa(Long id) {
        Optional<Mesa> mesaExistente = mesaRepository.findById(id);
        if (mesaExistente.isPresent()) {
            mesaRepository.delete(mesaExistente.get());
        } else {
            throw new IllegalArgumentException("No se encontró la mesa con id " + id);
        }
    }

}
