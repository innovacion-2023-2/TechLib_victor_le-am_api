package com.dci.biblioteca.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dci.biblioteca.modelos.Publicacion;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    List<Publicacion> findAll();

    Publicacion findById(Long id);

    @Query("SELECT p FROM Publicacion p WHERE p.descripcion LIKE %:palabra% OR p.titulo LIKE %:palabra%")
    List<Publicacion> findAllByPalabra(@Param("palabra") String palabra);

}
