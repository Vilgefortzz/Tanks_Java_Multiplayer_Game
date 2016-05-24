/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.database;

public class User {

    private int id;
    private String login;
    private String password;

    private String firstName;
    private String lastName;
    private String email;

    // Do zczytania danych
    public User(int id, String login, String password, String firstName, String lastName, String email) {

        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}