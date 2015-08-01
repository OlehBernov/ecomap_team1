package com.ecomap.ukraine.models;

import java.io.Serializable;

/**
 * TODO doc
 */
public class User implements Serializable {

    private int id;

    private String name;

    private String surname;

    private String role;

    private String iat;

    private String token;

    private String email;

    public User(int id, String name, String surname, String role, String iat,
                String token, String email) {

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.iat = iat;
        this.token = token;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return role;
    }

    public String getIat() {
        return iat;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}
