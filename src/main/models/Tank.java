/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Tank extends Sprite{

    private int dx;
    private int dy;
    private ArrayList missiles;

    public Tank() {
        super();
        init();
    }

    private void init() {

        missiles = new ArrayList();
        loadImage("tank.png");
        getImageDimensions();
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public ArrayList getMissiles() {
        return missiles;
    }

    public void shoot() {
        missiles.add(new Missile(x + width, y + height/2));
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_L){
            shoot();
        }

        if (key == KeyEvent.VK_S) {
            dy = 1;
        }

        if (key == KeyEvent.VK_A) {
            dx = -1;
        }

        if (key == KeyEvent.VK_W) {
            dy = -1;
        }

        if (key == KeyEvent.VK_D) {
            dx = 1;
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

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }
    }
}
