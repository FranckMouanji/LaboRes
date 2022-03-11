package cm.franckmouanji.labores.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Comparator;

public class Reservation implements Serializable {
    private String id;
    private String grade;
    private String nomProf;
    private String numeroProf;
    private String typeReservation;
    private String dateReservation;
    private String heureDebut;
    private String heureFin;
    private Timestamp dateCreation;
    private boolean check;


    public Reservation() {
    }

    public Reservation(String id, String grade, String nomProf, String typeReservation, String dateReservation, String heureDebut, String heureFin, boolean check) {
        this.id = id;
        this.grade = grade;
        this.nomProf = nomProf;
        this.typeReservation = typeReservation;
        this.dateReservation = dateReservation;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.check = check;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public String getTypeReservation() {
        return typeReservation;
    }

    public void setTypeReservation(String typeReservation) {
        this.typeReservation = typeReservation;
    }

    public String getNumeroProf() {
        return numeroProf;
    }

    public void setNumeroProf(String numeroProf) {
        this.numeroProf = numeroProf;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return dateReservation.equals(that.dateReservation) && heureDebut.equals(that.heureDebut) && heureFin.equals(that.heureFin);
    }


    private static void translateStringToInt(String [] tabString, int [] tabInt){
        int len = tabInt.length;
        for(int i=0; i<len; i++){
            tabInt[i] = Integer.parseInt(tabString[i]);
        }
    }

    private static boolean interval(int []x, int []y, int []z){
        return (compare(x,y) && compare(z,x));
    }

    private static boolean compare(int []tab, int []tab1){
        if(tab[0]>tab1[0]){
            return  true;
        }else if((tab[0] == tab1[0]) && (tab[1]>=tab1[1])){
            return true;
        }else{
            return false;
        }
    }


    public static boolean compHeure(String heure1, String heure2){
        String [] heure1L = heure1.split(":");
        String [] heure2L = heure2.split(":");
        int[] heureDeb = new int[2];
        int[] heureFin = new int[2];

        translateStringToInt(heure1L, heureDeb);
        translateStringToInt(heure2L, heureFin);

        if(compare(heureDeb, heureFin)){
            return true;
        }else{
            return false;
        }
    }

    private static boolean imbriquer(int []tab, int []tab1, int []tab2, int []tab3){
        if(compare(tab2,tab) && compare(tab1, tab3)){
            return true;
        }else if(compare(tab,tab2) && compare(tab3, tab1)){
            return true;
        }else{
            return false;
        }
    }



    public boolean chevauche(Reservation that) {
        int[] heureDeb1 = new int[2];
        int[] heureFin1 = new int[2];
        int[] heureDeb2 = new int[2];
        int[] heureFin2 = new int[2];

        String[] heureDebS1 = this.heureDebut.split(":");
        String[] heureFinS1 = this.heureFin.split(":");
        String[] heureDebS2 = that.heureDebut.split(":");
        String[] heureFinS2 = that.heureFin.split(":");

        translateStringToInt(heureDebS1, heureDeb1);
        translateStringToInt(heureFinS1, heureFin1);
        translateStringToInt(heureDebS2, heureDeb2);
        translateStringToInt(heureFinS2, heureFin2);

        if (this.equals(that)) {
            return true;
        }else {
            if (this.dateReservation.equals(that.dateReservation)) {
                if(imbriquer(heureDeb1, heureFin1, heureDeb2, heureFin2)){
                    return true;
                } else if (interval(heureDeb2, heureDeb1, heureFin1)) {
                    return true;
                }else if(interval(heureDeb1, heureDeb2, heureFin2)){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public static Comparator<Reservation> comparatorDate = (reservation, t1) -> reservation.getDateCreation().compareTo(t1.getDateCreation());

}
