/*
 * Copyright (c) 2016.
 * @gklimek
 */

package database;

import utilities.Utilities;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static gui.MainFrame.yourLogin;

public class Database {

    /*
    Połączenie z bazą danych oraz statement do wykonywania poleceń SQL
     */

    private Connection connection = null;
    private Statement statement = null;
    public static Map<String, User> registeredUsers = null; // zarejestrowani użytkownicy w bazie danych < login, User >

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

        ResultSet result = statement.executeQuery("SELECT * FROM tanks.user");
        int id;
        String login, password, firstName, lastName, email;

        while (result.next()) {

            id = result.getInt("user_id");
            login = result.getString("login");
            password = result.getString("password");
            firstName = result.getString("first_name");
            lastName = result.getString("last_name");
            email = result.getString("email");

            registeredUsers.put(login, new User(id, login, password, firstName, lastName, email));
        }
    }

    public boolean registerUser(String login, String password, String firstName, String lastName, String email) {

        if (registeredUsers.size() != 0) {
            if (registeredUsers.containsKey(login))
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
                registeredUsers.put(login, new User(id, login, password, firstName, lastName, email));
            }

            // Dodanie usera do tabeli stats z zerowymi statystykami
            preparedStatement = connection.prepareStatement(sqlCommand2);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Can't insert new user to database");
            return false;

        } finally {

            if (preparedStatement != null)
                Utilities.closingStatementsInDatabases(preparedStatement);
        }

        return true;
    }

    public boolean loginUser(String login, String password) {

        if (registeredUsers.size() == 0) {
            return false;
        }

        if (registeredUsers.containsKey(login)) {
            if (registeredUsers.get(login).getPassword().equals(password)) {
                return true;
            } else
                return false;
        } else
            return false;
    }


    public boolean addStats(int id, int destroyed, int deaths) {

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

            System.err.println("Not assigned stats to user");
            return false;

        } finally {

            if (preparedStatement != null)
                Utilities.closingStatementsInDatabases(preparedStatement);
        }

        return true;
    }

    public Object[][] enterDataToTable() {

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

            ResultSet result = statement.executeQuery("SELECT u.login, s.destroyed, s.deaths, s.difference " +
                    "FROM tanks.user as u" +
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
            e.printStackTrace();
            return null;
        }
            return data;
    }
}