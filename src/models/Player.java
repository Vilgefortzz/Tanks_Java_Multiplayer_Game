/*
 * Copyright (c) 2016.
 * @gklimek
 */

package models;

import client.Client;
import gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import static client.Client.sendYourFire;
import static client.Client.sendYourMove;
import static gui.GamePanel.walls;
import static gui.MainFrame.database;
import static gui.MainFrame.yourLogin;
import static io.KeyInput.*;
import static io.LoadImages.*;

public class Player extends Sprite {

    /*
    Identifications
     */

    private int id;

    private boolean randomCreated = false;
    private int orientation;

    /*
    Basic things like hp, array with missiles etc.
     */

    private int hp = 100;
    private int destroyed = 0;
    private int deaths = 0;
    private int dx;
    private int dy;
    private List<Missile> missiles = null;

    private Client client = null;
    private MainFrame frame = null;

    // Konstruktor do losowego generowania
    public Player(int id) {

        super();

        this.id = id;
        this.missiles = new ArrayList<>();

        orientation = 3;
        mainImage = tankRight;
        getImageDimensions();

        randomGenerate();
    }

    // Konstruktor do generowania na konkretnej pozycji i z konkretną orientacją czołgu
    public Player(int id, int orientation, int x, int y) {

        super(x, y);

        this.id = id;
        this.missiles = new ArrayList<>();
        randomCreated = true;

        this.orientation = orientation;
        createTankOrientationMap();

        mainImage = tankOrientationMap.get(orientation);

        getImageDimensions();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    public int getId() {
        return id;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setDx(int dx) {this.dx = dx;}

    public void setDy(int dy) {this.dy = dy;}

    public int getDx() {return dx;}

    public int getDy() {return dy;}

    public List<Missile> getMissiles() {
        return missiles;
    }

    private boolean isRandomCreated() {
        return randomCreated;
    }

    private void setRandomCreated(boolean randomCreated) {
        this.randomCreated = randomCreated;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDestroyed() {
        return destroyed;
    }

    public void setDestroyed(int destroyed) {
        this.destroyed = destroyed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    private void randomGenerate(){

        boolean isIntersection;

        // Losowe wygenerowanie na mapie czołgu

            while (!isRandomCreated()){

                isIntersection = false;

                this.x = new Random().nextInt(1239) + 48;
                this.y = new Random().nextInt(615) + 72;

                for (Wall wall1 : walls) {

                    if (getBounds().intersects(wall1.getBounds())) {
                        isIntersection = true;
                        break;
                    }
                }

                if (!isIntersection){
                    setRandomCreated(true);
                }
            }
    }

    public void updateMovement() {

        x += dx;
        y += dy;
    }

    public void updateMissiles() {

        for (int i = 0; i < missiles.size(); i++) {

            if (missiles.get(i).isVisible()) {

                missiles.get(i).move();
            } else
                missiles.remove(i);
        }
    }

    public void shootUP(){
        missiles.add(new Missile(2, x + width/2 - 5, y - 11, id));
    }

    public void shootDown(){
        missiles.add(new Missile(4, x + width/2 - 5, y + height + 2, id));
    }

    public void shootRight() {
        missiles.add(new Missile(3, x + width, y + height/2 - 5, id));
    }

    public void shootLeft(){
        missiles.add(new Missile(1, x - 11, y + height/2 - 5, id));
    }

    public void tankMovement() {

            if (down){

                orientation = 4;
                dy = 1;
                dx = 0;
                // Wysłanie informacji serwerowi o ruchu konkretnego klienta
                sendYourMove(id, orientation, dx, dy);
            }

            if (left){

                orientation = 1;
                dx = -1;
                dy = 0;
                // Wysłanie informacji serwerowi o ruchu konkretnego klienta
                sendYourMove(id, orientation, dx, dy);
            }

            if (up){

                orientation = 2;
                dy = -1;
                dx = 0;
                // Wysłanie informacji serwerowi o ruchu konkretnego klienta
                sendYourMove(id, orientation, dx, dy);
            }

            if (right){

                orientation = 3;
                dx = 1;
                dy = 0;
                // Wysłanie informacji serwerowi o ruchu konkretnego klienta
                sendYourMove(id, orientation, dx, dy);
            }

            if (fire){

                // Wysłanie informacji serwerowi o oddaniu strzału przez konkretnego klienta
                sendYourFire(id, orientation);
            }

            if (esc){

                if (JOptionPane.showConfirmDialog(null, "Do you want to leave the game???", "PAUSE",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

                    // Dodawanie statystyk do bazy danych w momencie jak gracz opuści grę
                    if (database.addStats(id, destroyed, deaths)) {

                        System.out.println("Udalo sie zapisac dane");
                    }
                    else{
                        // TODO LOGS writing nie udalo sie zapisac danych
                        System.out.println("Nie udalo sie zapisac danych");
                    }

                    esc = keys[KeyEvent.VK_ESCAPE] = false;
                    client.disconnect();
                }
                else
                    esc = keys[KeyEvent.VK_ESCAPE] = false;
            }
    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(mainImage, x, y, null);
    }

    /*
    Collisions detection
     */

    public boolean checkCollisionWithWall(){

        boolean collision = false;

        for (Wall wall : walls) {
            if (getBounds().intersects(wall.getBounds())) {
                collision = true;
                break;
            }
        }
        return collision;
    }

    public void restorePreviousPosition(){

        x = x - dx;
        y = y - dy;
    }

    public void respawn(int x, int y){

        hp = 100;
        this.x = x;
        this.y = y;
        orientation = 3;
        mainImage = death_inform;
        getImageDimensions();
    }
}