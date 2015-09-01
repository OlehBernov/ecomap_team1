package com.ecomap.ukraine.models;

import com.google.android.gms.ads.search.SearchAdRequest;

import java.io.Serializable;

public class User implements Serializable{

    private static final String DEFAULT_NAME = "Anonym";

    public static final User ANONYM_USER =  new User(-1, DEFAULT_NAME, "", "", "", "", "");

    private static User instance;

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
