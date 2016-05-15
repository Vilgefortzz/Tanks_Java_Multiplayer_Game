/*
 * Copyright (c) 2016.
 * @gklimek
 */

package utilities;

import java.io.Closeable;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.sql.Statement;

public class Utilities {

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

    // Funkcja haszująca (SHA-256 zapewniająca bezpieczeństwo zapisu)

    public static String passwordHashing(String nonHashPassword){

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(nonHashPassword.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte aHash : hash) {
                String hex = Integer.toHexString(0xff & aHash);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}