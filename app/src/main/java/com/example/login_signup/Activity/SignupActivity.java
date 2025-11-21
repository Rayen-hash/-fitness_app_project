package com.example.login_signup.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.login_signup.Database.Database;
import com.example.login_signup.Models.User;
import com.example.login_signup.R;

import java.io.Serializable;

public class SignupActivity extends AppCompatActivity {
    Database db;
    EditText editnom,editprenom,editemail,editpass ;
    Button btnnext ;
    TextView connect ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        db = new Database(this);
        connect = findViewById(R.id.loginlink);
        editnom = findViewById(R.id.nom);
        editprenom = findViewById(R.id.prenom);
        editemail = findViewById(R.id.email);
        editpass = findViewById(R.id.password);
        connect.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        btnnext = findViewById(R.id.next);
        User user = new User();
        btnnext.setOnClickListener(v -> {
            if (editnom.getText().toString().isEmpty() || editprenom.getText().toString().isEmpty()
                    || editemail.getText().toString().isEmpty() || editpass.getText().toString().isEmpty()) {
                Toast.makeText(this, "il faut remplir tous les champs", Toast.LENGTH_LONG).show();
            } else {
                Cursor cursor = db.getUserbyEmail(editemail.getText().toString());
                if (cursor != null && cursor.moveToFirst()) {
                    Toast.makeText(this, "l'adresse mail est deja utilisé", Toast.LENGTH_LONG).show();
                } else {
                    String nom = editnom.getText().toString();
                    String prenom = editprenom.getText().toString();
                    String email = editemail.getText().toString();
                    String password = editpass.getText().toString();
                    user.set("nom", nom);
                    user.set("prenom", prenom);
                    user.set("email", email);
                    user.set("password", password);
                    Intent intent = new Intent(SignupActivity.this, SuivantActivity.class);
                    intent.putExtra("user", (Serializable) user);
                    startActivityForResult(intent, 1);

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data != null){
            editnom = findViewById(R.id.nom);
            editprenom = findViewById(R.id.prenom);
            editemail = findViewById(R.id.email);
            editpass = findViewById(R.id.password);
            editnom.setText("");
            editprenom.setText("");
            editemail.setText("");
            editpass.setText("");
        }
    }
}