/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.*;

public class Sprite {

    // Parametry

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean vis;
    protected Image image;

    public Sprite() {

        vis = true;
    }

    public Sprite(double x, double y) {

        this.x = (int) x;
        this.y = (int) y;
        vis = true;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setVisible(Boolean visible) {
        vis = visible;
    }
}
