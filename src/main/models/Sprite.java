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
    protected boolean vis;

    protected Image image;
    protected int width;
    protected int height;

    public Sprite() {

        vis = true;
    }

    public Sprite(int x, int y) {

        this.x =  x;
        this.y = y;
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

    public void setVisible(Boolean visible) {
        vis = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}