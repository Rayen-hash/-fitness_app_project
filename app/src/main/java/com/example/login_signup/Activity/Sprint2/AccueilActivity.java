package com.example.login_signup.Activity.Sprint2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Activity.ProfilActivity;
import com.example.login_signup.Database.Database;
import com.example.login_signup.Models.User;
import com.example.login_signup.R;

import java.util.ArrayList;

public class AccueilActivity extends AppCompatActivity {

    ListView listeAccueil1;
    ListView listeAccueil2;
    Database db;
    ImageButton btnExercice, btnHome, btnProfil, btnNutrition ;
    ArrayList<String> liste1 = new ArrayList<>();
    ArrayList<String> liste2 = new ArrayList<>();
    ProgressBar progressCalories;
    TextView txtnomprenom , txtbesoin ,txtobj , txtcalpro ,txtcalobj;
    int size = 0;

    final Double[] besoinCal = new Double[1];
    public double calculBesoinCalorique(Double poids , Double taille , int age , String nvA) {
        double bmr;
        bmr = 88.36 + (13.4 * poids) + (4.8 * taille *100) - (5.7 * age);
        double facteur = 1.2;
        switch(nvA) {
            case "Niveau Débutant (2–3 jours / semaine)": facteur = 1.375; break;
            case "Niveau Intermédiaire (3–5 jours / semaine)": facteur = 1.55; break;
            case "Niveau Avancé (5–6 jours / semaine)": facteur = 1.725; break;
        }

        return bmr * facteur;
    }
    public float calculCaloriesRepas(float proteines, float glucides, float lipides) {
        return (proteines * 4) + (glucides * 4) + (lipides * 9);
    }
    public Double calculCaloriesObj(String obj,Double besoin){
        switch(obj) {
            case "Perte de poids": return besoin-500.0;
            case "Prise de masse": return besoin+500;
            case "Maintien du poids": return besoin;
        }
        return besoin;
    }
    int nbrerepas=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);

        btnExercice = findViewById(R.id.btnExercice);
        btnHome = findViewById(R.id.btnHome);
        btnProfil = findViewById(R.id.btnProfil);
        btnNutrition = findViewById(R.id.btnNutrition);

        txtnomprenom=findViewById(R.id.txtNomPrenom);
        txtbesoin=findViewById(R.id.txtBesoin);
        txtobj=findViewById(R.id.txtObjectif);
        txtcalpro=findViewById(R.id.txtCaloriesProgress);
        progressCalories=findViewById(R.id.progressCalories);

        txtcalobj=findViewById(R.id.txtCaloriesobj);

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

        Cursor c = db.getUserById(id);
        final Double[] BC = new Double[1];
        if(c.moveToFirst()){
            String nom = c.getString(c.getColumnIndexOrThrow("nom"));
            String prenom=c.getString(c.getColumnIndexOrThrow("prenom"));
            int age=c.getInt(c.getColumnIndexOrThrow("age"));
            Double poids=c.getDouble(c.getColumnIndexOrThrow("poids"));
            Double taille=c.getDouble(c.getColumnIndexOrThrow("taille"));

            String objectif=c.getString(c.getColumnIndexOrThrow("objectif"));

            String nvA=c.getString(c.getColumnIndexOrThrow("niveau_activite"));
            BC[0]=calculBesoinCalorique(poids,taille,age,nvA);
            txtnomprenom.setText(nom+" "+prenom);
            txtobj.setText(txtobj.getText() + objectif);
            txtbesoin.setText(txtbesoin.getText() + String.valueOf(BC[0]));
            txtcalpro.setText(txtcalpro.getText()+String.valueOf(calculCaloriesObj(objectif,BC[0])-calculCaloriesObj(objectif,BC[0])));
            txtcalobj.setText("/"+String.valueOf(calculCaloriesObj(objectif,BC[0])));
            int max = 100;           // valeur maximale
            int caloriesConsommees = 0;
            besoinCal[0] = calculCaloriesObj(objectif,BC[0]);

            int progress = (int)((caloriesConsommees * max) / besoinCal[0]);

            progressCalories.setProgress(progress);
        }


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
            for (int i = nbrerepas; i < ingridientActivity.historiqueIngridientsIds.size(); i++) {
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


                        float calories_repas=calculCaloriesRepas(protcalcule,carbcalcule,fatcalcule);
                        Double calpro = Double.parseDouble(txtcalpro.getText().toString());
                        calpro=calpro+calories_repas;
                        txtcalpro.setText(String.valueOf(calpro));
                        int max = 100;           // valeur maximale
                        int progress = (int)((calories_repas * max) / besoinCal[0]);
                        progressCalories.setProgress(progress);


                }
                if (cursor != null) cursor.close();
                nbrerepas++;
            }
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, liste2);
        listeAccueil2.setAdapter(adapter2);
    }
   @Override
    protected void onResume() {
        super.onResume();
        liste1.clear();
        liste2.clear();
        loadlist();


    }
    public void loadlist() {
        db = new Database(this);
        listeAccueil1 = findViewById(R.id.listeAccueil1);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, liste1);
        listeAccueil1.setAdapter(adapter);

        ArrayList<String> liste2 = new ArrayList<>();

        if (ingridientActivity.historiqueIngridientsIds.isEmpty()) {
            Toast.makeText(this, "Aucun ingridient dans l'historique", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = nbrerepas ; i < ingridientActivity.historiqueIngridientsIds.size(); i++) {
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
                    if(size<liste2.size()){
                        float calories_repas=calculCaloriesRepas(protcalcule,carbcalcule,fatcalcule);
                        Double calpro = Double.parseDouble(txtcalpro.getText().toString());
                        calpro=calpro+calories_repas;
                        txtcalpro.setText(String.valueOf(calpro));
                        int max = 100;           // valeur maximale
                        int progress = (int)((calories_repas * max) / besoinCal[0]);
                        progressCalories.setProgress(progress);
                    }
                    nbrerepas++;
                }
                if (cursor != null) cursor.close();
            }
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, liste2);
        listeAccueil2.setAdapter(adapter2);


    }

}
