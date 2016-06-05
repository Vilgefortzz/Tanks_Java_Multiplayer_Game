/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.database;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

import static main.client.Client.yourLogin;
import static main.logs.Logs.log;
import static main.utilities.Utilities.closingStatementsInDatabases;

public class Database {

    /*
    Połączenie z bazą danych oraz statement do wykonywania poleceń SQL
     */

    private Connection connection = null;
    private Statement statement = null;

    public synchronized void connectToDatabase() throws SQLException {

        try {

            String address, url;
            String user = null, password = null;

            address = JOptionPane.showInputDialog(null, "Server address of database: ", "Address",
                    JOptionPane.PLAIN_MESSAGE);

            try (
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            this.getClass().getResourceAsStream("/MySQL-Account.txt")))
            ) {

                String[] lines = new String[2];
                int i = 0;

                while (bufferedReader.ready()){

                    lines[i++] = bufferedReader.readLine();
                }

                user = lines[0];
                password = lines[1];

            } catch (IOException e) {
                System.err.println("IOException: " + e.getMessage());
                System.exit(0);
            }

            url = "jdbc:mysql://" + address + ":3306/tanks?autoReconnect=true&useSSL=false";
            //url = "jdbc:mysql://localhost:3306/tanks?autoReconnect=true&useSSL=false";

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new SQLException("Cannot connect to Database", e);
        }
    }

    public synchronized int takeID(String login){

        try {

            ResultSet result = statement.executeQuery("SELECT user_id FROM tanks.user WHERE login = '" + login + "'");
            if (result.next()){
                return result.getInt("user_id");
            }

        } catch (SQLException e) {
            System.err.println("Player's id hadn't taken");
        }
        return 0;
    }

    public synchronized boolean registerUser(String login, String password, String firstName, String lastName, String email) {

        // Sprawdzanie czy istnieje gracz o tym samym loginie albo e-mailu
        try {

            ResultSet result = statement.executeQuery("SELECT login FROM tanks.user WHERE login = '" + login + "'");
            if (result.next()){
                return false;
            }

            result = statement.executeQuery("SELECT email FROM tanks.user WHERE email = '" + email + "'");
            if (result.next()){
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Invalid sql command in registration");
            return false;
        }

        PreparedStatement preparedStatement = null;

        String sqlCommand1 = "INSERT INTO tanks.user"
                + "(login, password, first_name, last_name, email) VALUES"
                + "(?,?,?,?,?)";

        String sqlCommand2 = "INSERT INTO tanks.stats"
                + "(user_id) VALUES"
                + "(?)";

        try {

            preparedStatement = connection.prepareStatement(sqlCommand1);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);

            preparedStatement.executeUpdate();

            ResultSet result = statement.executeQuery("SELECT user_id FROM tanks.user WHERE login = '" + login + "'");
            int id = 0;
            if (result.next()) {
                id = result.getInt("user_id");
            }

            // Dodanie usera do tabeli stats z zerowymi statystykami
            preparedStatement = connection.prepareStatement(sqlCommand2);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            log("client", "Register new user is failed");
            System.err.println("Can't insert new user to database");
            return false;

        } finally {

            if (preparedStatement != null)
                closingStatementsInDatabases(preparedStatement);
        }

        return true;
    }

    public synchronized boolean loginUser(String login, String password) {

        // Sprawdzanie czy istnieje dany gracz o danym loginie i danym haśle
        try {

            ResultSet result = statement.executeQuery("SELECT login, password FROM tanks.user WHERE login = '" + login + "'");
            if (result.next()){

                String passwd;
                passwd = result.getString("password");

                return passwd.equals(password);
            }
            else
                return false;

        } catch (SQLException e) {
            System.err.println("Invalid sql command in logging");
            return false;
        }
    }

    public synchronized boolean addStats(int id, int destroyed, int deaths) {

        PreparedStatement preparedStatement = null;
        String sqlCommand = "UPDATE tanks.stats SET destroyed = ? , deaths = ? , difference = ? WHERE user_id = ?";

        int previousDestroyed = 0;
        int previousDeaths = 0;

        try {

            // Zczytanie wartości sprzed aktualizacji
            ResultSet result = statement.executeQuery("SELECT * FROM tanks.stats WHERE user_id = '" + id + "'");

            if (result.next()){

                previousDestroyed = result.getInt("destroyed");
                previousDeaths = result.getInt("deaths");
            }

            int actualDestroyed = destroyed + previousDestroyed;
            int actualDeaths = deaths + previousDeaths;
            int actualDifference = actualDestroyed - actualDeaths;

            preparedStatement = connection.prepareStatement(sqlCommand);

            preparedStatement.setInt(1, actualDestroyed);
            preparedStatement.setInt(2, actualDeaths);
            preparedStatement.setInt(3, actualDifference);
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Not assigned stats to user" + " " + e.getMessage());
            return false;

        } finally {

            if (preparedStatement != null)
                closingStatementsInDatabases(preparedStatement);
        }

        return true;
    }

    public synchronized Object[][] loadDataToTable() {

        int numberOfPlayers = 0;
        int indexRow = 0;
        int indexCol = 0;
        String login;
        int destroyed, deaths, difference;
        Object[][] data;

        try {

            ResultSet rezultat = statement.executeQuery("SELECT * FROM tanks.stats");

            while(rezultat.next()) {
                numberOfPlayers++;
            }

            data = new Object[numberOfPlayers][4];

            ResultSet result = statement.executeQuery("SELECT u.login, s.destroyed, s.deaths, s.difference" +
                    " FROM tanks.user as u" +
                    " INNER JOIN tanks.stats as s" +
                    " WHERE u.user_id = s.user_id");

            while(result.next()) {

                login = result.getString("login");

                if (login.equals(yourLogin)){
                    data[indexRow][indexCol++] = "YOU";
                }
                else
                    data[indexRow][indexCol++] = login;

                destroyed = result.getInt("destroyed");
                data[indexRow][indexCol++] = destroyed;
                deaths = result.getInt("deaths");
                data[indexRow][indexCol++] = deaths;
                difference = result.getInt("difference");
                data[indexRow][indexCol] = difference;

                indexRow++;
                indexCol = 0;
            }

        } catch (SQLException e) {

            log("client", "Enter data to table is failed");
            e.printStackTrace();
            return null;
        }
            return data;
    }

    public synchronized int[] takeStats(int id){

        int[] variables = new int[3];

        // Sprawdzanie statystyk konkretnego gracza
        try {

            ResultSet result = statement.executeQuery("SELECT * FROM tanks.stats WHERE user_id = '" + id + "'");
            if (result.next()){

                variables[0] = result.getInt("destroyed");
                variables[1] = result.getInt("deaths");
                variables[2] = result.getInt("difference");
            }

        } catch (SQLException e) {
            System.err.println("Invalid sql command in taking the stats");
            return null;
        }

        return variables;
    }
}