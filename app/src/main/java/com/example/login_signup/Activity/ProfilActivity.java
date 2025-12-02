package com.example.login_signup.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Activity.Sprint2.AccueilActivity;
import com.example.login_signup.Database.Database;
import com.example.login_signup.Models.User;
import com.example.login_signup.R;

public class ProfilActivity extends AppCompatActivity {
    Database db ;
    EditText editnom , editprenom , editpoid , edittaille , editage ;
    Button submit , cancel ;
    Spinner spObj ,spNiv;
    String[] objectifs = {"--objectifs--","Perte de poids", "Prise de masse", "Maintien du poids"};
    String[] niveau = {"--Niveau Activité--","Niveau Débutant (2–3 jours / semaine)", "Niveau Intermédiaire (3–5 jours / semaine)", "Niveau Avancé (5–6 jours / semaine)"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        db = new Database(this);
        int user_id = getIntent().getIntExtra("id" , 0);
        editnom = findViewById(R.id.nom);
        editprenom = findViewById(R.id.prenom);
        editpoid = findViewById(R.id.poid);
        edittaille= findViewById(R.id.taille);
        editage=findViewById(R.id.age);


        spObj = findViewById(R.id.objectif_spinner);
        ArrayAdapter<String> adapterobj = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item,objectifs);
        adapterobj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spObj.setAdapter(adapterobj);
        String[] obj = new String[1];

        spNiv = findViewById(R.id.niveau);
        ArrayAdapter<String> adapterniv = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item,niveau);
        adapterniv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNiv.setAdapter(adapterniv);

        String[] niv = new String[1];


        User user = new User();
        Cursor cursor = db.getUserById(user_id);
        if (cursor!=null & cursor.moveToFirst()) {
            editnom.setText(cursor.getString(cursor.getColumnIndexOrThrow("nom")));
            editprenom.setText(cursor.getString(cursor.getColumnIndexOrThrow("prenom")));
            editpoid.setText(cursor.getString(cursor.getColumnIndexOrThrow("poids")));
            edittaille.setText(cursor.getString(cursor.getColumnIndexOrThrow("taille")));
            editage.setText(cursor.getString(cursor.getColumnIndexOrThrow("age")));
            spObj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    obj[0] =parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spNiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    niv[0] =parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            obj[0] = cursor.getString(cursor.getColumnIndexOrThrow("objectif"));
            spObj.setSelection(adapterobj.getPosition(obj[0]));
            niv[0]= cursor.getString(cursor.getColumnIndexOrThrow("niveau_activite"));
            spNiv.setSelection(adapterniv.getPosition(niv[0]));

        }
        cursor.close();
        submit=findViewById(R.id.submitbtn);
        submit.setOnClickListener(v -> {
            if (editprenom.getText().toString().isEmpty()|| editnom.getText().toString().isEmpty()||edittaille.getText().toString().isEmpty() || editpoid.getText().toString().isEmpty()
                    || editage.getText().toString().isEmpty()||obj[0].equals("--objectifs--") || niv[0].equals("--Niveau Activité--")) {
                Toast.makeText(this,"il faut remplir tous les champs",Toast.LENGTH_LONG).show();
            }
            else{
                String nom = editnom.getText().toString();
                String prenom =editprenom.getText().toString();
                String taille = edittaille.getText().toString();
                String poids = editpoid.getText().toString();
                String age = editage.getText().toString();
                user.set("nom",nom);
                user.set("prenom",prenom);
                user.set("taille",Double.valueOf(taille));
                user.set("poids",Double.valueOf(poids));
                user.set("age",Integer.parseInt(age));
                user.set("objectif",obj[0]);
                user.set("niveau_activite",niv[0]);
                db.updateuser(user_id,user);
                Toast.makeText(this , "les informations  sont modifiés ",Toast.LENGTH_LONG).show();
                /*rechargé la page*/
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        cancel=findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(v -> {
            Toast.makeText(this , "modififcation annulée", Toast.LENGTH_LONG).show();
            /*Intent intent = getIntent();*/
            finish();
        });


    }
}