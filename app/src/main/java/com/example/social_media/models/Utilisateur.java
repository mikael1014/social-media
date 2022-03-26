/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.social_media.models;


/**
 *
 * @author Assia
 */

public class Utilisateur {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private  String password;
    private RoleUtilisateur role;
    private Sexe sexe;
    private String telephone;
    private String avatar;
    private String description;


    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email, String password, Sexe sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.sexe = sexe;
    }

    public Utilisateur(String nom, String prenom, String email, RoleUtilisateur role, Sexe sexe, String telephone, String description) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.sexe = sexe;
        this.telephone = telephone;
        this.description = description;
    }

    public Utilisateur(String nom, String prenom, String email, String password, RoleUtilisateur role, Sexe sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
        this.sexe = sexe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sexe=" + sexe +
                ", telephone='" + telephone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
