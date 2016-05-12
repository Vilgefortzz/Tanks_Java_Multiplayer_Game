/*
 * Copyright (c) 2016.
 * @gklimek
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    /*
    Połączenie z bazą danych
     */

    private Connection connection = null;

    public void connectToDatabase () throws SQLException {

        try{

            String host = "jdbc:mysql://localhost:3306/tanks?autoReconnect=true&useSSL=false";
            String user = "root";
            String passwd = "welcomeYou1.";
            connection = DriverManager.getConnection(host, user, passwd);

        } catch (SQLException e) {
            throw new SQLException("Cannot connect to database", e);
        }
    }
}
