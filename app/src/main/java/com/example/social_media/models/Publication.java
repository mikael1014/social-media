/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.social_media.models;


import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author Assia
 */

public class Publication implements Serializable {

    private Long id;
    private String texte;
    private Date dateCreation;
    private Utilisateur publisher;
    private Visibilite visibilite;
    private String referenceMedia;
    private int like;
    private int dislike;
    private int commentaire;

    public void setReferenceMedia(String referenceMedia) {
        this.referenceMedia = referenceMedia;
    }

    public Publication() {
    }

    public Publication(Visibilite visibilite, String texte) {
        this.texte = texte;
        this.visibilite = visibilite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Utilisateur getPublisher() {
        return publisher;
    }

    public void setPublisher(Utilisateur publisher) {
        this.publisher = publisher;
    }
    public String getReferenceMedia() {
        return referenceMedia;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", texte='" + texte + '\'' +
                ", dateCreation=" + dateCreation +
                ", publisher=" + publisher +
                '}';
    }
}
