package com.example.biblioteca.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.biblioteca.dto.BookDTO;
import org.springframework.aot.generate.GeneratedTypeReference;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable( // Aqui se utiliza joinTable para indicar que la habra una tabla de union
            name = "book_autors", // Su nombre de tabla
            joinColumns = @JoinColumn(name = "book_id"), // joinColumns es la clave foranea de esta entindad
            inverseJoinColumns = @JoinColumn(name = "autor_id") // aqui se pone la llave foranea a la cual se esta
                                                                // haciedno la union
    )
    private Set<Autor> autores = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private Lenguages lenguages;

    private Long downLoadCount;

    public Book(){

    }

    public Book(BookDTO dto){
        this.titulo = dto.title();
        this.lenguages = Lenguages.fromString(dto.languages().getFirst());
        this.downLoadCount = dto.downloadCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public Lenguages getLenguages() {
        return lenguages;
    }

    public void setLenguages(Lenguages lenguages) {
        this.lenguages = lenguages;
    }

    public Long getDownLoadCount() {
        return downLoadCount;
    }

    public void setDownLoadCount(Long downLoadCount) {
        this.downLoadCount = downLoadCount;
    }

    public void addAutor(Autor autor){
        this.getAutores().add(autor);
    }

    public String mostrarPantalla(){
        return """
                Libro : %s.
                    -legunaje : %s.
                    -Numero de descargas: %d
                    -Autores: %s
                """.formatted(this.getTitulo(), this.getLenguages().idiomaEsp, this.getDownLoadCount(), autores.stream().map(Autor::getNombre).collect(Collectors.joining(", ")));
    }
}
