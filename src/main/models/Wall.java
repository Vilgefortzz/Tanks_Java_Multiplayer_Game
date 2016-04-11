package main.models;


import javax.swing.*;
import java.awt.*;

public class Wall extends Sprite{

    public Wall(int x, int y) {

        super(x, y);
        loadImage("brick1.png");
        getImageDimensions();
    }

    private void loadImage(String imageName) {

        image = new ImageIcon(getClass().getResource("/main/resources/sprites/walls/" + imageName)).getImage();
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(image, x, y, null);
    }
}