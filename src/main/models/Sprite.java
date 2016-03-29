/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.*;

public class Sprite {

    private final int SPACE = 32;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isLeftCollision(Sprite sprite) {
        if ((x - SPACE) == sprite.x && y == sprite.y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRightCollision(Sprite sprite) {
        if ((x + SPACE) == sprite.x && y == sprite.y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTopCollision(Sprite sprite) {
        if ((y - SPACE) == sprite.y && x == sprite.x) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBottomCollision(Sprite sprite) {
        if ((y + SPACE) == sprite.y && x == sprite.x) {
            return true;
        } else {
            return false;
        }
    }
}