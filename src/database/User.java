/*
 * Copyright (c) 2016.
 * @gklimek
 */

package database;

public class User {

    private String login;
    private String password;

    private String firstName;
    private String lastName;
    private String email;

    public User(String login, String password, String firstName, String lastName, String email) {

        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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