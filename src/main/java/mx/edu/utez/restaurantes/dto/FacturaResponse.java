package mx.edu.utez.restaurantes.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FacturaResponse {
    private Long id;
    private String mesa;
    private String mesero;
    private Date fecha;
    private Double total;

    private String correo; // Nuevo campo para correo electrónico (opcional)

    private List<OrdenResponse> ordenes; // Utilizamos OrdenResponse que ya existe
}
