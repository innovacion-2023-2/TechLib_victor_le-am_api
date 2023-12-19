package com.dci.biblioteca.Security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dci.biblioteca.enums.Rol;
import com.dci.biblioteca.jwt.JwtService;
import com.dci.biblioteca.modelos.Cuenta;
import com.dci.biblioteca.repositorios.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Datos que se encuentran en la solicitud HTTP.

    public AuthResponse login(LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña()));
        UserDetails user = usuarioRepository.findOneByCorreo(request.getCorreo()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public boolean estaAutenticado(Cuenta cuenta) {
        return cuenta.getRol().name().equals(Rol.CLIENTE.name());
    }

    public boolean usuarioRegistrado(RegisterRequest request) {
        return usuarioRepository.findOneByCorreo(request.getCorreo()).isPresent();
    }

    public AuthResponse register(RegisterRequest request) {

        Cuenta cuenta = Cuenta.builder()
                .correo(request.correo)
                .contraseña(passwordEncoder.encode(request.contraseña))
                .nombre(request.nombre)
                .apellido(request.apellido)
                .rol(Rol.CLIENTE)
                .build();

        usuarioRepository.save(cuenta);

        return AuthResponse.builder()
                .token(jwtService.getToken(cuenta))
                .build();

    }

    public AuthResponse registerAdmin(RegisterRequest request) {

        Cuenta cuenta = Cuenta.builder()
                .correo(request.correo)
                .contraseña(passwordEncoder.encode(request.contraseña))
                .nombre(request.nombre)
                .apellido(request.apellido)
                .rol(Rol.BIBLIOTECARIO)
                .build();

        usuarioRepository.save(cuenta);

        return AuthResponse.builder()
                .token(jwtService.getToken(cuenta))
                .build();

    }

    public ResponseEntity<String> validar(String token) {

        String usuario = jwtService.getUsernameFromToken(token);
        Cuenta cuenta = usuarioRepository.findOneByCorreo(usuario).get();

        if (jwtService.isTokenValid(token, cuenta)) {
            cuenta.setRol(Rol.CLIENTE);
            usuarioRepository.save(cuenta);
            return ResponseEntity.ok("La cuenta esta autenticada");
        } else {
            return ResponseEntity.ok("Token vencido, vuelva a registrarse");
        }

    }

}
