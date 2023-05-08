package com.example.socialgift.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Manager {
    public Manager() {
    }

    public String passwordEncrypt(String password) {
        try {
            // Obtener instancia de MessageDigest para SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Aplicar hash a la contraseña
            byte[] hashedPassword = md.digest(password.getBytes());

            // Convertir el hash a una cadena hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
