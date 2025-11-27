package com.example.login_signup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Database.Database;

import com.example.login_signup.Models.User;
import com.example.login_signup.R;

public class MainActivity extends AppCompatActivity {

    Button commencer;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        db = new Database(this);
        db.adduser(new User("Rayen", "Guesmi", "rayen@example.com", "123456", 70.0, 1.75, 25, "Prise de masse","Niveau Intermédiaire (3–5 jours / semaine)"));
        commencer =findViewById(R.id.btncom);
        commencer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }

}