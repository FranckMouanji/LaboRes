package cm.franckmouanji.labores.systeme;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cm.franckmouanji.labores.BuildConfig;
import cm.franckmouanji.labores.firebase.Firestore;
import cm.franckmouanji.labores.model.Reservation;

public class Controller extends Firestore {
    public static List<Reservation> listReservation = new ArrayList<>();
    public static List<Reservation> listOldReservation = new ArrayList<>();
    public static boolean first = true;

    private static final String TAG = "Controller";
    private static final String FILE_NAME = "user_Information";
    private static final String COLLECTION_SYSTEM = "System";
    private static final String DOCUMENT_VERSION = "versionSystem";
    private static final String FIELD_VERSION = "version";
    private static final String FIELD_PLAGE_DEBUT = "plageDebut";
    private static final String FIELD_PLAGE_FIN = "plageFin";
    private static final String DOCUMENT_PLAGE = "plage_semain";
    private static Map<String, Object> plageHoraire = new HashMap<>();



    public static void setData(String heureDebut, String heureFin){
        Map<String, Object> plage = new HashMap<>();
        plage.put(FIELD_PLAGE_DEBUT, heureDebut);
        plage.put(FIELD_PLAGE_FIN, heureFin);

        getCollection(COLLECTION_SYSTEM).document(DOCUMENT_PLAGE).set(plage);

    }



    private static boolean id_exist(String id){
        for(int i=0; i<listReservation.size(); i++){
            if(listReservation.get(i).getId().equals(id)){
                return true;
            }
        }
        for(int i=0; i<listOldReservation.size(); i++){
            if(listOldReservation.get(i).getId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public static String getId(){
        int id=0;
        while(id_exist("res"+id)){
            id++;
        }

        return "res"+id;
    }

    public static Reservation getNewReservation(String id){
        for(int i=0; i<listReservation.size(); i++){
            if(listReservation.get(i).getId().equals(id)){
                return listReservation.get(i);
            }
        }

        return null;
    }

    public static Reservation getOldReservation(String id){

        for(int i=0; i<listOldReservation.size(); i++){
            if(listOldReservation.get(i).getId().equals(id)){
                return listOldReservation.get(i);
            }
        }
        return null;
    }


    public static boolean verifNumero(String numero){
        int taille = numero.length();
        if(taille == 9){
            String []tab = new String[taille];
            for(int i=0; i<taille; i++){
                tab[i] = String.valueOf(numero.charAt(i));
            }


            if(tab[0].equals("6")){
                return tab[1].equals("5") || tab[1].equals("8") || tab[1].equals("7") || tab[1].equals("9") || tab[1].equals("6");
            }
        }

        return false;
    }


    public static void veriVersion(Context context){
        getCollection(COLLECTION_SYSTEM).document(DOCUMENT_VERSION).get().addOnSuccessListener(documentSnapshot -> {
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

    public static String take_information_of_file_users(Context context){
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
                stringBuilder.append(line);
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

    public static Calendar getCalendarForm(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parse = null;
        try {
            parse = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(parse);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean haveStoragePermission(Context context) {
        if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission error","You have permission");
            return true;
        } else {

            Log.e("Permission error","You have asked for permission");
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    public static void createPdf(String fileName, List<Reservation> liste, Context context) throws FileNotFoundException{
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String filnameRoot = fileName + ".pdf";
        File file = Controller.createOrGetFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filnameRoot, "Labores");
        file.getParentFile().mkdirs();
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);


        //creation du tableau dans le pdf
        Table table = addDataToPdf(liste);

        document.add(table);
        document.setMargins(10,10, 10, 10);


        document.close();

        Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
        pdfViewIntent.setDataAndType(pdfUri,"application/pdf");
        pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    private static Table addDataToPdf(List<Reservation> liste){
        float[] columnWidth = {200f, 200f, 200f, 200f, 200f, 200f, 200f};
        Table table = new Table(columnWidth);
        List<Reservation> semaine = new ArrayList<>();
        listSemaine(listReservation, semaine);

        table.addCell(new Cell().add(new Paragraph("Lundi")));
        table.addCell(new Cell().add(new Paragraph("Mardi")));
        table.addCell(new Cell().add(new Paragraph("Mercredi")));
        table.addCell(new Cell().add(new Paragraph("Jeudi")));
        table.addCell(new Cell().add(new Paragraph("Vendredi")));
        table.addCell(new Cell().add(new Paragraph("Samedi")));
        table.addCell(new Cell().add(new Paragraph("Dimanche")));


        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));
        table.addCell(new Cell().add(new Paragraph(" ")));

        return table;
    }

    public static File createOrGetFile(File destination, String fileName, String folderName){
        File folder = new File(destination, folderName);
        return new File(folder, fileName);
    }


    public static void getPlage(){
        getCollection(COLLECTION_SYSTEM).document(DOCUMENT_PLAGE).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                plageHoraire.put(FIELD_PLAGE_DEBUT, documentSnapshot.getString(FIELD_PLAGE_DEBUT));
                plageHoraire.put(FIELD_PLAGE_FIN, documentSnapshot.getString(FIELD_PLAGE_FIN));
            }
        });
    }

    private static void listSemaine(List<Reservation> all, List<Reservation> semaine){
        Calendar date1 = Controller.getCalendarForm(FIELD_PLAGE_DEBUT);
        Calendar date2 = Controller.getCalendarForm(FIELD_PLAGE_FIN);
        for(int i=0; i<all.size(); i++){
            Calendar date = Controller.getCalendarForm(all.get(i).getDateReservation());
            if((date.compareTo(date1) > 0) && (date.compareTo(date2) < 0)){
                semaine.add(all.get(i));
            }
        }
    }

}
