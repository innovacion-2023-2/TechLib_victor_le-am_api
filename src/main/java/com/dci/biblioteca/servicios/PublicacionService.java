package com.dci.biblioteca.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dci.biblioteca.modelos.Publicacion;
import com.dci.biblioteca.repositorios.PublicacionRepository;

@Service
public class PublicacionService {

    PublicacionRepository publicacionRepository;

    @Autowired
    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    /*
     * @Transactional
     * public void estadoProducto(Integer id) {
     * Optional<Publicacion> publicacionOptional =
     * publicacionRepository.findById(id);
     * 
     * if (publicacionOptional.isPresent()) {
     * Publicacion publicacion = publicacionOptional.get();
     * publicacion.setEstado(DISPONIBLE);
     * publicacionRepository.save(publicacion);
     * }
     * }
     */
    public void guardarPublicacion(Publicacion pub) {
        publicacionRepository.save(pub);
    }

    public void eliminarPublicacion(Integer id) {
        publicacionRepository.deleteById(id);
    }

    public Publicacion buscarPublicacionPorId(long id) {
        return publicacionRepository.findById(id);
    }

    public void actualizarPublicacion(Integer id, Publicacion nuevaPublicacion) {
        Optional<Publicacion> optionalPublicacion = publicacionRepository.findById(id);

        if (optionalPublicacion.isPresent()) {
            Publicacion publicacionExistente = optionalPublicacion.get();

            if (nuevaPublicacion != null) {
                if (nuevaPublicacion.getDescripcion() != null) {
                    publicacionExistente.setDescripcion(nuevaPublicacion.getDescripcion());
                }
                if (nuevaPublicacion.getTitulo() != null) {
                    publicacionExistente.setTitulo(nuevaPublicacion.getTitulo());
                }
            }
            guardarPublicacion(publicacionExistente);
        }
    }

    public List<Publicacion> buscarPorPalabra(String palabra) {
        return publicacionRepository.findAllByPalabra(palabra);
    }

    public List<Publicacion> verPublicacion() {
        return publicacionRepository.findAll();
    }
}