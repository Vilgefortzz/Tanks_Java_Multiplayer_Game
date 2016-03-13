/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank{

    private int sizeX;
    private int sizeY;

    private int dx;
    private int dy;
    private int x;
    private int y;
    private Image image;

    public Tank() { init(); }

    private void init() {

        checkResolution();

        image = (new ImageIcon(getClass().getResource("/main/resources/tank.png"))).getImage();


        if (sizeX == 1920) {
            if (sizeY == 1080){

                x = new Random().nextInt(1921);
                y = new Random().nextInt(1081);
                }
            }

        else if (sizeX == 1366) {
            if (sizeY == 768){

                x = new Random().nextInt(1367);
                y = new Random().nextInt(769);
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

    public void move() {
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_S) {
            dy = 1;
        }

        if (key == KeyEvent.VK_A) {
            dx = -1;
        }

        if (key == KeyEvent.VK_W) {
            dy = -1;
        }

        if (key == KeyEvent.VK_D) {
            dx = 1;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_A) {
            dx = 0;
        }

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }
    }
}
