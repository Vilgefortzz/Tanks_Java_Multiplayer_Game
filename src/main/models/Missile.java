/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.*;
import static main.io.LoadImages.*;

public class Missile extends Sprite{

    private int damage;
    private final int MISSILE_SPEED = 9;

    public Missile(int x, int y) {

        super(x, y);
        this.damage = 1;
    }

    public int getDamage() {
        return damage;
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(mainImage, x, y, null);
    }

    public void move() {

        if (mainImage == missileRight){
            x += MISSILE_SPEED;
        }
        else if (mainImage == missileUp){
            y -= MISSILE_SPEED;
        }
        else if (mainImage == missileLeft){
            x -= MISSILE_SPEED;
        }
        else if (mainImage == missileDown){
            y += MISSILE_SPEED;
        }
    }
}