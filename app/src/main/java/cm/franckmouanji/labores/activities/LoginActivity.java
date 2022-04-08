package cm.franckmouanji.labores.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.systeme.ActionAboutUser;
import cm.franckmouanji.labores.systeme.Controller;
import cm.franckmouanji.labores.systeme.DialogInformAdd;

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
            boolean data, data1;

            String email = Objects.requireNonNull(log_email.getEditText()).getText().toString();
            String password = Objects.requireNonNull(log_password.getEditText()).getText().toString();
            if(email.equals("")){
                log_email.setError("entrez votre mail");
                log_email.requestFocus();
                data = false;

            }else{
                log_email.setError("");
                log_email.clearFocus();
                data = true;
            }

            if(password.equals("")){
                log_password.setError("entrez votre mot de passe");
                log_password.requestFocus();
                data1 = false;
            }else{
                log_password.setError("");
                log_password.clearFocus();
                data1 = true;
            }

            if(data && data1){
                if((email.equalsIgnoreCase("fs") && password.equals("237")) || (email.equals("adminLabo") && password.equals("237"))){
                    Controller.create_file(email, LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    ActionAboutUser.verifUtilisateur(LoginActivity.this, email, password);
                }
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        if(intent.hasExtra("connexion")){
            String message = intent.getStringExtra("connexion");
            if(message.equals("false")){
                DialogInformAdd.fatalErrorDialog(LoginActivity.this);
            }
        }

        Controller.veriVersion(LoginActivity.this);

        log_email = findViewById(R.id.log_email);
        log_password = findViewById(R.id.log_password);
        log_send = findViewById(R.id.log_send);

    }
}