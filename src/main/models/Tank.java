/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import main.gui.views.GUI;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Tank extends Sprite{

    private double dx;
    private double dy;
    private ArrayList<Missile> missiles;
    private int tankOrientation = 1;

    public Tank() {

        super();
        init();
    }

    private void init() {

        missiles = new ArrayList<>();
        loadImage("Tank_Right.png");
        getImageDimensions();
        randomGenerate();    // ustawia pozycję czołgu generowaną losowo
    }

    private void loadImage(String imageName) {

        image = new ImageIcon(getClass().getResource("/main/resources/sprites/tanks/team_orange/" + imageName)).getImage();
    }

    public void move() {

        x += dx;
        y += dy;

        if ( x < 0)
            x = 0;
        if ( y < 0)
            y = 0;
    }

    public ArrayList<Missile> getMissiles() {
        return missiles;
    }

    private void shootUP(){
        missiles.add(new Missile(x + width/2 - 17, y - 4));
    }

    private void shootDown(){
        missiles.add(new Missile(x + width/2 - 17, y + height + 18));
    }

    private void shootRight() {
        missiles.add(new Missile(x + width, y + height/2 - 4));
    }

    private void shootLeft(){
        missiles.add(new Missile(x - 8, y + height/2 - 4));
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

            if (key == KeyEvent.VK_L) if (tankOrientation == 4) {
                shootDown();
                missiles.get(missiles.size() - 1).loadImage("Missile_Down.gif");
                missiles.get(missiles.size() - 1).setMissileOrientation(4);
            } else if (tankOrientation == 2) {
                shootUP();
                missiles.get(missiles.size() - 1).loadImage("Missile_Up.gif");
                missiles.get(missiles.size() - 1).setMissileOrientation(2);
            } else if (tankOrientation == 3) {
                shootLeft();
                missiles.get(missiles.size() - 1).loadImage("Missile_Left.gif");
                missiles.get(missiles.size() - 1).setMissileOrientation(3);
            } else {
                if (tankOrientation == 1) {
                    shootRight();
                    missiles.get(missiles.size() - 1).loadImage("Missile_Right");
                    missiles.get(missiles.size() - 1).setMissileOrientation(1);
                }
            }

                if (key == KeyEvent.VK_S) {

                    loadImage("Tank_Down.png");
                    dy = 1;
                    tankOrientation = 4;

                }

                if (key == KeyEvent.VK_A) {

                    loadImage("Tank_Left.png");
                    dx = -1;
                    tankOrientation = 3;
                }

                if (key == KeyEvent.VK_W) {

                    loadImage("Tank_Up.png");
                    dy = -1;
                    tankOrientation = 2;

                }

                if (key == KeyEvent.VK_D) {

                    loadImage("Tank_Right.png");
                    dx = 1;
                    tankOrientation = 1;
                }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_A) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }

    }

    private void randomGenerate(){

        x = new Random().nextInt(GUI.sizeX - width);
        y = new Random().nextInt(GUI.sizeY - height);
    }
}