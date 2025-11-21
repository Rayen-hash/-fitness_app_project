package com.example.login_signup.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.login_signup.Database.Database;
import com.example.login_signup.Models.User;
import com.example.login_signup.R;

import java.io.Serializable;

public class SuivantActivity extends AppCompatActivity {
    Database db;
    EditText editpoid , edittaille , editage ;
    Button submit , cancel ;
    Spinner spObj;
    String[] objectifs = {"--objectifs--","Perte de poids", "Prise de masse", "Maintien du poids"};
    ArrayAdapter<String>  adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suivant);
        db = new Database(this);
        spObj = findViewById(R.id.objectif_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,objectifs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spObj.setAdapter(adapter);
        User user = (User)getIntent().getSerializableExtra("user");
        editpoid=findViewById(R.id.poid);
        edittaille=findViewById(R.id.taille);
        editage=findViewById(R.id.age);
        submit=findViewById(R.id.submitbtn);
        cancel=findViewById(R.id.cancelbtn);
        final String[] obj = new String[1];
        spObj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obj[0] =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cancel.setOnClickListener(v -> {
            editpoid.setText("");
            edittaille.setText("");
            editage.setText("");
            obj[0]="";
            spObj.setSelection(0);
            Intent intent = new Intent();
            setResult(RESULT_OK ,intent);
            finish();
        });
        submit.setOnClickListener(v -> {
            if (edittaille.getText().toString().isEmpty() || editpoid.getText().toString().isEmpty()
                    || editage.getText().toString().isEmpty()||obj[0].isEmpty()) {
                Toast.makeText(this,"il faut remplir tous les champs",Toast.LENGTH_LONG).show();
            }
            else{
                    String taille = edittaille.getText().toString();
                    String poids = editpoid.getText().toString();
                    String age = editage.getText().toString();
                    user.set("taille",taille);
                    user.set("poids",poids);
                    user.set("age",age);
                    user.set("objectif",obj[0]);
                    db.adduser(user);
                    Intent intent = new Intent(SuivantActivity.this , LoginActivity.class);
                    startActivity(intent);

            }
        });





    }
}