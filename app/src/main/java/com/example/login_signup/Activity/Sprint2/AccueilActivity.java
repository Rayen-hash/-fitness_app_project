package com.example.login_signup.Activity.Sprint2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends AppCompatActivity {

    ListView listeAccueil;
    Database db;
    ImageButton btnExercice, btnHome, btnProfil, btnNutrition , btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);

        btnExercice = findViewById(R.id.btnExercice);
        btnHome = findViewById(R.id.btnHome);
        btnProfil = findViewById(R.id.btnProfil);
        btnNutrition = findViewById(R.id.btnNutrition);
        btnHistory=findViewById(R.id.btnHistory);
        //id d'utilisateur connecté
        int id = getIntent().getIntExtra("id" , 0);
        final int[] idSeance = new int[1];
        /*recherche si il y a une seance deja creeé pour ce user aujourd'hui */
        Cursor cursor = db.getSeanceByuserid(id);
        Boolean existSeancetoday = false;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String date_seance = cursor.getString(cursor.getColumnIndexOrThrow(" date_seance"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    if (date_seance.equals(LocalDate.now().toString())){
                        existSeancetoday=true;
                        idSeance[0] = cursor.getInt(cursor.getColumnIndexOrThrow(" id"));
                    }
                }
            } while (cursor.moveToNext() || existSeancetoday==true);
        }
        if (existSeancetoday==false){
            db.addSeance(id);
            /*on refait le recherche de seance qu'on a ajouter pour recupere son id*/
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String date_seance = cursor.getString(cursor.getColumnIndexOrThrow(" date_seance"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        if (date_seance.equals(LocalDate.now().toString())){
                            idSeance[0] = cursor.getInt(cursor.getColumnIndexOrThrow(" id"));
                        }
                    }
                } while (cursor.moveToNext());
            }
            if (cursor != null) cursor.close();
        }
        btnExercice.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, exerciceActivity.class);
            intent.putExtra("idSeance", idSeance[0]);
            startActivity(intent);
        });
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, AccueilActivity.class);
            startActivity(intent);
        });
        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, ProfilActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        db = new Database(this);
        listeAccueil = findViewById(R.id.listeAccueil);
        ArrayList<String> liste = new ArrayList<>();

        /*methode qui recupere la liste des exercices dans une seance par l'id de seance*/
        List<Integer> historiqueExerciceIds = db.getExercicesinSeance(idSeance[0]);

        /*methode qui recupere la liste des exercices dans une seance par l'id de seance*/
        List<Integer> historiqueDurees=db.getDureExercicesinSeance(idSeance[0]);

        if (historiqueExerciceIds.isEmpty()) {
            Toast.makeText(this, "Aucun exercice dans l'historique", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < historiqueExerciceIds.size(); i++) {
                int exerciceId = historiqueExerciceIds.get(i);
                int duree = historiqueDurees.get(i);

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
