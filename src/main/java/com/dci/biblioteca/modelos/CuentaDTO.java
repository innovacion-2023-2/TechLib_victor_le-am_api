package com.dci.biblioteca.modelos;

import com.dci.biblioteca.enums.Rol;

import lombok.Data;

@Data
public class CuentaDTO {
    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
    private Rol rol;
}
