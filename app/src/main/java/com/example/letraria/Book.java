package com.example.letraria;

public class Book {
    private String titulo;
    private String autor;
    private String status; // "Lido", "Lendo", "Quero Ler"

    public Book(String titulo, String autor, String status) {
        this.titulo = titulo;
        this.autor = autor;
        this.status = status;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getStatus() { return status; }
}
