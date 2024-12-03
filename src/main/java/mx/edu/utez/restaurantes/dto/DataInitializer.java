package mx.edu.utez.restaurantes.dto;

import mx.edu.utez.restaurantes.model.Usuario;
import mx.edu.utez.restaurantes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
