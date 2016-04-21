/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main;

import main.gui.MainFrame;

import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static main.gui.MainFrame.sizeX;
import static main.gui.MainFrame.sizeY;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                MainFrame frame = new MainFrame();

                frame.setTitle("Tanks - Multiplayer by GK");
                frame.setSize(sizeX, sizeY);
                frame.setResizable(false);

                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}