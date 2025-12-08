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

import com.example.login_signup.Activity.ProfilActivity;
import com.example.login_signup.Database.Database;
import com.example.login_signup.R;

import java.util.ArrayList;

public class AccueilActivity extends AppCompatActivity {

    ListView listeAccueil1;
    ListView listeAccueil2;
    Database db;
    ImageButton btnExercice, btnHome, btnProfil, btnNutrition , btnHistory;
    ArrayList<String> liste = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);

        btnExercice = findViewById(R.id.btnExercice);
        btnHome = findViewById(R.id.btnHome);
        btnProfil = findViewById(R.id.btnProfil);
        btnNutrition = findViewById(R.id.btnNutrition);

        db = new Database(this);
        listeAccueil1 = findViewById(R.id.listeAccueil1);
        listeAccueil2 = findViewById(R.id.listeAccueil2);

        int id = getIntent().getIntExtra("id" , 0);

        btnExercice.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, exerciceActivity.class);
            startActivity(intent);
        });
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, AccueilActivity.class);
            startActivity(intent);
        });

        btnNutrition.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, ingridientActivity.class);
            startActivity(intent);
        });
        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, ProfilActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        ArrayList<String> liste1 = new ArrayList<>();

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

                    liste1.add(
                            "Exercice : " + titre + "\n" +
                                    desc + "\n" +
                                    "Durée : " + duree + " min\n" +
                                    "Calories brûlées : " + String.format("%.2f", caloriesBrulees) + " kcal\n"
                    );
                }
                if (cursor != null) cursor.close();
            }
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, liste1);
        listeAccueil1.setAdapter(adapter1);

        ArrayList<String> liste2 = new ArrayList<>();

        if (ingridientActivity.historiqueIngridientsIds.isEmpty()) {
            Toast.makeText(this, "Aucun ingridient dans l'historique", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < ingridientActivity.historiqueIngridientsIds.size(); i++) {
                int ingridientId = ingridientActivity.historiqueIngridientsIds.get(i);
                int quantite = ingridientActivity.historiqueQauntite.get(i);

                Cursor cursor = db.getIngridientbyId(ingridientId);

                if (cursor != null && cursor.moveToFirst()) {
                    //int id_ing = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow("NOM"));
                    float prot = cursor.getFloat(cursor.getColumnIndexOrThrow("PROTEINES"));
                    float carb = cursor.getFloat(cursor.getColumnIndexOrThrow("CARBS"));
                    float fat = cursor.getFloat(cursor.getColumnIndexOrThrow("FATS"));

                    float protcalcule = prot * (quantite / 100.0f);
                    float carbcalcule = carb * (quantite / 100.0f);
                    float fatcalcule = fat * (quantite / 100.0f);

                    liste2.add(
                             nom + "\n proteines : " +
                                    protcalcule + "\n carbs : " + carbcalcule + "\n fats :  " + fatcalcule
                    );
                }
                if (cursor != null) cursor.close();
            }
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, liste2);
        listeAccueil2.setAdapter(adapter2);
    }
   @Override
    protected void onResume() {
        super.onResume();
        liste.clear();
        loadlist();


    }
    public void loadlist(){
        db = new Database(this);
        listeAccueil1 = findViewById(R.id.listeAccueil1);

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
        listeAccueil1.setAdapter(adapter);
    

}

}
