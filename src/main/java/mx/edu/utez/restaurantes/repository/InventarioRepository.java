package mx.edu.utez.restaurantes.repository;

import mx.edu.utez.restaurantes.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByCategoriaIgnoreCase(String categoria);

    @Query("SELECT DISTINCT i.categoria FROM Inventario i")
    List<String> findDistinctCategorias();

    // Recuperar una lista de artículos por una lista de IDs
    @Query("SELECT i FROM Inventario i WHERE i.id IN :ids")
    List<Inventario> findAllByIds(@Param("ids") List<Long> ids);
}
