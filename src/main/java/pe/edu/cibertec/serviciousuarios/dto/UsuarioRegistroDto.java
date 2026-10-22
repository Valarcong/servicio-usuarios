package pe.edu.cibertec.serviciousuarios.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRegistroDto {

    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private String nombres;
    private String apellidos;
    private String rol;
}