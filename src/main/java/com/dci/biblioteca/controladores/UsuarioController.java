package com.dci.biblioteca.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dci.biblioteca.jwt.JwtService;
import com.dci.biblioteca.modelos.Cuenta;
import com.dci.biblioteca.modelos.UsuarioActualizado;
import com.dci.biblioteca.repositorios.UsuarioRepository;
import com.dci.biblioteca.servicios.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/privado")
@RequiredArgsConstructor
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    @Autowired
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/sesion")
    public Cuenta obtenerUsuarioSesion(Authentication authentication) {
        return usuarioService.obtenerUsuarioSesion(authentication);
    }

    @PostMapping(value = "usuario")
    @PreAuthorize("hasRole('USUARIO_AUTENTICADO')")
    public String user() {
        return "Hola, usuario.";
    }

    @PostMapping(value = "admin")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String admin() {
        return "Hola, administrador.";
    }

    // trae un usuario especifico con su id /usuarios/id
    @GetMapping("/{id}")
    public Cuenta obtenerUsuarioPorId(@PathVariable Integer id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @PutMapping("/actualizar-datos")
    public ResponseEntity<String> actualizarUsuario(Authentication authentication,
            @RequestBody UsuarioActualizado usuarioActualizado) {

        try {
            Cuenta usuarioExistente = usuarioService.obtenerUsuarioSesion(authentication);

            // Actualizar los datos del usuario
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellido(usuarioActualizado.getApellido());
            usuarioExistente.setContraseña(passwordEncoder.encode(usuarioActualizado.getContraseña()));

            // Guardar la actualización del usuario
            usuarioService.guardarUsuario(usuarioExistente);

            return ResponseEntity.ok("Usuario actualizado correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<String> eliminarCuenta(Authentication authentication) {
        try {
            Cuenta usuario = usuarioService.obtenerUsuarioSesion(authentication);

            usuarioService.eliminarUsuario(usuario.getId());

            return ResponseEntity.ok("Cuenta eliminada correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la cuenta: " + e.getMessage());
        }
    }

}
