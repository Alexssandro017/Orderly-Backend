package mx.edu.utez.restaurantes.service;

import mx.edu.utez.restaurantes.model.Inventario;
import mx.edu.utez.restaurantes.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> obtenerArticuloPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario guardarArticulo(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public boolean eliminarArticulo(Long id) {
        if (inventarioRepository.existsById(id)) {
            inventarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
    // Nuevo método: obtener artículos por categoría
    public List<Inventario> listarPorCategoria(String categoria) {
        return inventarioRepository.findByCategoriaIgnoreCase(categoria);
    }

    public List<String> listarCategoriasUnicas() {
        return inventarioRepository.findDistinctCategorias();
    }

    public List<Inventario> obtenerArticulosPorIds(List<Long> ids) {
        return inventarioRepository.findAllByIds(ids);
    }

}