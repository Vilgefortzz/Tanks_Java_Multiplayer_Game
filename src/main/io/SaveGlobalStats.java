/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.*;

import static main.client.Client.database;
import static main.client.Client.yourID;
import static main.client.Client.yourLogin;

public class SaveGlobalStats {

    public static boolean saveGlobalStatistics(){

        // Zczytanie id danego gracza z bazy danych
        yourID = database.takeID(yourLogin);

        // Sprawdzanie statystyk danego gracza
        int[] variables;
        variables = database.takeStats(yourID);

        try (
                FileWriter fileWriter = new FileWriter("src/main/resources/statistics/global/" +
                        yourLogin + "_stats.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "STATS FROM USER '" + yourLogin.toUpperCase() + "'" + "\n" +
                    "Tanks destroyed: " + variables[0] +  "\n" +
                    "The number of deaths: " + variables[1] + "\n" +
                    "Difference between tanks destroyed and the number of deaths: " + variables[2];
            out.print(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }
        return true;
    }
}