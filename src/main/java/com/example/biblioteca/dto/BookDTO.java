package com.example.biblioteca.dto;

import java.util.List;
import java.util.Set;

import com.example.biblioteca.models.Lenguages;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown  = true)
public record BookDTO(
        String id,
        String title,
        Set<AutorsDTO> authors,
        List<String> languages,
        @JsonAlias("download_count") Long downloadCount) {
}
