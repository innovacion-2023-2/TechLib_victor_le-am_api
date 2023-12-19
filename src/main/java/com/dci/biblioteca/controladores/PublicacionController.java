package com.dci.biblioteca.controladores;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.dci.biblioteca.modelos.Cuenta;
import com.dci.biblioteca.modelos.Publicacion;
import com.dci.biblioteca.repositorios.PublicacionRepository;
import com.dci.biblioteca.servicios.PublicacionService;
import com.dci.biblioteca.servicios.UsuarioService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {
    PublicacionService publicacionService;
    UsuarioService usuarioService;
    PublicacionRepository publicacionRepository;

    public PublicacionController(PublicacionService publicacionService, UsuarioService usuarioService,
            PublicacionRepository publicacionRepository) {
        this.publicacionService = publicacionService;
        this.usuarioService = usuarioService;
        this.publicacionRepository = publicacionRepository;
    }

    @GetMapping("/ver")
    @Transactional
    public List<Publicacion> verPublicaciones() {
        return publicacionService.verPublicacion();
    }

    @GetMapping("/{id}")
    public Publicacion buscarPorId(@RequestParam("id") Integer id, Authentication authentication) {
        return publicacionService.buscarPublicacionPorId(id);
    }

    @DeleteMapping("/eliminar")
    public List<Publicacion> eliminarPublicacion(@RequestParam("id") Integer id, Authentication authentication) {
        publicacionService.eliminarPublicacion(id);
        return publicacionService.verPublicacion();
    }

    @PostMapping(value = "/crear")
    public void crearPublicacion(@RequestBody Publicacion publicacion, Authentication authentication) {

        Cuenta usuario = usuarioService.obtenerUsuarioSesion(authentication);
        publicacion.setCuenta(usuario);
        publicacionService.guardarPublicacion(publicacion);
    }

    @GetMapping("/buscarPorPalabra")
    public List buscarPorPalabra(@RequestParam("palabra") String palabra) {
        return publicacionService.buscarPorPalabra(palabra);
    }

    @PutMapping(value = "/actualizar/{id}")
    public void actualizarPublicacion(
            @PathVariable Integer id,
            @RequestPart("publicacion") Publicacion publicacion) {
        publicacionService.actualizarPublicacion(id, publicacion);
    }

}