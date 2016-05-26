/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Sprite{

    // Parametry

    protected int x;
    protected int y;
    protected boolean vis;

    protected BufferedImage mainImage = null;
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

    public void getImageDimensions() {

        this.width = mainImage.getWidth(null);
        this.height = mainImage.getHeight(null);
    }

    protected Image getMainImage() {
        return mainImage;
    }

    public void setMainImage(BufferedImage mainImage) {
        this.mainImage = mainImage;
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