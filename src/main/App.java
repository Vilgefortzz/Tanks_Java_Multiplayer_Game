/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main;

import main.gui.GUI;

import java.awt.*;


public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new GUI();
            }
        });
    }
}