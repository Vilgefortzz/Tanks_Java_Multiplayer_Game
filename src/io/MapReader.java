/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;

import java.io.BufferedReader;
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

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("res\\maps\\" + filename))) {

            do {
                lines.add(bufferedReader.readLine());

            } while (bufferedReader.ready());
        }
    }
}