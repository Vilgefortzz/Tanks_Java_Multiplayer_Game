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
    private int sizeX;
    private int sizeY;

    public MenuPanel() { init(); }

    private void init() {

        checkResolution();

        if (sizeX == 1920) {
            if (sizeY == 1080){
                try {
                    backgroundImg = ImageIO.read(getClass().getResource("/main/resources/tanks1920x1080.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }

        else if (sizeX == 1366) {
            if (sizeY == 768){
                try {
                    backgroundImg = ImageIO.read(getClass().getResource("/main/resources/tanks1366x768.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else{
            System.out.println("Zla rozdzielczosc ekranu. Dostepne sa 1366x768 lub 1920x1080");
            System.exit(0);
        }
    }

    private void checkResolution(){

        Toolkit tk = Toolkit.getDefaultToolkit();
        sizeX = (int) tk.getScreenSize().getWidth();
        sizeY = (int) tk.getScreenSize().getHeight();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);
    }
}