package mx.edu.utez.restaurantes.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrdenResponse {
    private Long id;
    private String mesero;
    private String mesa;
    private String estatus;
    private List<ArticuloResponse> articulos;
    private Double precioTotal;

    @Getter
    @Setter
    public static class ArticuloResponse {
        private Long id;
        private String nombre;
        private Integer cantidad;
        private Double precioUnitario;
        private Double precioTotal;
    }
}
