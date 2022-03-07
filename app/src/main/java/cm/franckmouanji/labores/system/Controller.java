package cm.franckmouanji.labores.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cm.franckmouanji.labores.BuildConfig;
import cm.franckmouanji.labores.firebase.Firestore;
import cm.franckmouanji.labores.model.Reservation;

public class Controller extends Firestore {
    public static List<Reservation> listReservation = new ArrayList<>();
    public static List<Reservation> listOldReservation = new ArrayList<>();

    private static final String TAG = "Controller";
    private static final String FILE_NAME = "user_Information";
    private static final String COLLECTION_VERSION = "System";
    private static final String DOCUMENT_VERSION = "versionSystem";
    private static final String FIELD_VERSION = "version";


    public static boolean verifNumero(String numero){
        int taille = numero.length();
        if(taille == 9){
            String []tab = new String[taille];
            for(int i=0; i<taille; i++){
                tab[i] = String.valueOf(numero.charAt(i));
            }

//            for(int i=0; i<taille; i++){
//                Log.i("numero", tab[i]);
//            }

            if(tab[0].equals("6")){
                return tab[1].equals("5") || tab[1].equals("8") || tab[1].equals("7") || tab[1].equals("9") || tab[1].equals("6");
            }
        }

        return false;
    }


    public static void veriVersion(Context context){
        getCollection(COLLECTION_VERSION).document(DOCUMENT_VERSION).get().addOnSuccessListener(documentSnapshot -> {
            String local_version = documentSnapshot.getString(FIELD_VERSION);

            assert local_version != null;
            if(!(local_version.equals(BuildConfig.VERSION_NAME))){
                DialogInformAdd.updateErrorDialog(context);
            }
        });
    }


    private static boolean verif_file_exist(Context context){
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.isFile();
    }

    public static boolean file_not_empty(Context context){
        if (verif_file_exist(context)){
            String content_of_file = Controller.take_information_of_file_users(context);
            return content_of_file != null && !content_of_file.trim().equals("");
        }else {
            return false;
        }
    }



    //create a file
    public static void create_file(String statut, Context context){
        if (statut != null){

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(statut.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            String contenu_ecrit = " ";
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fileOutputStream.write(contenu_ecrit.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //read information of file

    private static String take_information_of_file_users(Context context){
        String information_user_take;

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(FILE_NAME);
            Log.d(TAG, "take_information_of_file_users: fis : " + fis);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "take_information_of_file_users: Error when open file user info", e);
        }
        assert fis != null;
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }

            reader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
            Log.e(TAG, "take_information_of_file_users: ", e);
        } finally {
            information_user_take = stringBuilder.toString();
        }

        return information_user_take;
    }

}
