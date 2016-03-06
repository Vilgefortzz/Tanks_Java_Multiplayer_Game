/*
 * Copyright (c) 2016.
 * Made by Grzegorz Klimek
 */

package main;

import main.gui.views.GUI;

import javax.swing.*;


public class App {
    public static void main(String[] args) {
        Runnable doStart = () -> new GUI();
        SwingUtilities.invokeLater(doStart);
    }
}

