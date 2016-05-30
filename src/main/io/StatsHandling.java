/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static java.lang.Integer.parseInt;
import static main.client.Client.yourLogin;

public class StatsHandling {

    private boolean created = false;
    private boolean fileExists = false;

    private static List<String> lines;

    public StatsHandling() {

        try {

            File file = new File("src/main/resources/statistics/all/" +
                    yourLogin + "_all_stats.txt");
            if (file.exists()){
                fileExists = true;
            }
            else
                created = file.createNewFile();
        }
        catch(IOException | NullPointerException | SecurityException ex) {
            System.out.println("Error while creating a new empty file :" + ex);
            created = false;
            fileExists = false;
        }
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isFileExists() {
        return fileExists;
    }

    public boolean getStatsFromThisMonth() {

        LocalDate today = LocalDate.now();
        int destroyed = 0, deaths = 0;

        lines = new ArrayList<>();

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                        "/statistics/all/" + yourLogin + "_all_stats.txt")))
        ) {
            while (bufferedReader.ready()){
                lines.add(bufferedReader.readLine());
            }
            bufferedReader.close();
        }
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }

        int month = today.getMonthValue();

        for (String line: lines){

            String[] values;
            values = line.split(" ");

            if (values[0].equals(String.valueOf(month))){
                destroyed += parseInt(values[1]);
                deaths += parseInt(values[2]);
            }
        }

        // Zapis do pliku tych danych

        try (
                FileWriter fileWriter = new FileWriter("src/main/resources/statistics/monthly/" +
                        yourLogin + "_" + today.getMonth() + "_stats.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "'" + yourLogin.toUpperCase() + "'" + " STATS FROM THIS MONTH\n" +
                    "Tanks destroyed: " + destroyed +  "\n" +
                    "The number of deaths: " + deaths + "\n";
            out.print(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }

        return true;
    }

    public boolean getStatsFromLastMonth() {

        LocalDate today = LocalDate.now();
        LocalDate monthEarlier = today.minusMonths(1);
        int destroyed = 0, deaths = 0;

        lines = new ArrayList<>();

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                        "/statistics/all/" + yourLogin + "_all_stats.txt")))
        ) {
            while (bufferedReader.ready()){
                lines.add(bufferedReader.readLine());
            }
            bufferedReader.close();
        }
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }

        int month = monthEarlier.getMonthValue();

        for (String line: lines){

            String[] values;
            values = line.split(" ");

            if (values[0].equals(String.valueOf(month))){
                destroyed += parseInt(values[1]);
                deaths += parseInt(values[2]);
            }
        }

        // Zapis do pliku tych danych

        try (
                FileWriter fileWriter = new FileWriter("src/main/resources/statistics/monthly/" +
                        yourLogin + "_" + monthEarlier.getMonth() + "_stats.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "'" + yourLogin.toUpperCase() + "'" + " STATS FROM LAST MONTH\n" +
                    "Tanks destroyed: " + destroyed +  "\n" +
                    "The number of deaths: " + deaths + "\n";
            out.print(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }

        return true;
    }

    public boolean getStatsFrom2LastMonths() {

        LocalDate today = LocalDate.now();
        LocalDate monthEarlier = today.minusMonths(1);
        LocalDate twoMonthEarlier = today.minusMonths(2);
        int destroyed = 0, deaths = 0;

        lines = new ArrayList<>();

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                        "/statistics/all/" + yourLogin + "_all_stats.txt")))
        ) {
            while (bufferedReader.ready()){
                lines.add(bufferedReader.readLine());
            }
            bufferedReader.close();
        }
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }

        int month1 = monthEarlier.getMonthValue();
        int month2 = twoMonthEarlier.getMonthValue();

        for (String line: lines){

            String[] values;
            values = line.split(" ");

            if (values[0].equals(String.valueOf(month1)) || values[0].equals(String.valueOf(month2))){
                destroyed += parseInt(values[1]);
                deaths += parseInt(values[2]);
            }
        }

        // Zapis do pliku tych danych

        try (
                FileWriter fileWriter = new FileWriter("src/main/resources/statistics/monthly/" +
                        yourLogin + "_" + "from_" + twoMonthEarlier.getMonth() + "_" + "to" + "_" +
                        monthEarlier.getMonth() + "_stats.txt", false);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter out = new PrintWriter(bufferedWriter)
        ) {
            String addToFile = "'" + yourLogin.toUpperCase() + "'" + " STATS FROM TWO LAST MONTHS(SUM)\n" +
                    "Tanks destroyed: " + destroyed +  "\n" +
                    "The number of deaths: " + deaths + "\n";
            out.print(addToFile);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return false;
        }
        return true;
    }
}