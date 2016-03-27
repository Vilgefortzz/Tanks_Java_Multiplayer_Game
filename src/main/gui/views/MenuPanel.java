/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuPanel extends JPanel {

    private BufferedImage backgroundImg;

    public MenuPanel() { init(); }

    private void init() {

        setPreferredSize(new Dimension(GUI.sizeX,GUI.sizeY));

        try {
            backgroundImg = ImageIO.read(getClass().getResource("/main/resources/tanks1366x768.jpg"));
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