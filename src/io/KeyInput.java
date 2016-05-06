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

    private Player toControlPlayer; // tylko do wysyłania odpowiednich ruchów gracza

    public Player getToControlPlayer() {
        return toControlPlayer;
    }

    public void setToControlPlayer(Player toControlPlayer) {
        this.toControlPlayer = toControlPlayer;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_L){

            // Wysłanie informacji serwerowi o oddaniu strzału przez konkretnego klienta
            sendYourFire(toControlPlayer.getId(), toControlPlayer.getOrientation());
        }
        else{

            toControlPlayer.keyMoving(key);
            // Wysłanie informacji serwerowi o ruchu konkretnego klienta
            sendYourMove(toControlPlayer.getId(), toControlPlayer.getOrientation(), toControlPlayer.getDx(), toControlPlayer.getDy());
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        toControlPlayer.keyReleased(key);

        // Wysłanie informacji serwerowi o ruchu konkretnego klienta

        sendYourMove(toControlPlayer.getId(), toControlPlayer.getOrientation(), toControlPlayer.getDx(), toControlPlayer.getDy());
    }
}