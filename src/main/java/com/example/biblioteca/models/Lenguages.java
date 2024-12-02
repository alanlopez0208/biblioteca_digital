package com.example.biblioteca.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Lenguages {
    ES("Español", "es"),
    ENG("Ingles", "en"),
    HU("Hungaro", "hu"),
    PT("Portuges", "pt"),
    FR("Frances", "fr");

    String idiomaEsp;
    String idiomaIng;

    Lenguages(String idiomaEsp, String idiomaIng) {
        this.idiomaEsp = idiomaEsp;
        this.idiomaIng = idiomaIng;
    }



    static public Lenguages fromString(String lenguaje){
        for (Lenguages value : values()) {
            if (value.idiomaIng.equals(lenguaje)){
                return value;
            }
        }
        return ENG;
    }


    @JsonValue
    public String getIdiom() {
        return idiomaIng;
    }
}
