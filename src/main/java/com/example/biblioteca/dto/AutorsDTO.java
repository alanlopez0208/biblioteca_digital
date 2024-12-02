package com.example.biblioteca.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutorsDTO(
        String name,
        @JsonAlias("birth_year") Long birthYear,
        @JsonAlias("death_year") Long deathYear) {

}
