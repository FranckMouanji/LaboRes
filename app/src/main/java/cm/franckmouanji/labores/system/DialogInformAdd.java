package cm.franckmouanji.labores.system;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import cm.franckmouanji.labores.R;


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

    public static void addStudent(final Context context){

//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.error_connexion);
//        dialog.setCancelable(false);
//
//        //variable du layout
//        Button validate = dialog.findViewById(R.id.validate);
//
//        validate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((Activity)context).finish();
//                dialog.dismiss();
//            }
//        });
//
//
//        dialog.show();

        Toast.makeText(context, "add student exist", Toast.LENGTH_SHORT).show();

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
    }


    public static void addStudentDeux(Context context) {
    }
}