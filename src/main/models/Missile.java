/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import javax.swing.*;
import java.awt.*;

public class Missile extends Sprite{

    private int damage;
    private final int MISSILE_SPEED = 9;
    private int missileOrientation;

    public Missile(int x, int y) {

        super(x, y);

        this.damage = 1;
        loadImage("Missile_Right.gif");
        getImageDimensions();
        this.missileOrientation = 1;
    }

    public int getDamage() {
        return damage;
    }

    void loadImage(String imageName) {

        this.image = new ImageIcon(getClass().getResource("/main/resources/sprites/missiles/" + imageName)).getImage();
    }

    public int getMissileOrientation() {
        return missileOrientation;
    }

    void setMissileOrientation(int missileOrientation) {
        this.missileOrientation = missileOrientation;
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(image, x, y, null);
    }

    public void move() {

        if (missileOrientation == 1){
            x += MISSILE_SPEED;
        }
        else if (missileOrientation == 2){
            y -= MISSILE_SPEED;
        }
        else if (missileOrientation == 3){
            x -= MISSILE_SPEED;
        }
        else if (missileOrientation == 4){
            y += MISSILE_SPEED;
        }
    }
}