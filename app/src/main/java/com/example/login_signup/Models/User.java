package com.example.login_signup.Models;

import java.io.Serializable;
import java.lang.reflect.Field;

public class User implements Serializable {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Double poids;
    private Double taille;
    private int age;
    private String objectif;
    private String niveau_activite;

    public User() {}

    public User(String nom, String prenom, String email, String password, Double poids, Double taille, int age, String objectif,String niveau_activité) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.poids = poids;
        this.taille = taille;
        this.age = age;
        this.objectif = objectif;
        this.niveau_activite=niveau_activité;
    }

    public Object get(String attribut) {
        try {
            Field field = this.getClass().getDeclaredField(attribut);
            field.setAccessible(true);
            return field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(String attribut, Object newValue) {
        try {
            Field field = this.getClass().getDeclaredField(attribut);
            field.setAccessible(true);
            field.set(this, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*Calcul calories*/
}

