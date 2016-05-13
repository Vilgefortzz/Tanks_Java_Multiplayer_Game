/*
 * Copyright (c) 2016.
 * @gklimek
 */

package database;


import utilities.ConnectionHandling;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    /*
    Połączenie z bazą danych oraz statement do wykonywania poleceń SQL
     */

    private Connection connection = null;
    private Statement statement = null;
    private Map<String, User> registeredUsers = null; // zarejestrowani użytkownicy w bazie danych < login, User >

    public void connectToDatabase() throws SQLException {

        try {

            String host = "jdbc:mysql://localhost:3306/tanks?autoReconnect=true&useSSL=false";
            String user = "root";
            String passwd = "welcomeYou1.";
            connection = DriverManager.getConnection(host, user, passwd);
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new SQLException("Cannot connect to database", e);
        }
    }

    public void fillMapFromDatabase() throws SQLException {

        registeredUsers = new HashMap<>();

        ResultSet result = statement.executeQuery("SELECT * FROM `tanks`.user");
        String login, password, firstName, lastName, email;

        while (result.next()) {

            login = result.getString("login");
            password = result.getString("password");
            firstName = result.getString("first_name");
            lastName = result.getString("last_name");
            email = result.getString("email");

            registeredUsers.put(login, new User(login, password, firstName, lastName, email));
        }
    }

    public boolean registerUser(String login, String password, String firstName, String lastName, String email) {

        if (registeredUsers.size() != 0) {
            if (registeredUsers.containsKey(login))
                return false;
        }

        registeredUsers.put(login, new User(login, password, firstName, lastName, email));

        PreparedStatement preparedStatement = null;
        String sqlCommand = "INSERT INTO `tanks`.user"
                + "(login, password, first_name, last_name, email) VALUES"
                + "(?,?,?,?,?)";

        try {

            preparedStatement = connection.prepareStatement(sqlCommand);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Can't insert new user to database");
            e.printStackTrace();
            return false;

        } finally {

            if (preparedStatement != null)
                ConnectionHandling.closingStatementsInDatabases(preparedStatement);
        }

        return true;
    }

    public boolean loginUser(String login, String password){

        if (registeredUsers.size() == 0) {
                return false;
        }

        if (registeredUsers.containsKey(login)){
            if (registeredUsers.get(login).getPassword().equals(password)){
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }
}