package com.dci.biblioteca.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dci.biblioteca.jwt.JwtService;
import com.dci.biblioteca.modelos.Cuenta;
import com.dci.biblioteca.repositorios.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public void guardarUsuario(Cuenta cuenta) {
        usuarioRepository.save(cuenta);
    }

    public List<Cuenta> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Cuenta> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // en proceso
    // public Optional<Cuenta> obtenerUsuarioPorTokenVerificacion(String
    // tokenVerificacion) {
    // return usuarioRepository.findByTokenVerificacion(tokenVerificacion);
    // }

    public Optional<Cuenta> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findOneByCorreo(correo);
    }

    public Cuenta obtenerUsuarioSesion(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

            // Obtiene los detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Utiliza el correo electrónico del usuario autenticado para buscar en la base
            // de datos
            Optional<Cuenta> usuarioOptional = usuarioRepository.findOneByCorreo(userDetails.getUsername());

            // Devuelve el usuario si se encuentra en la base de datos
            return usuarioOptional.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        } else {
            throw new RuntimeException("Autenticación no válida");
        }
    }
}
