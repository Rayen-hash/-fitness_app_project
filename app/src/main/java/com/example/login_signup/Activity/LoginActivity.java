package com.example.login_signup.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Activity.Sprint2.AccueilActivity;
import com.example.login_signup.Database.Database;
import com.example.login_signup.R;

public class LoginActivity extends AppCompatActivity {
    private EditText editemail , editpass;
    private Button submit , cancel ;
    private TextView linkinscrip ;
    Database db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        db = new Database(this);
        linkinscrip = findViewById(R.id.signupLink);
        linkinscrip.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        editemail = findViewById(R.id.email);
        editpass = findViewById(R.id.password);
        submit = findViewById(R.id.submitbtn);
        cancel = findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(v -> {
            editemail.setText("");
            editpass.setText("");
        });
        submit.setOnClickListener(v -> {
            if(editemail.getText().toString().isEmpty() || editpass.getText().toString().isEmpty()){
                Toast.makeText(this , "il faut remplir les deux champs " , Toast.LENGTH_LONG).show();
            }
            else {

                    Cursor cursor = db.getUserbyEmail(editemail.getText().toString());
                    if (cursor != null && cursor.moveToFirst()) {
                        String pass = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                        if (!(pass.equals(editpass.getText().toString()))) {
                            Toast.makeText(this, "Mot de passe incorrecte", Toast.LENGTH_LONG).show();
                            editpass.setText("");
                        } else {
                                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                                Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                        }

                    } else {
                        editemail.setText("");
                        editpass.setText("");
                        Toast.makeText(this, "ce email ne correspond a aucun Compte", Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
            }

        });


    }
}