package com.example.biblioteca.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConvierteDatos {

    private ObjectMapper objectMapper;

    public ConvierteDatos() {
        objectMapper = new ObjectMapper();
    }

    public <T> T getJson(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public <T> List<T> getJson(String json, String value, TypeReference<List<T>> typeReference) {
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode resultado = jsonNode.get(value);
            return objectMapper.readValue(resultado.toString(), typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
