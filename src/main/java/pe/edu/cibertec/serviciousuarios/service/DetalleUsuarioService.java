package pe.edu.cibertec.serviciousuarios.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.serviciousuarios.model.Usuario;
import pe.edu.cibertec.serviciousuarios.repository.UsuarioRepository;

import java.util.stream.Collectors;

@Service
public class DetalleUsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DetalleUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByNombreUsuario(nombreUsuario)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado: " + nombreUsuario
                        )
                );

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getContrasena())
                .authorities(
                        usuario.getRoles()
                                .stream()
                                .map(rol ->
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + rol.getNombre()
                                        )
                                )
                                .collect(Collectors.toSet())
                )
                .disabled(!usuario.isActivo())
                .build();
    }
}