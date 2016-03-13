/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main;

import main.gui.views.GUI;

import javax.swing.*;


public class App {
    public static void main(String[] args) {
        Runnable doStart = GUI::new;
        SwingUtilities.invokeLater(doStart);
    }
}

