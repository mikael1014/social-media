/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.social_media.models;

import java.util.Date;

/**
 *
 * @author Assia
 */

public class Message {
    private Long idMessage;
    private String contenu;
    private Date dateEnvoie;
    private boolean lu;
    private Utilisateur emmetteur;
    private Utilisateur destinataire;
}
