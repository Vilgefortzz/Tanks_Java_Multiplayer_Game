/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main;

import main.gui.views.GUI;

import javax.swing.*;
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