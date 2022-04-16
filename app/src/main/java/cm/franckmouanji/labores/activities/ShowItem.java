package cm.franckmouanji.labores.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.model.Reservation;
import cm.franckmouanji.labores.systeme.ActionAboutReservation;
import cm.franckmouanji.labores.systeme.Controller;

public class ShowItem extends AppCompatActivity {
    TextView text_view_nom;
    TextView text_view_numero;
    TextView text_view_type;
    TextView text_view_date;
    TextView text_view_heure_deb;
    TextView text_view_heure_fin;


    LinearLayout outils_admin;
    Button check;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        initViews();
        String id = "";
        String source = "";

        Intent intent = getIntent();

        if(intent.hasExtra("idR")){
            id = intent.getStringExtra("idR");
        }

        if(intent.hasExtra("source")){
            source = intent.getStringExtra("source");
        }

        Reservation reservation;
        if(source.equals("home")){
            if(Controller.take_information_of_file_users(ShowItem.this).equals(Controller.ADMIN_ACCOUNT)){
                check.setVisibility(View.VISIBLE);
            }

            reservation = Controller.getNewReservation(id);
        }else{
            if(Controller.take_information_of_file_users(ShowItem.this).equals(Controller.ADMIN_ACCOUNT)){
                delete.setVisibility(View.VISIBLE);
            }

            reservation = Controller.getOldReservation(id);
        }


        assert reservation != null;
        chargeTextView(reservation);

        check.setOnClickListener(view -> {
            ActionAboutReservation.updateReservation(reservation.getId());
            finish();
        });

        delete.setOnClickListener(view -> {
            ActionAboutReservation.deleteReservation(reservation.getId());
            finish();
        });

    }

    @SuppressLint("SetTextI18n")
    private void chargeTextView(Reservation reservation) {
        if(reservation.getGrade().equals("Professeur") || reservation.getGrade().equals("Professor")){
            text_view_nom.setText("Pr "  + reservation.getNomProf());
        }else if(reservation.getGrade().equals("Docteur") || reservation.getGrade().equals("Doctor")){
            text_view_nom.setText("Dr "  + reservation.getNomProf());
        }else{
            text_view_nom.setText("Mr "  + reservation.getNomProf());
        }
        text_view_numero.setText(reservation.getNumeroProf());
        text_view_type.setText(reservation.getTypeReservation());
        text_view_date.setText(reservation.getDateReservation());
        text_view_heure_deb.setText(reservation.getHeureDebut());
        text_view_heure_fin.setText(reservation.getHeureFin());
    }

    private void initViews(){
        text_view_nom = findViewById(R.id.text_view_nom);
        text_view_numero = findViewById(R.id.text_view_numero);
        text_view_type = findViewById(R.id.text_view_type);
        text_view_date = findViewById(R.id.text_view_date);
        text_view_heure_deb = findViewById(R.id.text_view_heure_deb);
        text_view_heure_fin = findViewById(R.id.text_view_heure_fin);
        outils_admin = findViewById(R.id.outils_admin);
        check = findViewById(R.id.check);
        delete = findViewById(R.id.delete);

        String user = Controller.take_information_of_file_users(ShowItem.this);

        if(user.equalsIgnoreCase("fs") || user.equalsIgnoreCase("fsprof")){
            outils_admin.setVisibility(View.GONE);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}