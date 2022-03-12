package cm.franckmouanji.labores.firebase;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Firestore {
    @SuppressLint("StaticFieldLeak")
    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * Mouanji Franck
     * user firebase firestore
     */

    //get collection
    protected static CollectionReference getCollection(String collectionName){
        return db.collection(collectionName);
    }



    //add data to firebase
    protected static Task<DocumentReference> addData(String collectionName, Object data){
        return getCollection(collectionName).add(data);
    }

    //externe interface to add data
    protected static void setDataFromFirebase(String collectionName, Object data, Context context){
        ProgressDialog progressDialog = ProgressDialog.show(context, null, "un instant");
        progressDialog.show();
        addData(collectionName, data).addOnSuccessListener(documentReference -> {
            progressDialog.dismiss();
            //todo
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            //todo
        });
    }

    //get data from firebase
    protected static Task<QuerySnapshot> getAllData(String collectionName){
        return getCollection(collectionName).get();
    }

    protected static Task<QuerySnapshot> getParticularData(String collectionName, String field, String value){
        return getCollection(collectionName).whereEqualTo(field, value).get();
    }

    protected static Task<QuerySnapshot> getParticularData(String collectionName, String field, boolean value){
        return getCollection(collectionName).whereEqualTo(field, value).get();
    }

    protected static Task<QuerySnapshot> getParticularDataOdrer(String collectionName, String field, String value, String fieldOrder){
        return getCollection(collectionName).orderBy(fieldOrder, Query.Direction.DESCENDING).whereEqualTo(field, value).get();
    }


}
