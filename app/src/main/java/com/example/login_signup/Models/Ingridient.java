package com.example.login_signup.Models;

import androidx.appcompat.app.AppCompatActivity;

public class Ingridient extends AppCompatActivity {
    private int id ;
    private String NOM ;
    private Float PROTEINES ;
    private Float CARBS ;
    private Float FATS ;
    private Float QUANTITE;

    public Ingridient(int id, String NOM, Float PROTEINES, Float CARBS, Float FATS, Float QUANTITE) {
        this.id = id;
        this.NOM = NOM;
        this.PROTEINES = PROTEINES;
        this.CARBS = CARBS;
        this.FATS = FATS;
        this.QUANTITE = QUANTITE;
    }


}
