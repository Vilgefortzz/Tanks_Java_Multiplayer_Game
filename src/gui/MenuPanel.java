/*
 * Copyright (c) 2016.
 * @gklimek
 */

package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private BufferedImage backgroundImg;

    public MenuPanel() {

        try {
        backgroundImg = ImageIO.read(new File("res\\tanks1366x768.jpg"));
        }  catch (IOException e) {
        e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);
    }
}