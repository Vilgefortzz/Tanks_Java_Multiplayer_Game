/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import java.awt.*;
import java.util.Map;

import static main.client.Client.myPlayer;
import static main.client.Client.sendDestroyedBy;
import static main.client.Client.sendYourRespawn;
import static main.io.Images.*;

public class Missile extends Sprite{

    private int damage = 1;
    private final int MISSILE_SPEED = 8;
    private int orientation;
    private int ownerID;

    public Missile(int orientation, int x, int y, int ownerID) {

        super(x, y);
        this.orientation = orientation;
        this.ownerID = ownerID;
        mainImage = missileOrientationMap.get(orientation);
        getImageDimensions();
    }

    public int getDamage() {
        return damage;
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(mainImage, x, y, null);
    }

    public void move() {

        if (orientation == 3){
            x += MISSILE_SPEED;
        }
        else if (orientation == 2){
            y -= MISSILE_SPEED;
        }
        else if (orientation == 1){
            x -= MISSILE_SPEED;
        }
        else if (orientation == 4){
            y += MISSILE_SPEED;
        }
    }

    public void hitPlayers(Map<Integer, Player> players) {
        players.values().stream().filter(player -> getBounds().intersects(player.getBounds())).forEachOrdered(this::hitPlayer);
    }

    private void hitPlayer(Player player) {

        if (player.getId() == myPlayer.getId()){

            myPlayer.setHp(myPlayer.getHp() - damage); // traci życie

            if (myPlayer.getHp() == 0){

                Player tempPlayer = new Player(myPlayer.getId()); // stworzenie na nowej pozycji gracza tego samego

                sendYourRespawn(tempPlayer.getId(), tempPlayer.getX(), tempPlayer.getY()); // informacja o odnowieniu
                sendDestroyedBy(ownerID); // informacja o zniszczonym czołgu przez ten pocisk
            }
        }

        setVisible(false); // pocisk staje się niewidoczny przy kontakcie z czołgiem
    }

    public void hitWall(Wall w) {
        if (getBounds().intersects(w.getBounds())) {
            setVisible(false);
        }
    }
}