/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.*;

public class Sprite{

    // Parametry

    protected int x;
    protected int y;
    protected boolean vis;

    protected Image image = null;
    protected int width;
    protected int height;

    public Sprite(){

        this.vis = true;
    }

    public Sprite(int x, int y) {

        this.x = x;
        this.y = y;
        this.vis = true;
    }

    protected void getImageDimensions() {

        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return vis;
    }

    public void setVisible(Boolean visible) {
        vis = visible;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}