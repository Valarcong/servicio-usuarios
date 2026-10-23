package pe.edu.cibertec.serviciousuarios.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.serviciousuarios.dto.UsuarioRegistroDto;
import pe.edu.cibertec.serviciousuarios.model.Rol;
import pe.edu.cibertec.serviciousuarios.model.Usuario;
import pe.edu.cibertec.serviciousuarios.repository.RolRepository;
import pe.edu.cibertec.serviciousuarios.repository.UsuarioRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario, String nombreRol) {

        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado: " + nombreRol)
                );

        usuario.setContrasena(
                passwordEncoder.encode(usuario.getContrasena())
        );

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);

        usuario.setRoles(roles);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerUsuarioPorId(Integer idUsuario) {

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado: " + idUsuario
                        )
                );
    }

    public Usuario actualizarUsuario(
            Integer idUsuario,
            UsuarioRegistroDto dto
    ) {

        Usuario usuario = obtenerUsuarioPorId(idUsuario);

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());

        if (dto.getContrasena() != null &&
                !dto.getContrasena().isBlank()) {

            usuario.setContrasena(
                    passwordEncoder.encode(dto.getContrasena())
            );
        }

        if (dto.getRol() != null &&
                !dto.getRol().isBlank()) {

            Rol rol = rolRepository.findByNombre(dto.getRol())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Rol no encontrado: " + dto.getRol()
                            )
                    );

            Set<Rol> roles = new HashSet<>();
            roles.add(rol);

            usuario.setRoles(roles);
        }

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Integer idUsuario) {

        Usuario usuario = obtenerUsuarioPorId(idUsuario);

        usuarioRepository.delete(usuario);
    }
    public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) {

        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado: " + nombreUsuario
                        )
                );
    }

    public Usuario obtenerAlumnoPorId(Integer idUsuario) {

        Usuario usuario = obtenerUsuarioPorId(idUsuario);

        boolean esAlumno = usuario.getRoles()
                .stream()
                .anyMatch(rol ->
                        "ALUMNO".equals(rol.getNombre())
                );

        if (!esAlumno) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario indicado no tiene rol ALUMNO"
            );
        }

        return usuario;
    }

}