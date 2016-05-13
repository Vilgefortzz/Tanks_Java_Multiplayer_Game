/*
 * Copyright (c) 2016.
 * @gklimek
 */

package utilities;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionHandling {

    public static void closingSocketsAndStreams(Closeable object) {

        try {
            object.close();

        } catch (IOException e) {
            System.out.println("Problem with closing the object!");
        }
    }

    public static void closingStatementsInDatabases(Statement object){

        try {
            object.close();

        } catch (SQLException e) {
            System.out.println("Problem with closing the object!");
        }
    }

    public static void join(Thread thread) {

        try {
            thread.join();

        } catch (InterruptedException e) {
            System.out.println("Incorrectly stopping a thread!");
        }
    }
}