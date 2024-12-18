package mx.edu.utez.restaurantes.repository;

import mx.edu.utez.restaurantes.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByEstatusAndMesaIgnoreCase(String completada, String mesa);

    List<Orden> findByMesaAndEstatusIgnoreCase(String mesa, String estatus);
}
