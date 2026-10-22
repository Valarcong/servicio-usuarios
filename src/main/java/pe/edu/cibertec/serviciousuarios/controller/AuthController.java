package pe.edu.cibertec.serviciousuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.serviciousuarios.dto.Login;
import pe.edu.cibertec.serviciousuarios.dto.UsuarioJwt;
import pe.edu.cibertec.serviciousuarios.model.Usuario;
import pe.edu.cibertec.serviciousuarios.security.JwtService;
import pe.edu.cibertec.serviciousuarios.service.DetalleUsuarioService;
import pe.edu.cibertec.serviciousuarios.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final DetalleUsuarioService detalleUsuarioService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthenticationManager authenticationManager,
            DetalleUsuarioService detalleUsuarioService,
            JwtService jwtService,
            UsuarioService usuarioService
    ) {
        this.authenticationManager = authenticationManager;
        this.detalleUsuarioService = detalleUsuarioService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioJwt> login(
            @RequestBody Login login
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getNombreUsuario(),
                        login.getContrasena()
                )
        );

        UserDetails userDetails =
                detalleUsuarioService.loadUserByUsername(
                        login.getNombreUsuario()
                );

        Usuario usuario =
                usuarioService.obtenerUsuarioPorNombreUsuario(
                        login.getNombreUsuario()
                );

        String token = jwtService.generarToken(
                userDetails,
                usuario.getIdUsuario()
        );

        return ResponseEntity.ok(
                new UsuarioJwt(token)
        );
    }
}