/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private BufferedImage backgroundImg;

    public MenuPanel() {

        try {
        backgroundImg = ImageIO.read(getClass().getResource("/tanks1366x768.jpg"));
        }  catch (IOException e) {
            System.out.println("Cannot set the background image!");
            System.exit(0);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);
    }
}