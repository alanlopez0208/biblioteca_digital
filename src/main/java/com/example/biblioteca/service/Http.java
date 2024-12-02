package com.example.biblioteca.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class Http {
    private final String URL = "https://gutendex.com/books/";

    private final HttpClient client;

    public Http() {
        client = HttpClient.newHttpClient();
    }

    public String request(String url) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL + url)).build();

        HttpResponse<String> response;
        try {
            response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la solicitud http" + response.body());
            }
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Error al enviar la solicitud HTTP", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error al enviar la solicitud HTTP", e);
        }
    }
}
