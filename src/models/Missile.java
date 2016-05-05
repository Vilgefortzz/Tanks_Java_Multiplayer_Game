/*
 * Copyright (c) 2016.
 * @gklimek
 */

package models;


import java.awt.*;
import java.util.Map;

import static client.Client.myPlayer;
import static io.LoadImages.*;

public class Missile extends Sprite{

    private int damage = 5;
    private final int MISSILE_SPEED = 6;
    private int orientation;

    public Missile(int orientation, int x, int y) {

        super(x, y);
        this.orientation = orientation;
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
        for (Player player : players.values())
            if (getBounds().intersects(player.getBounds()))
                hitPlayer(player);
    }

    private void hitPlayer(Player player) {

        if (player.getId() == myPlayer.getId()){
            myPlayer.setHp(myPlayer.getHp() - damage);
        }

        setVisible(false); // pocisk staje się niewidoczny przy kontakcie z czołgiem
    }

    public void hitWall(Wall w) {
        if (getBounds().intersects(w.getBounds())) {
            setVisible(false);
        }
    }
}