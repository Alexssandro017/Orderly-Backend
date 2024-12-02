package mx.edu.utez.restaurantes.service;

import mx.edu.utez.restaurantes.model.Inventario;
import mx.edu.utez.restaurantes.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Map<String, Object>> listarCategoriasConConteo() {
        List<Object[]> categoriasConConteo = inventarioRepository.findCategoriasWithCount();

        // Crear una lista de Mapas para devolver en un formato adecuado
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Object[] obj : categoriasConConteo) {
            Map<String, Object> categoriaData = new HashMap<>();
            categoriaData.put("categoria", obj[0]);
            categoriaData.put("items", obj[1]);
            resultado.add(categoriaData);
        }

        return resultado;
    }


}