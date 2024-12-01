package mx.edu.utez.restaurantes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mesero;

    @Column(nullable = false)
    private String mesa;

    @Column(nullable = false)
    private String estatus = "pendiente";  // Estatus por defecto

    @ManyToMany
    @JoinTable(
            name = "orden_articulos",
            joinColumns = @JoinColumn(name = "orden_id"),
            inverseJoinColumns = @JoinColumn(name = "articulo_id")
    )
    private List<Inventario> articulos;

    // Campo para almacenar las cantidades de cada artículo en la orden
    @ElementCollection
    @CollectionTable(name = "orden_cantidades", joinColumns = @JoinColumn(name = "orden_id"))
    @Column(name = "cantidad")
    private List<Integer> cantidades;

    // Precio total de la orden
    @Column(nullable = false)
    private Double precioTotal = 0.0;
}
