/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

public class Missile extends Sprite{

    private final int MISSILE_SPEED = 8;

    public Missile(int x, int y) {
        super(x, y);
        init();
    }

    private void init(){
        loadImage("missile.png");
        getImageDimensions();
    }

    public void move() {

        x += MISSILE_SPEED;
        //y += MISSILE_SPEED;

        if (x > sizeX) {
            vis = false;
        }
        if (y > sizeY){
            vis = false;
        }
    }
}
