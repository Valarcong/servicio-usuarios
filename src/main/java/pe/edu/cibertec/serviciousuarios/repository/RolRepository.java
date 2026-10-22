package pe.edu.cibertec.serviciousuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.serviciousuarios.model.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByNombre(String nombre);

}