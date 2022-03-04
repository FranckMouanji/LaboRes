package cm.franckmouanji.labores.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.system.Controller;
import cm.franckmouanji.labores.system.DialogInformAdd;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout log_email;
    TextInputLayout log_password;
    Button log_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        log_send.setOnClickListener(view -> {
            boolean goodDate;
            String email = Objects.requireNonNull(log_email.getEditText()).getText().toString();
            String password = Objects.requireNonNull(log_password.getEditText()).getText().toString();
            if(email.equals("")){
                log_email.setError("entrez votre mail");
                log_email.requestFocus();
                goodDate = false;

            }else{
                log_email.setError("");
                log_email.clearFocus();
                goodDate = true;
            }

            if(password.equals("")){
                log_password.setError("entrez votre mot de passe");
                log_password.requestFocus();
                goodDate = false;
            }else{
                log_password.setError("");
                log_password.clearFocus();
                goodDate = true;
            }

            if(goodDate){
                if((email.equalsIgnoreCase("fs") && password.equals("237")) || (email.equals("admin") && password.equals("237"))){
                    Controller.create_file(email, LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    log_email.setError("vous avez entrer le mauvais email");
                    log_email.requestFocus();
                    log_password.setError("mot de passe incorrect");
                    log_password.requestFocus();
                }
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        if(intent.hasExtra("connexion")){
            String message = intent.getStringExtra("connexion");
            if(message.equals("false")){
                DialogInformAdd.connexionDialog(LoginActivity.this);
            }
        }
        log_email = findViewById(R.id.log_email);
        log_password = findViewById(R.id.log_password);
        log_send = findViewById(R.id.log_send);

    }
}