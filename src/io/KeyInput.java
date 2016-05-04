/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;

import gui.GamePanel;
import models.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static client.Client.clientPlayerID;
import static client.Client.sendYourMove;

public class KeyInput extends KeyAdapter{

    private GamePanel game;

    public KeyInput(GamePanel game) {

        this.game = game;
    }

    public void keyPressed(KeyEvent e) {

        Player me = game.players.get(clientPlayerID);
        me.keyPressed(e);

        // Wysłanie informacji serwerowi o ruchu konkretnego klienta

        sendYourMove(me.getId(), me.getOrientation(), me.getDx(), me.getDy());
    }

    public void keyReleased(KeyEvent e) {

        Player me = game.players.get(clientPlayerID);
        me.keyReleased(e);

        // Wysłanie informacji serwerowi o ruchu konkretnego klienta

        sendYourMove(me.getId(), me.getOrientation(), me.getDx(), me.getDy());
    }
}