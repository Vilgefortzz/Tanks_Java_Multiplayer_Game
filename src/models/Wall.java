/*
 * Copyright (c) 2016.
 * @gklimek
 */

package models;

import java.awt.*;
import static io.LoadImages.wall;

public class Wall extends Sprite{

    public Wall(int x, int y) {

        super(x, y);
        mainImage = wall;
        getImageDimensions();
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(mainImage, x, y, null);
    }
}