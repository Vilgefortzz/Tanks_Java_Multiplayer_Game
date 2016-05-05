/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;

import models.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static client.Client.sendYourFire;
import static client.Client.sendYourMove;

public class KeyInput extends KeyAdapter{

    private Player thisPlayer; // tylko do wysyłania odpowiednich ruchów gracza

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public void setThisPlayer(Player thisPlayer) {
        this.thisPlayer = thisPlayer;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_L){

            // Wysłanie informacji serwerowi o oddaniu strzału przez konkretnego klienta
            sendYourFire(thisPlayer.getId(), thisPlayer.getOrientation());
        }
        else{

            thisPlayer.keyMoving(key);
            // Wysłanie informacji serwerowi o ruchu konkretnego klienta
            sendYourMove(thisPlayer.getId(), thisPlayer.getOrientation(), thisPlayer.getDx(), thisPlayer.getDy());
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        thisPlayer.keyReleased(key);

        // Wysłanie informacji serwerowi o ruchu konkretnego klienta

        sendYourMove(thisPlayer.getId(), thisPlayer.getOrientation(), thisPlayer.getDx(), thisPlayer.getDy());
    }
}