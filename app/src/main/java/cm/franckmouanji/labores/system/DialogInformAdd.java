package cm.franckmouanji.labores.system;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.model.Reservation;


public class DialogInformAdd {

    public static void connexionDialog(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.error_connexion);
        dialog.setCancelable(false);

        //variable du layout
        Button validate = dialog.findViewById(R.id.validate);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public static void addReservation(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_reservation);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        /**
         * initiation du spinner
         */
        String[] list= context.getResources().getStringArray(R.array.types_reservations);
        String[] listGrade= context.getResources().getStringArray(R.array.grade);

        ArrayAdapter adapter1 = new ArrayAdapter(context, R.layout.spinner_item, list);
        ArrayAdapter adapter2 = new ArrayAdapter(context, R.layout.spinner_item, listGrade);

        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);

        //initViews
        Spinner  type_reservation = dialog.findViewById(R.id.type_reservation);
        Spinner grade = dialog.findViewById(R.id.grade);
        TextInputLayout nomProf = dialog.findViewById(R.id.nomProf);
        TextInputLayout date_reservation = dialog.findViewById(R.id.date_reservation);
        TextInputLayout heure_debut = dialog.findViewById(R.id.heure_debut);
        TextInputLayout heure_fin = dialog.findViewById(R.id.heure_fin);
        Button add_reserve = dialog.findViewById(R.id.add_reserve);
        Button annuler = dialog.findViewById(R.id.annuler);


        //local variable
        EditText editText = date_reservation.getEditText();
        EditText editText1 = heure_debut.getEditText();
        EditText editText2 = heure_fin.getEditText();

        //add adapter
        type_reservation.setAdapter(adapter1);
        grade.setAdapter(adapter2);


        //Listener

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        assert editText != null;
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(context, date_reservation.getEditText());
            }
        });

        assert editText1 != null;
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(context, heure_debut.getEditText());
            }
        });

        assert editText2 != null;
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(context, heure_fin.getEditText());
            }
        });

        add_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nomProf.getEditText().getText().toString();
                String gradeProf = grade.getSelectedItem().toString();
                String reservation = type_reservation.getSelectedItem().toString();
                String date = date_reservation.getEditText().getText().toString();
                String heure_deb = heure_debut.getEditText().getText().toString();
                String heure_f = heure_fin.getEditText().getText().toString();

                boolean goodData;

                if(nom.equals("")){
                    nomProf.setError("precisez votre nom");
                    nomProf.requestFocus();
                    goodData = false;
                }else{
                    nomProf.setError("");
                    nomProf.clearFocus();
                    goodData = true;
                }

                if(gradeProf.equals("Grade")){
                    Toast.makeText(context, "Choisir un grade", Toast.LENGTH_LONG).show();
                    grade.requestFocus();
                    goodData = false;
                }else if(reservation.equals("Motif de reservation")){
                    Toast.makeText(context, "precisez le motif de la reservation", Toast.LENGTH_LONG).show();
                    goodData = false;
                }else{
                    goodData = true;
                }

                if(date.equals("")){
                    date_reservation.setError("choisir la date de reservation");
                    goodData = false;
                }else{
                    date_reservation.setError("");
                    goodData = true;
                }

                if(heure_deb.equals("")){
                    heure_debut.setError("choisir l'heure de debut");
                    goodData = false;
                }else{
                    heure_debut.setError("");
                    goodData = true;
                }

                if(heure_f.equals("")){
                    heure_fin.setError("precisez l'heure de fin");
                    goodData = false;
                }else{
                    if(Reservation.compHeure(heure_deb, heure_f)){
                        heure_debut.setError("les heures doivent être differentes");
                        heure_fin.setError("les heures doivent être differentes");
                        goodData = false;
                    }else{
                        heure_debut.setError("");
                        heure_fin.setError("");
                        goodData = true;
                    }
                }



                if(goodData){
                    Toast.makeText(context, "Good", Toast.LENGTH_SHORT).show();
                    Reservation reservation1 = new Reservation("res1", gradeProf, nom, reservation, date, heure_deb, heure_f, false);
                }


            }
        });

        dialog.show();

//        Toast.makeText(context, "add student exist", Toast.LENGTH_SHORT).show();

    }

    public static void showDatePickerDialog(Context context, final EditText editText){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                (Activity)context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        editText.setText(i+"/"+i1+"/"+i2);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    public static void showTimePickerDialog(Context context, final EditText editText){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                (Activity) context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        editText.setText(i+":"+i1);
                    }
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context)
        );
        timePickerDialog.show();
    }


    public static void addStudentDeux(Context context) {
    }
}