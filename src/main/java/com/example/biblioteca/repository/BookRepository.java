package com.example.biblioteca.repository;

import com.example.biblioteca.models.Book;
import com.example.biblioteca.models.Lenguages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitulo(String titulo);

    List<Book> findByLenguages(Lenguages lenguages);

    List<Book> findTop10ByOrderByDownLoadCountDesc();
}
