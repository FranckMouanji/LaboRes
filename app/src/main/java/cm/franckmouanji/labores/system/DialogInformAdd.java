package cm.franckmouanji.labores.system;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.model.Reservation;


public class DialogInformAdd {

    public static void fatalErrorDialog(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.error_connexion);
        dialog.setCancelable(false);

        //variable du layout
        Button validate = dialog.findViewById(R.id.validate);

        validate.setOnClickListener(view -> {
            ((Activity)context).finish();
            dialog.dismiss();
        });


        dialog.show();

    }


    public static void updateErrorDialog(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.update_error);
        dialog.setCancelable(false);

        //variable du layout
        Button validate = dialog.findViewById(R.id.validate_update);

        validate.setOnClickListener(view -> {
            ((Activity)context).finish();
            dialog.dismiss();
        });


        dialog.show();

    }

    public static void addReservation(final Context context){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_reservation);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        String[] list= context.getResources().getStringArray(R.array.types_reservations);
        String[] listGrade= context.getResources().getStringArray(R.array.grade);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_item, list);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_item, listGrade);

        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);

        //initViews
        TextInputLayout grade_spinner = dialog.findViewById(R.id.grade_spinner);
        TextInputLayout type_reservation_spinner = dialog.findViewById(R.id.type_reservation_spinner);
        AutoCompleteTextView grade = dialog.findViewById(R.id.grade);
        AutoCompleteTextView  type_reservation = dialog.findViewById(R.id.type_reservation);


        TextInputLayout nomProf = dialog.findViewById(R.id.nomProf);
        TextInputLayout numProf = dialog.findViewById(R.id.numProf);
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

        annuler.setOnClickListener(view -> dialog.dismiss());

        assert editText != null;
        editText.setOnClickListener(view -> showDatePickerDialog(context, date_reservation.getEditText()));

        assert editText1 != null;
        editText1.setOnClickListener(view -> showTimePickerDialog(context, heure_debut.getEditText()));

        assert editText2 != null;
        editText2.setOnClickListener(view -> showTimePickerDialog(context, heure_fin.getEditText()));

        add_reserve.setOnClickListener(view -> {
            String nom = Objects.requireNonNull(nomProf.getEditText()).getText().toString();
            String numero = Objects.requireNonNull(numProf.getEditText()).getText().toString();
            String gradeProf = grade.getText().toString();
            String reservation = type_reservation.getText().toString();
            String date = Objects.requireNonNull(date_reservation.getEditText()).getText().toString();
            String heure_deb = Objects.requireNonNull(heure_debut.getEditText()).getText().toString();
            String heure_f = Objects.requireNonNull(heure_fin.getEditText()).getText().toString();

            boolean data, data1, data2, data3, data4, data5, data6;

            if(gradeProf.equals("")){
                grade_spinner.setError("Choisir un grade");
                grade_spinner.requestFocus();
                data = false;
            }else {
                grade_spinner.setError("");
                grade_spinner.clearFocus();
                data = true;
            }


            if(nom.equals("")){
                nomProf.setError("precisez votre nom");
                nomProf.requestFocus();
                data1 = false;
            }else{
                nomProf.setError("");
                nomProf.clearFocus();
                data1 = true;
            }



            if(reservation.equals("")){
                type_reservation_spinner.setError("precisez le motif de la reservation");
                type_reservation_spinner.requestFocus();
                data2 = false;
            }else{
                type_reservation_spinner.setError("");
                type_reservation_spinner.clearFocus();
                data2 = true;
            }


            if(!(Controller.verifNumero(numero))){
                numProf.setError("entrez un numero valide");
                numProf.requestFocus();
                data3 = false;
            }else{
                numProf.setError("");
                numProf.clearFocus();
                data3 = true;
            }

            if(date.equals("")){
                date_reservation.setError("choisir la date de reservation");
                data4 = false;
            }else{
                date_reservation.setError("");
                data4 = true;
            }

            if(heure_deb.equals("")){
                heure_debut.setError("choisir l'heure de debut");
                data5 = false;
            }else{
                heure_debut.setError("");
                data5 = true;
            }

            if(heure_f.equals("")){
                heure_fin.setError("precisez l'heure de fin");
                data6 = false;
            }else{
                if(Reservation.compHeure(heure_deb, heure_f)){
                    heure_debut.setError("les heures doivent être differentes");
                    heure_fin.setError("les heures doivent être differentes");
                    data6 = false;
                }else{
                    heure_debut.setError("");
                    heure_fin.setError("");
                    data6 = true;
                }
            }



            if(data && data1 && data2 && data3 && data4 && data5 && data6){
                Date dateCreation = new Date();
                Timestamp timestamp = new Timestamp(dateCreation);
                String id = "res"+ (Controller.listReservation.size() + Controller.listOldReservation.size());
                Reservation reservation1 = new Reservation(id, gradeProf, nom, reservation, date, heure_deb, heure_f, false);
                reservation1.setNumeroProf(numero);
                reservation1.setDateCreation(timestamp);
                if(!(ActionAboutReservation.testReservation(reservation1, Controller.listReservation))){
                    ActionAboutReservation.setDataFromFirebase(reservation1, context, dialog);
                }else{
                    informError("Vous chevauchez une autre reservation veuillez modifier vos horaires",context);
                    dialog.dismiss();
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
                        i1++;
                        editText.setText(i2+"/"+i1+"/"+i);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
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

    public static void informError(String text, Context context){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.inform_layout);
        dialog.show();

        TextView textView = dialog.findViewById(R.id.inform);

        textView.setText(text);


    }

    public static void addStudentDeux(Context context) {
    }
}