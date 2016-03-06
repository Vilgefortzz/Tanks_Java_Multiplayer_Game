/*
 * Copyright (c) 2016.
 * Made by Grzegorz Klimek
 */

package main.gui.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private BufferedImage backgroundImg;

    public MenuPanel() {
        backgroundImg = createBackgroundImage();
    }

    private BufferedImage createBackgroundImage() {
        BufferedImage backgroundImg = null;
        try {
            backgroundImg = ImageIO.read(getClass().getResource("/main/resources/menu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return backgroundImg;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);
    }

}