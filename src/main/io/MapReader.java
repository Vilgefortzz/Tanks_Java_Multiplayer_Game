/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapReader {

    private ArrayList<String> lines;

    public MapReader(String mapFile) {

        try {
            readFile(mapFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    private void readFile(String filename) throws IOException {

        lines = new ArrayList<>();

        String filePath = new File("").getAbsolutePath();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + "/src/main/resources/maps/" + filename))) {

            do {
                lines.add(bufferedReader.readLine());

            } while (bufferedReader.ready());
        }
    }
}