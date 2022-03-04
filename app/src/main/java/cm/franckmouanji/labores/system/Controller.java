package cm.franckmouanji.labores.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cm.franckmouanji.labores.model.Reservation;

public class Controller {
    public static List<Reservation> listReservation = new ArrayList<>();
    public static List<Reservation> listOldReservation = new ArrayList<>();


    public static boolean verifNumero(String numero){
        int taille = numero.length();
        if(taille == 9){
            int []tab = new int[taille];
            for(int i=0; i<taille; i++){
                tab[i] = (int) numero.charAt(i);
            }

            if(tab[0] == 6){
                if(tab[1] == 5 || tab[1] == 8 || tab[1] == 7 || tab[1] == 9 || tab[1] == 6){
                    return true;
                }
            }
        }

        return false;
    }


    private static final String TAG = "Controller";
    private static final String FILE_NAME = "user_Information";


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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
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
