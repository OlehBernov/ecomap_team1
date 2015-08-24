package com.ecomap.ukraine.models;

import com.ecomap.ukraine.account.manager.AccountManager;

public class User {

    private static final String DEFAULT_NAME = "Anonym";

    private static User instance;

    private int id;

    private String name;

    private String surname;

    private String role;

    private String iat;

    private String token;

    private String email;

    private User(int id, String name, String surname, String role, String iat,
                 String token, String email) {

        this.id = id;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.iat = iat;
        this.token = token;
        this.email = email;
    }

    public static User getInstance(int id, String name, String surname, String role, String iat,
                                   String token, String email) {
        instance = new User(id, name, surname, role, iat, token, email);
        return instance;
    }

    public static User getInstance() {
        if (instance == null) {
            instance = setDefaultInstance();
        }
        return instance;
    }

    public static void reset() {

        setDefaultInstance();
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

    private static User setDefaultInstance() {
        instance = new User(-1, DEFAULT_NAME, "", "", "", "",
                "");
        return instance;
    }
}
