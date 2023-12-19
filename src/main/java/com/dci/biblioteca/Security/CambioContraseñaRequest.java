package com.dci.biblioteca.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CambioContraseñaRequest {
    private String contraseña;
    private String correo;
}
