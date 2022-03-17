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

    public static void createPdf(String fileName, List<Reservation> liste, Context context) throws FileNotFoundException, ParseException {
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

    private static Table addDataToPdf(List<Reservation> liste) throws ParseException {
        float[] columnWidth = {200f, 200f, 200f, 200f, 200f, 200f, 200f, 200f};
        Table table = new Table(columnWidth);
        List<Reservation> semaine = new ArrayList<>();
        listSemaine(listReservation, semaine);

        table.addCell(new Cell().add(new Paragraph("Horaire")));
        table.addCell(new Cell().add(new Paragraph("Lundi")));
        table.addCell(new Cell().add(new Paragraph("Mardi")));
        table.addCell(new Cell().add(new Paragraph("Mercredi")));
        table.addCell(new Cell().add(new Paragraph("Jeudi")));
        table.addCell(new Cell().add(new Paragraph("Vendredi")));
        table.addCell(new Cell().add(new Paragraph("Samedi")));
        table.addCell(new Cell().add(new Paragraph("Dimanche")));



        String mat[][] = new String[7][8];
        initMatrice(mat, 7, 8);
        formData(mat, semaine);


        for(int i=0; i< 7; i++){
            table.addCell(new Cell().add(new Paragraph(mat[i][7])));
            table.addCell(new Cell().add(new Paragraph(mat[i][0])));
            table.addCell(new Cell().add(new Paragraph(mat[i][1])));
            table.addCell(new Cell().add(new Paragraph(mat[i][2])));
            table.addCell(new Cell().add(new Paragraph(mat[i][3])));
            table.addCell(new Cell().add(new Paragraph(mat[i][4])));
            table.addCell(new Cell().add(new Paragraph(mat[i][5])));
            table.addCell(new Cell().add(new Paragraph(mat[i][6])));
        }

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

        Calendar date1 = Controller.getCalendarForm(plageHoraire.get(FIELD_PLAGE_DEBUT).toString());
        Calendar date2 = Controller.getCalendarForm(plageHoraire.get(FIELD_PLAGE_FIN).toString());
        for(int i=0; i<all.size(); i++){
            Calendar date = Controller.getCalendarForm(all.get(i).getDateReservation());
            Log.i("debut", date.getTime().toString());
            if((date.compareTo(date1) >= 0) && (date.compareTo(date2) <= 0)){
                if(!(existe_deja(all.get(i), semaine))){
                    Log.i("debut1", "apres le if");
                    semaine.add(all.get(i));
                }
            }
        }
    }

    private static boolean existe_deja(Reservation reservation, List<Reservation> semaine){
        for(int i=0; i<semaine.size(); i++){
            if(reservation.equals(semaine.get(i))){
                return true;
            }
        }
        return false;
    }

    private static int getJour(Reservation reservation) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date parse = null;
        parse = sdf.parse(reservation.getDateReservation());
        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    private static void initMatrice(String mat[][], int nbLigne, int nbreCol){
        for (int i=0; i<nbLigne; i++){
            for(int j=0; j<(nbreCol-1); j++){
                mat[i][j]= "";
            }
        }

        mat[0][nbreCol-1] = "07h-09h";
        mat[1][nbreCol-1] = "09h-11h";
        mat[2][nbreCol-1] = "11h-13h";
        mat[3][nbreCol-1] = "13h-15h";
        mat[4][nbreCol-1] = "15h-17h";
        mat[5][nbreCol-1] = "17h-19h";
        mat[6][nbreCol-1] = "19h-21h";
    }

    private static void formData(String mat[][], List<Reservation> semaine) throws ParseException {
        for(int i=0; i< semaine.size(); i++){
            int jour = getJour(semaine.get(i));

            if(jour == 2){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][0] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }

            if(jour == 3){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][1] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }

            if(jour == 4){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][2] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }

            if(jour == 5){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][3] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }

            if(jour == 6){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][4] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }

            if(jour == 7){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][5] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }

            if(jour == 1){
                if(semaine.get(i).chevauche("07:00", "09:00")){
                    mat[0][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("09:01", "11:00")){
                    mat[1][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("11:01", "13:00")){
                    mat[2][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("13:01", "15:00")){
                    mat[3][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("15:01", "17:00")){
                    mat[4][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("17:01", "19:00")){
                    mat[5][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
                if(semaine.get(i).chevauche("19:01", "21:00")){
                    mat[6][6] = semaine.get(i).getNomProf()+"\n"+semaine.get(i).getHeureDebut()+"-"+semaine.get(i).getHeureFin();
                }
            }
        }
    }

}
