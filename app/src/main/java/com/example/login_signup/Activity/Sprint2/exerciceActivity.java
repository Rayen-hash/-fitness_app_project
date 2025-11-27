package com.example.login_signup.Activity.Sprint2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Database.Database;
import com.example.login_signup.R;

import java.util.ArrayList;

public class exerciceActivity extends AppCompatActivity {
    Button btnretour;
    ListView listeExercice;
    Database db;
    ArrayList<Integer> exerciceIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exercice_page);
        db = new Database(this);
        btnretour = findViewById(R.id.btnretour);
        listeExercice = findViewById(R.id.listeExercice);

        ArrayList<String> exercicelist = new ArrayList<>();
        Cursor cursor = db.getAllExercices();

        // Vérifie s’il y a des données dans la base
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Récupère les colonnes
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String titre = cursor.getString(cursor.getColumnIndexOrThrow("TITRE"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));

                // Ajoute les infos dans les deux listes
                exerciceIds.add(id); // pour savoir quel contact est cliqué
                exercicelist.add("TITRE : " + titre + "\nDESCRIPTION : " + description); // texte à afficher
            } while (cursor.moveToNext()); // Passe au contact suivant
        }
        // Ferme le curseur pour libérer la mémoire
        if (cursor != null) cursor.close();
        // Adapter pour afficher la liste à l’écran
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,                             // Contexte actuel
                android.R.layout.simple_list_item_1, // Modèle d’affichage simple
                exercicelist                        // Données à afficher
        );

        // Relie l’adaptateur à la ListView
        listeExercice.setAdapter(adapter);

        listeExercice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupère l'ID du contact sélectionné
                int exerciceId = exerciceIds.get(position);
                // Prépare le passage vers l’activité d’édition du contact
                Intent intent = new Intent(exerciceActivity.this, AccueilActivity.class);
                // Envoie l’ID du contact à l’autre activité
                intent.putExtra("CONTACT_ID", exerciceId);
                // Ouvre la nouvelle activité
                startActivity(intent);
            }
        });

    btnretour.setOnClickListener(v -> {
        Intent intent = new Intent(exerciceActivity.this, AccueilActivity.class);
        startActivity(intent);
    });

    }
}
