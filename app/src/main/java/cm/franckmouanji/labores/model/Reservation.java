package cm.franckmouanji.labores.model;

import android.provider.Settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Reservation {
    private String id;
    private String grade;
    private String nomProf;
    private String typeReservation;
    private String dateReservation;
    private String heureDebut;
    private String heureFin;
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

    public boolean chevauche(Reservation that){
        if(this.equals(that)){
            return true;
        }else{
            if(this.dateReservation.equals(that.dateReservation)){
                DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.forLanguageTag(Settings.Global.AUTO_TIME_ZONE));

                try {
                    Date date = df.parse(this.heureDebut);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

}
