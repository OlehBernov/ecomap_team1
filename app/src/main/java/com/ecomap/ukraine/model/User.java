package com.ecomap.ukraine.model;

import java.io.Serializable;

/**
 * Represents user atributes
 */
public class User {

    /**
     * Default user name
     */
    private static final String DEFAULT_NAME = "Anonym";

    /**
     * Anonym user
     */
    public static final User ANONYM_USER =  new User(-1, DEFAULT_NAME, "", "", "", "", "");

    /**
     * User id
     */
    private int id;

    /**
     * User name
     */
    private String name;

    /**
     * User surname
     */
    private String surname;

    /**
     * User role
     */
    private String role;

    /**
     * User iat
     */
    private String iat;

    /**
     * User token
     */
    private String token;

    /**
     * User email
     */
    private String email;

    /**
     * Constructor
     * @param id User id
     * @param name User name
     * @param surname User surname
     * @param role User role
     * @param iat User iat
     * @param token User token
     * @param email User email
     */
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


    /**
     * Provide access to id field
     */
    public int getId() {
        return id;
    }

    /**
     * Provide access to name field
     */
    public String getName() {
        return name;
    }

    /**
     * Provide access to surname field
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Provide access to role field
     */
    public String getRole() {
        return role;
    }

    /**
     * Provide access to iat field
     */
    public String getIat() {
        return iat;
    }

    /**
     * Provide access to token field
     */
    public String getToken() {
        return token;
    }

    /**
     * Provide access to email field
     */
    public String getEmail() {
        return email;
    }

}
