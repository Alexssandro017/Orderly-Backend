package mx.edu.utez.restaurantes.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mesa;
    private String mesero;
    private Date fecha;
    private Double total;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id") // Asocia ordenes a una factura
    private List<Orden> ordenes;

}
