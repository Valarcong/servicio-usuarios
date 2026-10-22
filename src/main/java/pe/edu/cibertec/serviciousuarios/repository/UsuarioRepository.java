package pe.edu.cibertec.serviciousuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.serviciousuarios.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

}