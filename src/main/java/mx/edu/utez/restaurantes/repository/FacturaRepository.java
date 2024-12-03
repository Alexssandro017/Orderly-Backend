package mx.edu.utez.restaurantes.repository;

import mx.edu.utez.restaurantes.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}