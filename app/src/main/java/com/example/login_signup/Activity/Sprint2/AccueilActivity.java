package com.example.login_signup.Activity.Sprint2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Database.Database;
import com.example.login_signup.R;

import java.util.ArrayList;

public class AccueilActivity extends AppCompatActivity {

    ListView listeAccueil;
    Database db;
    ImageButton btnExercice, btnHome, btnProfil, btnNutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);

        btnExercice = findViewById(R.id.btnExercice);
        btnHome = findViewById(R.id.btnHome);
        btnProfil = findViewById(R.id.btnProfil);
        btnNutrition = findViewById(R.id.btnNutrition);

        btnExercice.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, exerciceActivity.class);
            startActivity(intent);
        });
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, AccueilActivity.class);
            startActivity(intent);
        });

        db = new Database(this);
        listeAccueil = findViewById(R.id.listeAccueil);

        ArrayList<String> liste = new ArrayList<>();

        if (exerciceActivity.historiqueExerciceIds.isEmpty()) {
            Toast.makeText(this, "Aucun exercice dans l'historique", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < exerciceActivity.historiqueExerciceIds.size(); i++) {
                int exerciceId = exerciceActivity.historiqueExerciceIds.get(i);
                int duree = exerciceActivity.historiqueDurees.get(i);

                Cursor cursor = db.getExercicebyId(exerciceId);

                if (cursor != null && cursor.moveToFirst()) {
                    String titre = cursor.getString(cursor.getColumnIndexOrThrow("TITRE"));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
                    int calHeure = cursor.getInt(cursor.getColumnIndexOrThrow("CALORIES_HEURE"));

                    double caloriesBrulees = calHeure * (duree / 60.0);

                    liste.add(
                            "Exercice : " + titre + "\n" +
                                    desc + "\n" +
                                    "Durée : " + duree + " min\n" +
                                    "Calories brûlées : " + String.format("%.2f", caloriesBrulees) + " kcal\n"
                    );
                }

                if (cursor != null) cursor.close();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, liste);
        listeAccueil.setAdapter(adapter);
    }
}
