package mx.edu.utez.restaurantes.service;

import mx.edu.utez.restaurantes.dto.OrdenesCompletadasResponse;
import mx.edu.utez.restaurantes.model.Inventario;
import mx.edu.utez.restaurantes.model.Orden;
import mx.edu.utez.restaurantes.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private InventarioService inventarioService;  // Servicio para obtener los inventarios

    // Crear una nueva orden
    public Orden crearOrden(List<Long> articulosIds, String mesero, String mesa) {
        // Obtener los artículos del inventario por sus IDs
        List<Inventario> articulos = inventarioService.obtenerArticulosPorIds(articulosIds);

        // Contar cuántas veces se repite cada artículo
        Map<Long, Long> articuloRepeticiones = articulosIds.stream()
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        // Crear la nueva orden
        Orden nuevaOrden = new Orden();
        nuevaOrden.setMesero(mesero);
        nuevaOrden.setMesa(mesa);
        nuevaOrden.setEstatus("pendiente");

        List<Inventario> articulosFinales = new ArrayList<>();
        List<Integer> cantidadesFinales = new ArrayList<>();

        double precioTotal = 0;

        // Asignar artículos y cantidades
        for (Inventario articulo : articulos) {
            long cantidad = articuloRepeticiones.get(articulo.getId());
            articulosFinales.add(articulo);
            cantidadesFinales.add((int) cantidad);  // Convertir la cantidad a int para la cantidad del artículo
            precioTotal += articulo.getPrecio() * cantidad;  // Calcular el precio total de cada artículo
        }

        nuevaOrden.setArticulos(articulosFinales);
        nuevaOrden.setCantidades(cantidadesFinales);
        nuevaOrden.setPrecioTotal(precioTotal);

        // Guardar la nueva orden
        return ordenRepository.save(nuevaOrden);
    }

    // Obtener todas las órdenes
    public List<Orden> listarOrdenes() {
        return ordenRepository.findAll();
    }

    // Obtener una orden por su ID
    public Optional<Orden> obtenerOrdenPorId(Long id) {
        return ordenRepository.findById(id);
    }

    // Actualizar el estatus de una orden
    public Orden actualizarEstatus(Long id, String estatus) {
        Optional<Orden> orden = ordenRepository.findById(id);
        if (orden.isPresent()) {
            orden.get().setEstatus(estatus);
            return ordenRepository.save(orden.get());
        }
        return null;
    }


    // Método para convertir Orden a OrdenResponse
    private OrdenesCompletadasResponse convertirOrdenAResponse(List<Orden> ordenes) {
        OrdenesCompletadasResponse response = new OrdenesCompletadasResponse();
        List<OrdenesCompletadasResponse.OrdenResponse> ordenesResponse = new ArrayList<>();
        Double sumaTotal = 0.0;

        for (Orden orden : ordenes) {
            OrdenesCompletadasResponse.OrdenResponse ordenResponse = new OrdenesCompletadasResponse.OrdenResponse();
            ordenResponse.setId(orden.getId());
            ordenResponse.setMesero(orden.getMesero());
            ordenResponse.setMesa(orden.getMesa());
            ordenResponse.setEstatus(orden.getEstatus());
            ordenResponse.setPrecioTotal(orden.getPrecioTotal());

            List<OrdenesCompletadasResponse.OrdenResponse.ArticuloResponse> articulosResponse = new ArrayList<>();
            for (int i = 0; i < orden.getArticulos().size(); i++) {
                OrdenesCompletadasResponse.OrdenResponse.ArticuloResponse articuloResponse = new OrdenesCompletadasResponse.OrdenResponse.ArticuloResponse();
                articuloResponse.setId(orden.getArticulos().get(i).getId());
                articuloResponse.setNombre(orden.getArticulos().get(i).getNombre());
                articuloResponse.setCantidad(orden.getCantidades().get(i));
                articuloResponse.setPrecioUnitario(orden.getArticulos().get(i).getPrecio());
                articuloResponse.setPrecioTotal(orden.getArticulos().get(i).getPrecio() * orden.getCantidades().get(i));
                articulosResponse.add(articuloResponse);
            }

            ordenResponse.setArticulos(articulosResponse);
            ordenesResponse.add(ordenResponse);
            sumaTotal += orden.getPrecioTotal(); // Sumar el precio total de la orden
        }

        response.setOrdenes(ordenesResponse);
        response.setSumaTotalOrdenes(sumaTotal); // Establecer la suma total
        return response;
    }

    public OrdenesCompletadasResponse listarOrdenesCompletadasPorMesa(String mesa) {
        List<Orden> ordenes = ordenRepository.findByEstatusAndMesaIgnoreCase("completada", mesa);
        return convertirOrdenAResponse(ordenes);
    }


}
