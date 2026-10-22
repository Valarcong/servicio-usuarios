package pe.edu.cibertec.serviciousuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.serviciousuarios.dto.UsuarioRegistroDto;
import pe.edu.cibertec.serviciousuarios.model.Usuario;
import pe.edu.cibertec.serviciousuarios.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(
            @RequestBody UsuarioRegistroDto dto
    ) {

        Usuario usuario = new Usuario();

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(dto.getContrasena());
        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());

        Usuario usuarioRegistrado =
                usuarioService.registrarUsuario(
                        usuario,
                        dto.getRol()
                );

        return ResponseEntity.ok(usuarioRegistrado);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(
                usuarioService.listarUsuarios()
        );
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(
            @PathVariable Integer idUsuario
    ) {
        return ResponseEntity.ok(
                usuarioService.obtenerUsuarioPorId(idUsuario)
        );
    }




    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Integer idUsuario,
            @RequestBody UsuarioRegistroDto dto
    ) {

        return ResponseEntity.ok(
                usuarioService.actualizarUsuario(
                        idUsuario,
                        dto
                )
        );
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Integer idUsuario
    ) {

        usuarioService.eliminarUsuario(idUsuario);

        return ResponseEntity.noContent().build();
    }




}