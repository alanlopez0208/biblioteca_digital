package com.example.biblioteca.repository;

import com.example.biblioteca.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a From Autor a WHERE a.deathYear >= :fecha AND a.birthYear >= :fecha")
    List<Autor> finByFecha(Long fecha);
}
