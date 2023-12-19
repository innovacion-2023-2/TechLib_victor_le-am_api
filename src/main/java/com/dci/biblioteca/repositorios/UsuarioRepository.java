package com.dci.biblioteca.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dci.biblioteca.modelos.Cuenta;

@Repository
public interface UsuarioRepository extends JpaRepository<Cuenta, Integer> {

    Optional<Cuenta> findByNombre(String nombre);

    Optional<Cuenta> findById(Integer id);

    Optional<Cuenta> findOneByCorreo(String correo);

}
