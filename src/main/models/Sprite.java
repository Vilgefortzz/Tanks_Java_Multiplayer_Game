/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Sprite {

    // Rozdzielczość ekranu

    protected int sizeX;
    protected int sizeY;

    // Parametry

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean vis;
    protected Image image;

    public Sprite() {

        checkResolution();
        randomGenerate(); // ustawia pozycję sprite'a generowaną losowo
        vis = true;
    }

    public Sprite(int x, int y) {
        checkResolution();
        this.x = x;
        this.y = y;
        vis = true;
    }

    protected void loadImage(String imageName) {

        image = new ImageIcon(getClass().getResource("/main/resources/" + imageName)).getImage();
    }

    protected void getImageDimensions() {

        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return vis;
    }

    public void setVisible(Boolean visible) {
        vis = visible;
    }

    private void checkResolution(){

        Toolkit tk = Toolkit.getDefaultToolkit();
        sizeX = (int) tk.getScreenSize().getWidth();
        sizeY = (int) tk.getScreenSize().getHeight();
    }

    private void randomGenerate(){
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
}
