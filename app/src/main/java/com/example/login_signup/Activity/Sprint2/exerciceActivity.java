package com.example.login_signup.Activity.Sprint2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Database.Database;
import com.example.login_signup.R;

import java.util.ArrayList;

public class exerciceActivity extends AppCompatActivity {
    Button btnretour;
    ListView listeExercice;
    Database db;
    ArrayList<Integer> exerciceIds = new ArrayList<>();

    //Liste statique pour stocker les exercices et durées pendant la session
    public static ArrayList<Integer> historiqueExerciceIds = new ArrayList<>();
    public static ArrayList<Integer> historiqueDurees = new ArrayList<>();

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

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String titre = cursor.getString(cursor.getColumnIndexOrThrow("TITRE"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
                int calories = cursor.getInt(cursor.getColumnIndexOrThrow("CALORIES_HEURE"));

                exerciceIds.add(id);
                exercicelist.add(titre + "\n" + description + "\nCalories/h : " + calories);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                exercicelist
        );
        listeExercice.setAdapter(adapter);

        // Lorsque l'utilisateur clique sur un exercice
        listeExercice.setOnItemClickListener((parent, view, position, id) -> {
            int exerciceId = exerciceIds.get(position);

            AlertDialog.Builder dialog = new AlertDialog.Builder(exerciceActivity.this);
            dialog.setTitle("Entrer la durée (minutes)");

            final EditText input = new EditText(exerciceActivity.this);
            input.setHint("Durée en minutes");
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialog.setView(input);

            dialog.setPositiveButton("Valider", (d, which) -> {
                String minutesText = input.getText().toString().trim();
                if (minutesText.isEmpty()) return;

                int minutes = Integer.parseInt(minutesText);

                // Ajouter dans la liste statique
                historiqueExerciceIds.add(exerciceId);
                historiqueDurees.add(minutes);

                // Aller à l'accueil
                finish();
            });

            dialog.setNegativeButton("Annuler", null);
            dialog.show();
        });

        btnretour.setOnClickListener(v -> {
            finish();
        });
    }
}
