package mx.edu.utez.restaurantes.dto;

import mx.edu.utez.restaurantes.model.Inventario;
import mx.edu.utez.restaurantes.model.Mesa;
import mx.edu.utez.restaurantes.model.Orden;
import mx.edu.utez.restaurantes.model.Usuario;
import mx.edu.utez.restaurantes.repository.InventarioRepository;
import mx.edu.utez.restaurantes.repository.MesaRepository;
import mx.edu.utez.restaurantes.repository.OrdenRepository;
import mx.edu.utez.restaurantes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            // Verifica si el usuario administrador ya existe
            if (usuarioRepository.findByEmail("admin@example.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Principal");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña encriptada
                admin.setRol(Usuario.Rol.ADMIN);
                admin.setActivo(true);

                usuarioRepository.save(admin);
                System.out.println("Usuario administrador creado exitosamente.");
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    }

    @Bean
    public CommandLineRunner initProducts() {
        return args -> {
            // Crear productos si no existen
            crearProductoSiNoExiste("Pizza Margarita", 120.00, "Comida");
            crearProductoSiNoExiste("Coca Cola", 25.00, "Bebida");
            crearProductoSiNoExiste("Ensalada César", 85.00, "Comida");
            crearProductoSiNoExiste("Pastel de Chocolate", 50.00, "Postre");
        };
    }

    private void crearProductoSiNoExiste(String nombre, Double precio, String categoria) {
        if (inventarioRepository.findByNombre(nombre).isEmpty()) {
            Inventario producto = new Inventario();
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setCategoria(categoria);
            producto.setActivo(true);

            inventarioRepository.save(producto);
            System.out.println("Producto " + nombre + " creado exitosamente.");
        } else {
            System.out.println("El producto " + nombre + " ya existe.");
        }
    }

    @Bean
    public CommandLineRunner initTables() {
        return args -> {
            // Crear mesas si no existen
            crearMesaSiNoExiste(1, 4, true);
            crearMesaSiNoExiste(2, 2, true);
            crearMesaSiNoExiste(3, 6, true);
            crearMesaSiNoExiste(4, 8, true);
        };
    }

    private void crearMesaSiNoExiste(Integer numero, Integer capacidad, Boolean disponible) {
        if (mesaRepository.findByNumero(numero).isEmpty()) {
            Mesa mesa = new Mesa();
            mesa.setNumero(numero);
            mesa.setCapacidad(capacidad);
            mesa.setDisponible(disponible);

            mesaRepository.save(mesa);
            System.out.println("Mesa " + numero + " creada exitosamente.");
        } else {
            System.out.println("La mesa " + numero + " ya existe.");
        }
    }

    @Bean
    public CommandLineRunner initOrders() {
        return args -> {
            // Crear algunos productos de ejemplo si no existen
            List<Inventario> articulos = crearProductosSiNoExisten();

            // Crear una orden con los productos disponibles
            crearOrdenSiNoExiste("Mesero Juan", "Mesa 1", articulos);
            crearOrdenSiNoExiste("Mesero Ana", "Mesa 2", articulos.subList(0, 2)); // Solo algunos productos
        };
    }

    private List<Inventario> crearProductosSiNoExisten() {
        List<String> nombresProductos = Arrays.asList("Pizza Margarita", "Hamburguesa Clásica", "Ensalada César");
        double[] precios = {120.0, 85.5, 70.0};

        for (int i = 0; i < nombresProductos.size(); i++) {
            String nombre = nombresProductos.get(i);
            Optional<Inventario> existingProduct = inventarioRepository.findByNombre(nombre);
            if (existingProduct.isEmpty()) {
                Inventario inventario = new Inventario();
                inventario.setNombre(nombre);
                inventario.setPrecio(precios[i]);
                inventario.setActivo(true);
                inventario.setCategoria("Comida");

                inventarioRepository.save(inventario);
                System.out.println("Producto " + nombre + " creado exitosamente.");
            }
        }
        return inventarioRepository.findAll();  // Retorna todos los productos
    }

    private void crearOrdenSiNoExiste(String mesero, String mesa, List<Inventario> articulos) {
        if (ordenRepository.findAll().stream().noneMatch(o -> o.getMesa().equals(mesa))) {
            Orden orden = new Orden();
            orden.setMesero(mesero);
            orden.setMesa(mesa);
            orden.setArticulos(articulos);

            // Crear una lista de cantidades igual a la cantidad de artículos
            List<Integer> cantidades = new ArrayList<>(Collections.nCopies(articulos.size(), 1));
            orden.setCantidades(cantidades);

            double precioTotal = 0.0;
            for (int i = 0; i < articulos.size(); i++) {
                precioTotal += articulos.get(i).getPrecio() * cantidades.get(i);
            }
            orden.setPrecioTotal(precioTotal);

            ordenRepository.save(orden);
            System.out.println("Orden para la " + mesa + " creada exitosamente.");
        } else {
            System.out.println("La orden para la " + mesa + " ya existe.");
        }
    }

    private void crearOrdenCompletada(String mesero, String mesa, List<Inventario> articulos) {
        if (ordenRepository.findAll().stream().noneMatch(o -> o.getMesa().equals(mesa))) {
            Orden orden = new Orden();
            orden.setMesero(mesero);
            orden.setMesa(mesa);
            orden.setArticulos(articulos);

            // Cantidad por defecto (1 por cada artículo)
            List<Integer> cantidades = new ArrayList<>(Collections.nCopies(articulos.size(), 1));
            orden.setCantidades(cantidades);

            double precioTotal = 0.0;
            for (int i = 0; i < articulos.size(); i++) {
                precioTotal += articulos.get(i).getPrecio() * cantidades.get(i);
            }
            orden.setPrecioTotal(precioTotal);
            orden.setEstatus("completada");  // Cambiar el estatus a completada

            ordenRepository.save(orden);
            System.out.println("Orden completada para la mesa " + mesa + " creada exitosamente.");
        } else {
            System.out.println("La orden para la mesa " + mesa + " ya existe.");
        }
    }

    // Llamar a este método en la clase DataInitializer
    
    public void run(String... args) throws Exception {
        List<Inventario> todosArticulos = inventarioRepository.findAll();

        // Crear 5 órdenes completadas con diferentes meseros y mesas
        crearOrdenCompletada("Ana", "Mesa 5", todosArticulos.subList(0, 2));
        crearOrdenCompletada("Carlos", "Mesa 6", todosArticulos.subList(2, 4));
        crearOrdenCompletada("Elena", "Mesa 7", todosArticulos.subList(1, 3));
        crearOrdenCompletada("Luis", "Mesa 8", todosArticulos.subList(0, 3));
        crearOrdenCompletada("Marta", "Mesa 9", todosArticulos.subList(1, 4));
    }


}
