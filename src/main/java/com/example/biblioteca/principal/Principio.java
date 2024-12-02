package com.example.biblioteca.principal;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import com.example.biblioteca.dto.AutorsDTO;
import com.example.biblioteca.models.Lenguages;
import com.example.biblioteca.repository.AutorRepository;
import com.example.biblioteca.repository.BookRepository;
import com.example.biblioteca.dto.BookDTO;
import com.example.biblioteca.models.Autor;
import com.example.biblioteca.models.Book;
import com.example.biblioteca.service.ConvierteDatos;
import com.example.biblioteca.service.Http;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

public class Principio {

    private final Scanner scanner;
    private final Http http;
    private ConvierteDatos convierteDatos;

    private BookRepository bookRepository;
    private AutorRepository autorRepository;

    public Principio(BookRepository repository, AutorRepository autorRepository) {
        scanner = new Scanner(System.in);
        http = new Http();
        convierteDatos = new ConvierteDatos();
        this.bookRepository = repository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu() {
        int opcion = 0;
        do {
            System.out.println("""
                        --------------------------------------------------
                       | Ingrese una opción a través de su numero:       |
                       | 1.- Buscar libro por titulo en la API           |
                       | 2.- Listar libros registrados.                  |
                       | 3.- Listar autores regisrados.                  |
                       | 4.- Listas autores vivos en un determinado año. |
                       | 5.- listar por libros por idioma.               |
                       | 6.- Ver el top 10 de los Libros mas descagados. |
                       | 7.- Generar Estadisitcas.                       |
                       | 8.- salir.                                      |
                       ---------------------------------------------------
                    """);
            System.out.println("Ingrese el número a elegir: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("----Ingrese una opcion valida-----\n\n");
            }
            mostrarOpcion(opcion);
        } while (opcion != 8);
    }

    private void mostrarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                buscarLibro();
                break;
            case 2:
                listarLibros();
                break;
            case 3:
                mostrarAutores();
                break;
            case 4:
                mostrarAutoresByFecha();
                break;
            case 5:
                mostrarByLenguaje();
                break;
            case 6:
                mostrartop();
                break;
            case 7:
                mostrarEstadisticas();
                break;
            case 8:
                break;
            default:
                System.out.println("---Numero no encotrado, porfavor intente de nuevo----");
                break;
        }
    }

    @Transactional
    protected void buscarLibro() {
        Optional<BookDTO> bookOptional = obtenerLibroApi();
        if (bookOptional.isEmpty()) {
            System.out.println("----Libro No Encontrado-----\n\n");
            return;
        }
        BookDTO bookDTO = bookOptional.get();
        if (bookRepository.findByTitulo(bookDTO.title()).isPresent()){
            System.out.println("------NO SE PUEDE REGISTRAR EL LIBRO MAS DE UNA VEZ------\n\n");
            return;
        }
        Book book = new Book(bookDTO);
        Set<Autor> autors = new HashSet<>();
        for (AutorsDTO author : bookDTO.authors()) {
           Autor autor = autorRepository.findByNombre(author.name()).orElseGet(() -> {
               Autor nuevoAutor = new Autor(author);
               autorRepository.save(nuevoAutor);
               return nuevoAutor;
           });
           autors.add(autor);
        }
        book.setAutores(autors);
        bookRepository.save(book);
        System.out.println("Libro Añadido:\n"+book.mostrarPantalla());
    }

    private Optional<BookDTO> obtenerLibroApi() {
        System.out.println("\n\n--------BUSCANDO LIBRO------");
        System.out.println("Ingrese el nombre del libro a buscar: ");
        String busqueda = scanner.nextLine();
        String response = http.request("?search=" + busqueda.replaceAll(" ", "%20"));
        List<BookDTO> libros = convierteDatos.getJson(response, "results", new TypeReference<List<BookDTO>>() {});
        return libros.stream().findFirst();
    }


    private void listarLibros(){
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()){
            System.out.println("---No hay libros registrados---");
            return;
        }
        books.forEach(libro-> System.out.println(libro.mostrarPantalla()));
    }


    private void mostrarAutores(){
        List<Autor> autors = autorRepository.findAll();
        if(autors.isEmpty()){
            System.out.println("------No hay Autores registrados----");
            return;
        }
        System.out.println("-----Autores Registrados--------\n");
        autors.forEach(autor -> System.out.println(autor.mostrarPantalla()));
    }

    private void mostrarAutoresByFecha(){
        System.out.println("Ingresa el año vivo de autores que desas buscar");
        try{
            Long fecha = scanner.nextLong();
            scanner.nextLine();
            List<Autor> autors = autorRepository.finByFecha(fecha);
            if (autors.isEmpty()){
                System.out.println("----No existen registros de autores vivios en ese año---");
            }
            autors.forEach(autor -> System.out.println(autor.mostrarPantalla()));
        }catch (NumberFormatException  | InputMismatchException e){
            System.out.println("Elige un numero correctamente");
        }
    }

    private void mostrarByLenguaje(){
        System.out.println("""
                Lenguajes Disponibles en el sistema:
                1.- Español.
                2.- Ingles.
                3.- Hungaro.
                4.- Portuges
                5.- Frances.
                Ingresa el numero correspondiente del lenguaje a buscar:
                """);
        while (true){
            try{
                int opcion = scanner.nextInt();
                scanner.nextLine();
                Lenguages lenguage = Lenguages.values()[opcion-1];
                List<Book> books = bookRepository.findByLenguages(lenguage);
                if (!books.isEmpty()){
                    books.forEach(libro-> System.out.println(libro.mostrarPantalla()));
                }else{
                    System.out.println("---No Hay Libros Registraos con ese Idioma----");
                }
                break;
            }catch (NumberFormatException e){
                System.out.println("-----Ingresa un numero correctamente-----");
                scanner.nextLine();
            }
        }
    }

    private void mostrartop(){
        List<Book> top10 = bookRepository.findTop10ByOrderByDownLoadCountDesc();
        System.out.println("---Los top 10 de libros en la base de datos son: ");
        top10.forEach(libro-> System.out.println(libro.mostrarPantalla()));
    }

    private void mostrarEstadisticas(){
        System.out.println("---Las estadisiticas de libros en la base de datos son:");
        DoubleSummaryStatistics estadisticas = bookRepository.findAll().stream()
                .collect(Collectors.summarizingDouble(Book::getDownLoadCount));

        System.out.println("""
                -Promedio de total de descargas: %.0f
                -Promedio de descargas : %.2f
                -Minimo de descargas: %.0f
                -Maximo de descargas: %.0f  
                """.formatted(estadisticas.getSum(),estadisticas.getAverage(), estadisticas.getMin(), estadisticas.getMax()));
    }

}
