package com.dci.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dci.biblioteca.Security.AuthResponse;
import com.dci.biblioteca.Security.AuthService;
import com.dci.biblioteca.Security.LoginRequest;
import com.dci.biblioteca.Security.RegisterRequest;
import com.dci.biblioteca.jwt.JwtService;
import com.dci.biblioteca.modelos.Cuenta;
import com.dci.biblioteca.repositorios.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);
        String usuario = jwtService.getUsernameFromToken(authResponse.getToken());
        Cuenta cuenta = usuarioRepository.findOneByCorreo(usuario).get();

        System.out.println(cuenta.getRol() + "baybay");
        if (authService.estaAutenticado(cuenta)) {
            return ResponseEntity.ok(authResponse.getToken());
        } else {
            return ResponseEntity.badRequest().body("El usuario no esta autenticado");
        }

    }

    @PostMapping(value = "register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        if (authService.usuarioRegistrado(request)) {
            return ResponseEntity.badRequest().body("El correo se encuentra en uso");
        } else {

            AuthResponse authResponse = authService.register(request);
            return ResponseEntity.ok(authResponse.getToken());
        }

    }

    @RequestMapping(value = "validar", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity validar(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.validar(token));
    }

    @PostMapping(value = "admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
        if (authService.usuarioRegistrado(request)) {
            return ResponseEntity.badRequest().body("El correo se encuentra en uso");
        } else {
            AuthResponse authResponse = authService.registerAdmin(request);
            return ResponseEntity.ok(authResponse.getToken());
        }

    }

    @GetMapping(value = "usuario-recuperar-contraseña")
    public Cuenta enviarUsuario(@RequestParam("token") String token) {

        try {

            String usuario = jwtService.getUsernameFromToken(token);
            Cuenta cuenta = usuarioRepository.findOneByCorreo(usuario).get();

            if (jwtService.isTokenValid(token, cuenta) && !jwtService.isTokenExpired(token)) {
                return cuenta;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }

    }
}
