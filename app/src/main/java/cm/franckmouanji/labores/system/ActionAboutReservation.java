package cm.franckmouanji.labores.system;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.franckmouanji.labores.adapter.ReservationAdapter;
import cm.franckmouanji.labores.firebase.Firestore;
import cm.franckmouanji.labores.model.Reservation;

public class ActionAboutReservation extends Firestore {

    private static final String COLLECTION_RESERVATION = "Reservation";

    public static CollectionReference getReservationCollection(){
        return getCollection(COLLECTION_RESERVATION);
    }


    public static void setDataFromFirebase( Reservation data, Context context, Dialog dialog){
        ProgressDialog progressDialog = ProgressDialog.show(context, null, "un instant");
        progressDialog.show();
        addData(COLLECTION_RESERVATION, data).addOnSuccessListener(documentReference -> {
            progressDialog.dismiss();
            Toast.makeText(context, "Reservation enregistrer", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            dialog.dismiss();
        });
    }




    public static void chargeNewReservationData(final Context context, final ListView list, final TextView error, final List<Reservation> liste){
        final List<Reservation> reservations = new ArrayList<>();
        liste.clear();
        Firestore.getAllData(COLLECTION_RESERVATION).addOnSuccessListener(queryDocumentSnapshots -> {
            Reservation reservation = null;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                reservation =documentSnapshot.toObject(Reservation.class);
                if(!(reservation.isCheck())){
                    reservations.add(reservation);
                    liste.add(reservation);
                }
            }

            if (reservations.size() == 0){
                error.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }else {
                error.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                String []date = new String[reservations.size()];
                String []heure = new String[reservations.size()];
                String []nom = new String[reservations.size()];

                Collections.sort(liste, Reservation.comparatorDate);
                Collections.sort(reservations, Reservation.comparatorDate);

                for (int i=0; i<date.length; i++){
                    date[i] = reservations.get(i).getDateReservation();
                    heure[i] = reservations.get(i).getHeureDebut() + "-" + reservation.getHeureFin();
                    if(reservations.get(i).getGrade().equals("Professeur") || reservations.get(i).getGrade().equals("Professor")){
                        nom[i] = "Pr "+ reservations.get(i).getNomProf();
                    }else if(reservations.get(i).getGrade().equals("Docteur") || reservations.get(i).getGrade().equals("Doctor")){
                        nom[i] = "Dr "+ reservations.get(i).getNomProf();
                    }else{
                        nom[i] = "Mr "+ reservations.get(i).getNomProf();
                    }
                }



                ReservationAdapter adapter = new ReservationAdapter(context,nom,date,heure);

                list.setAdapter(adapter);
            }
        });
    }


    public static void chargeOldReservationData(final Context context, final ListView list, final TextView error, final List<Reservation> liste){
        final List<Reservation> reservations = new ArrayList<>();
        liste.clear();
        Firestore.getAllData(COLLECTION_RESERVATION).addOnSuccessListener(queryDocumentSnapshots -> {
            Reservation reservation = null;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                reservation =documentSnapshot.toObject(Reservation.class);
                if(reservation.isCheck()){
                    reservations.add(reservation);
                    liste.add(reservation);
                }
            }

            if (reservations.size() == 0){
                error.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }else {
                error.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                String []date = new String[reservations.size()];
                String []heure = new String[reservations.size()];
                String []nom = new String[reservations.size()];

                Collections.sort(liste, Reservation.comparatorDate);
                Collections.sort(reservations, Reservation.comparatorDate);

                for (int i=0; i<date.length; i++){
                    date[i] = reservations.get(i).getDateReservation();
                    heure[i] = reservations.get(i).getHeureDebut() + "-" + reservation.getHeureFin();
                    if(reservations.get(i).getGrade().equals("Professeur") || reservations.get(i).getGrade().equals("Professor")){
                        nom[i] = "Pr "+ reservations.get(i).getNomProf();
                    }else if(reservations.get(i).getGrade().equals("Docteur") || reservations.get(i).getGrade().equals("Doctor")){
                        nom[i] = "Dr "+ reservations.get(i).getNomProf();
                    }else{
                        nom[i] = "Mr "+ reservations.get(i).getNomProf();
                    }
                }



                ReservationAdapter adapter = new ReservationAdapter(context,nom,date,heure);
                list.setAdapter(adapter);
            }
        });
    }

    public static boolean testReservation(Reservation reservation, List<Reservation>list){
        for(int i=0; i<list.size(); i++){
            if(reservation.chevauche(list.get(i))){
                return true;
            }
        }
        return false;
    }

    public static void updateReservation(String id_res, int i){
        getParticularData(COLLECTION_RESERVATION, "id", id_res).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                getReservationCollection().document(id).update("check", true);
            }
        });
    }

    public static void deleteReservation(String id_res, int i){
        getParticularData(COLLECTION_RESERVATION, "id", id_res).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                getReservationCollection().document(id).delete();
            }
        });
    }


}
