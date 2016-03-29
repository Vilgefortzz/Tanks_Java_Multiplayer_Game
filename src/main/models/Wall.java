package main.models;


import javax.swing.*;

public class Wall extends Sprite{

    public Wall(int x, int y) {

        super(x, y);
        loadImage("brick1.png");
    }

    private void loadImage(String imageName) {

        image = new ImageIcon(getClass().getResource("/main/resources/sprites/walls/" + imageName)).getImage();
    }
}