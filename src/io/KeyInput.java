/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;

import gui.GamePanel;
import models.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{

    private GamePanel game;

    public KeyInput(GamePanel game) {

        this.game = game;
    }

    public void keyPressed(KeyEvent e) {

        Player me = game.players.get(1);
        me.keyPressed(e);

    }

    public void keyReleased(KeyEvent e) {

        Player me = game.players.get(1);
        me.keyReleased(e);
    }
}