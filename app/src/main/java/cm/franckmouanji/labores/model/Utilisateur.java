package cm.franckmouanji.labores.model;

import java.util.Objects;

public class Utilisateur {
    private String grade;
    private String nom;
    private String numero_telephone;
    private String identifiant;
    private String mot_de_passe;

    public Utilisateur() {
    }

    public Utilisateur(String grade, String nom, String numero_telephone, String identifiant, String mot_de_passe) {
        this.grade = grade;
        this.nom = nom;
        this.numero_telephone = numero_telephone;
        this.identifiant = identifiant;
        this.mot_de_passe = mot_de_passe;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero_telephone() {
        return numero_telephone;
    }

    public void setNumero_telephone(String numero_telephone) {
        this.numero_telephone = numero_telephone;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        Utilisateur that = (Utilisateur) o;
        return grade.equals(that.grade) && nom.equals(that.nom) && identifiant.equals(that.identifiant) && mot_de_passe.equals(that.mot_de_passe);
    }

}
