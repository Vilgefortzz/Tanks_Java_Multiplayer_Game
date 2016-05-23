/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{

    public static boolean[] keys = new boolean[120];
    public static boolean up, down, left, right, fire, esc = false;

    public void update() {

        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        fire = keys[KeyEvent.VK_L];
        esc = keys[KeyEvent.VK_ESCAPE];
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}