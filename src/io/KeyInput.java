/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;

import models.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static client.Client.sendYourMove;

public class KeyInput extends KeyAdapter{

    private Player thisPlayer;

    public void setThisPlayer(Player thisPlayer) {
        this.thisPlayer = thisPlayer;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public void keyPressed(KeyEvent e) {

        thisPlayer.keyPressed(e);

        // Wysłanie informacji serwerowi o ruchu konkretnego klienta

        sendYourMove(thisPlayer.getId(), thisPlayer.getOrientation(), thisPlayer.getDx(), thisPlayer.getDy());
    }

    public void keyReleased(KeyEvent e) {

        thisPlayer.keyReleased(e);

        // Wysłanie informacji serwerowi o ruchu konkretnego klienta

        sendYourMove(thisPlayer.getId(), thisPlayer.getOrientation(), thisPlayer.getDx(), thisPlayer.getDy());
    }
}