package com.example.login_signup.Models;

import android.os.Build;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public class Seance {
    int id;
    List<Exercice> exercices ;
    List<Integer> dures_exercice;
    int id_user;
    String date_seance ;
    public Seance(int id_user){
        this.id=id;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.date_seance=LocalDate.now().toString();
        }
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


}
