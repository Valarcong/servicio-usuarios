package pe.edu.cibertec.serviciousuarios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.serviciousuarios.dto.Login;
import pe.edu.cibertec.serviciousuarios.dto.UsuarioJwt;
import pe.edu.cibertec.serviciousuarios.security.JwtService;
import pe.edu.cibertec.serviciousuarios.service.DetalleUsuarioService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final DetalleUsuarioService detalleUsuarioService;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            DetalleUsuarioService detalleUsuarioService,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.detalleUsuarioService = detalleUsuarioService;
        this.jwtService = jwtService;
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

        String token = jwtService.generarToken(userDetails);

        return ResponseEntity.ok(
                new UsuarioJwt(token)
        );
    }
}