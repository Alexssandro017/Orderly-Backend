package mx.edu.utez.restaurantes.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdenRequest {
    private List<Long> articulosIds;
    private String mesero;
    private String mesa;
}
