package pe.edu.cibertec.serviciousuarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String claveSecreta;

    @Value("${jwt.expiration}")
    private long tiempoExpiracion;

    public String generarToken(
            UserDetails userDetails,
            Integer idUsuario
    ) {

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(authority ->
                        authority.getAuthority()
                                .replace("ROLE_", "")
                )
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("idUsuario", idUsuario)
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + tiempoExpiracion
                        )
                )
                .signWith(obtenerClave())
                .compact();
    }

    public String obtenerNombreUsuario(String token) {
        return obtenerClaim(token, Claims::getSubject);
    }

    public boolean validarToken(
            String token,
            UserDetails userDetails
    ) {

        String nombreUsuario = obtenerNombreUsuario(token);

        return nombreUsuario.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return obtenerExpiracion(token).before(new Date());
    }

    private Date obtenerExpiracion(String token) {
        return obtenerClaim(token, Claims::getExpiration);
    }

    private <T> T obtenerClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims = obtenerTodosLosClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims obtenerTodosLosClaims(String token) {

        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClave() {

        byte[] keyBytes = Decoders.BASE64.decode(claveSecreta);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}