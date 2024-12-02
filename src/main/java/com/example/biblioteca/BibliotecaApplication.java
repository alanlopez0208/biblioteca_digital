package com.example.biblioteca;

import com.example.biblioteca.repository.AutorRepository;
import com.example.biblioteca.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.biblioteca.principal.Principio;

@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {

	@Autowired
	private BookRepository repository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principio principal = new Principio(repository,autorRepository);
		principal.muestraMenu();
	}

}
