package com.example.login_signup.Models;

public class Exercice {

    private int id;
    private String titre;
    private String description;
    private Integer CALORIES_HEURE;


    // Constructeur
    public Exercice(String titre, String description, String illustration, Integer CALORIES_HEURE)  {
        this.titre = titre;
        this.description = description;
        this.CALORIES_HEURE = CALORIES_HEURE;
    }

    // Constructeur vide (recommandé pour SQLite / Firebase / etc.)
    public Exercice() {
    }
}