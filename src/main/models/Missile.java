/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import main.gui.views.GUI;

import javax.swing.*;

public class Missile extends Sprite{

    private int damage;
    private final int MISSILE_SPEED = 8;
    private int missileOrientation;

    public Missile(int x, int y) {
        super(x, y);
        init();
    }

    private void init(){

        damage = 1;
        loadImage("Missile_Right.gif");
        getImageDimensions();
        missileOrientation = 1;
    }

    public int getDamage() {
        return damage;
    }

    void loadImage(String imageName) {

        image = new ImageIcon(getClass().getResource("/main/resources/sprites/missiles/" + imageName)).getImage();
    }

    public int getMissileOrientation() {
        return missileOrientation;
    }

    void setMissileOrientation(int missileOrientation) {
        this.missileOrientation = missileOrientation;
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

        if (x > GUI.sizeX || x < 0) {
            vis = false;
        }
        if (y > GUI.sizeY || y < 0){
            vis = false;
        }
    }
}