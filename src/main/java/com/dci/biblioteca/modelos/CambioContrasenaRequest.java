package com.dci.biblioteca.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CambioContrasenaRequest {
    private String contrasenaActual;
    private String nuevaContrasena;
}
