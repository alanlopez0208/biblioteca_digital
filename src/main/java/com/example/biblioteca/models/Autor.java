package com.example.biblioteca.models;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.biblioteca.dto.AutorsDTO;
import jakarta.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    private Long birthYear;
    private Long deathYear;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER) //mappedBy significa  que no es la dueña de la relacion y se pone el nombre como lo tenemos en el atributo
    private Set<Book> libros = new HashSet<>();


    public Autor(){

    }

    public Autor(AutorsDTO autorsDTO){
        this.nombre = autorsDTO.name();
        this.birthYear = autorsDTO.birthYear();
        this.deathYear = autorsDTO.deathYear();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Long birthYear) {
        this.birthYear = birthYear;
    }

    public Long getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Long deathYear) {
        this.deathYear = deathYear;
    }

    public Set<Book> getLibros() {
        return libros;
    }

    public void setLibros(Set<Book> libros) {
        this.libros = libros;
    }

    public void addBook(Book book){
        this.getLibros().add(book);
    }

    public String mostrarPantalla(){
        return """
                Autor :
                    -Nombre : %s.
                    -Anio Nacimento: %d
                    -Anio fallecimiento : %d
                    -Libros: %s
                """.formatted(this.getNombre(), this.getBirthYear(),this.getDeathYear(), libros.stream().map(Book::getTitulo).collect(Collectors.joining(". ")));
    }
}
