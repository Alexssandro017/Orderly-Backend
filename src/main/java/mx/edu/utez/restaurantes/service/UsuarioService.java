package mx.edu.utez.restaurantes.service;

import mx.edu.utez.restaurantes.dto.UsuarioResponseDTO;
import mx.edu.utez.restaurantes.model.Usuario;
import mx.edu.utez.restaurantes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(contrasenaEncriptada);
        return usuarioRepository.save(usuario);
    }


    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Iniciar sesión
    public UsuarioResponseDTO iniciarSesion(String email, String password) {
        // Buscar el usuario por su correo electrónico
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        // Verificar si el usuario existe
        if (usuario.isEmpty()) {
            return null;  // Usuario no encontrado
        }

        // Verificar la contraseña usando el password encoder
        if (!passwordEncoder.matches(password, usuario.get().getPassword())) {
            return null;  // Credenciales incorrectas
        }

        // Si las credenciales son correctas, devolver un DTO con los datos del usuario
        Usuario u = usuario.get();
        return new UsuarioResponseDTO(
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getRol().toString(),
                u.getActivo()
        );
    }


}
