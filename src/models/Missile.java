/*
 * Copyright (c) 2016.
 * @gklimek
 */

package models;

import java.awt.*;
import static io.LoadImages.*;

public class Missile extends Sprite{

    private int damage = 1;
    private final int MISSILE_SPEED = 6;
    private int orientation;

    public Missile(int orientation, int x, int y) {

        super(x, y);
        this.orientation = orientation;
        mainImage = missileOrientationMap.get(orientation);
        getImageDimensions();
    }

    public int getDamage() {
        return damage;
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(mainImage, x, y, null);
    }

    public void move() {

        if (orientation == 3){
            x += MISSILE_SPEED;
        }
        else if (orientation == 2){
            y -= MISSILE_SPEED;
        }
        else if (orientation == 1){
            x -= MISSILE_SPEED;
        }
        else if (orientation == 4){
            y += MISSILE_SPEED;
        }
    }
}