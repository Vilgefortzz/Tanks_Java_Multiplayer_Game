package main;

import main.gui.views.GUI;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        Runnable doStart = new Runnable() {
            public void run() {
                new GUI();
            }
        };
        SwingUtilities.invokeLater(doStart);
    }
}

