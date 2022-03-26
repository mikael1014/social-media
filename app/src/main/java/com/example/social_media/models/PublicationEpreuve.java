package com.example.social_media.models;

import java.util.Date;

public class PublicationEpreuve extends Publication {
    private String lien;
    private Date datePassage;
    private Categorie categorieEpreuve;
    private String classe;


    public PublicationEpreuve(String texte, String lien, Date datePassage, Categorie categorieEpreuve, String classe) {
        this.lien = lien;
        this.datePassage = datePassage;
        this.categorieEpreuve = categorieEpreuve;
        this.classe = classe;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public Date getDatePassage() {
        return datePassage;
    }

    public void setDatePassage(Date datePassage) {
        this.datePassage = datePassage;
    }

    public Categorie getCategorieEpreuve() {
        return categorieEpreuve;
    }

    public void setCategorieEpreuve(Categorie categorieEpreuve) {
        this.categorieEpreuve = categorieEpreuve;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return super.toString() + "PublicationEpreuve{" +
                "lien='" + lien + '\'' +
                ", datePassage=" + datePassage +
                ", categorieEpreuve=" + categorieEpreuve +
                ", classe='" + classe + '\'' +
                '}';
    }
}
