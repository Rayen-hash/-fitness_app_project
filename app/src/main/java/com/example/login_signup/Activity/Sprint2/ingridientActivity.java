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

public class ingridientActivity extends AppCompatActivity {
    ListView listeIngridients;
    Button btnretour;
    Database db;
    ArrayList<Integer> ingridientIds = new ArrayList<>();
    ArrayList<String> ingridientlist = new ArrayList<>();

    public static ArrayList<Integer> historiqueIngridientsIds = new ArrayList<>();
    public static ArrayList<Integer> historiqueQauntite = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nutrition_activity);
        db = new Database(this);
        btnretour = findViewById(R.id.btnretour);

        listeIngridients = findViewById(R.id.listeIngridients);



        Cursor cursor = db.getAllIngridients();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow("NOM"));
                float prot = cursor.getFloat(cursor.getColumnIndexOrThrow("PROTEINES"));
                float carb = cursor.getFloat(cursor.getColumnIndexOrThrow("CARBS"));
                float fat = cursor.getFloat(cursor.getColumnIndexOrThrow("FATS"));

                ingridientIds.add(id);
                ingridientlist.add(nom + "\n protienes/100gr: " + prot + "\n carbs/100gr: " + carb + "\n fats/100gr: "+ fat);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                ingridientlist
        );
        listeIngridients.setAdapter(adapter);
        // Lorsque l'utilisateur clique sur un exercice
        listeIngridients.setOnItemClickListener((parent, view, position, id) -> {
            int exerciceId = ingridientIds.get(position);

            AlertDialog.Builder dialog = new AlertDialog.Builder(ingridientActivity.this);
            dialog.setTitle("Entrer la quantité (en gr)");

            final EditText input = new EditText(ingridientActivity.this);
            input.setHint("quantité en gr");
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialog.setView(input);

            dialog.setPositiveButton("Valider", (d, which) -> {
                String quantitynumb = input.getText().toString().trim();
                if (quantitynumb.isEmpty()) return;

                int quantity = Integer.parseInt(quantitynumb);

                // Ajouter dans la liste statique
                historiqueIngridientsIds.add(exerciceId);
                historiqueQauntite.add(quantity);

                // Aller à l'accueil
                Intent intent = new Intent(ingridientActivity.this, AccueilActivity.class);
                startActivity(intent);
            });

            dialog.setNegativeButton("Annuler", null);
            dialog.show();
        });

        btnretour.setOnClickListener(v -> {
            Intent intent = new Intent(ingridientActivity.this, AccueilActivity.class);
            startActivity(intent);
        });
        }


}
