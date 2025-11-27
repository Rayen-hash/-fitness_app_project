package com.example.login_signup.Models;

public class Exercice {

    private int id;
    private String titre;
    private String description;

    // Constructeur
    public Exercice(String titre, String description, String illustration) {
        this.titre = titre;
        this.description = description;
    }

    // Constructeur vide (recommandé pour SQLite / Firebase / etc.)
    public Exercice() {
    }
}