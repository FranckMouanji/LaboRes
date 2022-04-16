package cm.franckmouanji.labores.systeme;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cm.franckmouanji.labores.activities.HomeActivity;
import cm.franckmouanji.labores.activities.LoginActivity;
import cm.franckmouanji.labores.firebase.Firestore;
import cm.franckmouanji.labores.model.Reservation;
import cm.franckmouanji.labores.model.Utilisateur;

public class ActionAboutUser extends Firestore {

    private static final String COLLECTION_USER = "Users";

    public static CollectionReference getReservationCollection(){
        return getCollection(COLLECTION_USER);
    }

    public static void setDataFromFirebase(Utilisateur data, Context context, Dialog dialog){
        ProgressDialog progressDialog = ProgressDialog.show(context, null, "un instant");
        progressDialog.show();
        addData(COLLECTION_USER, data).addOnSuccessListener(documentReference -> {
            progressDialog.dismiss();
            Controller.nbreUtilisateur++;
            Toast.makeText(context, "utilisateur enregistrer identifiant de "+data.getNom()+" est "+data.getIdentifiant(), Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            dialog.dismiss();
        });
    }

    public static void recupererNombreUtilisateur() {
        Firestore.getAllData(COLLECTION_USER).addOnSuccessListener(queryDocumentSnapshots -> Controller.nbreUtilisateur = queryDocumentSnapshots.size());
    }

    public static void verifUtilisateur(final Context context, String id, String password) {
        if (context != null) {
            Firestore.getAllData(COLLECTION_USER).addOnSuccessListener(queryDocumentSnapshots -> {
                Utilisateur utilisateur, utilisateurTrouver=null;

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    utilisateur =documentSnapshot.toObject(Utilisateur.class);
                    if(utilisateur.getIdentifiant().equals(id)){
                        utilisateurTrouver = utilisateur;
                    }
                }

                if(utilisateurTrouver != null){
                    Controller.create_file(utilisateurTrouver, context);
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else{
                    DialogInformAdd.informError("erreur de mot de passe ou d'identifiant", context);
                }
            });
        }
    }


    public static void updateInfomation(String id_res, String field, String value, Utilisateur utilisateur, Context context){
        getParticularData(COLLECTION_USER, "identifiant", id_res).addOnSuccessListener(queryDocumentSnapshots -> {
            String id = queryDocumentSnapshots.getDocuments().get(0).getId();
            getReservationCollection().document(id).update(field, value);

            Controller.delete_file(context);
            Controller.create_file(utilisateur, context);
        });
    }

    public static void updateInfomation(String id_res, String field1, String value1, String field2, String value2, Utilisateur utilisateur){
        getParticularData(COLLECTION_USER, "identifiant", id_res).addOnSuccessListener(queryDocumentSnapshots -> {
            String id = queryDocumentSnapshots.getDocuments().get(0).getId();
            getReservationCollection().document(id).update(field1, value1);
            getReservationCollection().document(id).update(field2, value2);
        });
    }
}
