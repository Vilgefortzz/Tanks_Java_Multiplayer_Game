/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import main.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import static main.client.Client.database;
import static main.client.Client.sendYourFire;
import static main.client.Client.sendYourMove;
import static main.gui.GamePanel.walls;
import static main.gui.MainFrame.yourLogin;
import static main.io.KeyInput.*;
import static main.io.Images.*;
import static main.logs.Logs.log;

public class Player extends Sprite {

    /*
    Identifications
     */

    private int id;

    private boolean randomCreated = false;
    private int orientation;

    /*
    Basic things like hp, array with bullets etc.
     */

    private int hp = 100;
    private int destroyed = 0;
    private int deaths = 0;
    private int dx;
    private int dy;
    private List<Bullet> bullets = null;

    private Client client = null;

    // Konstruktor do losowego generowania
    public Player(int id) {

        super();

        this.id = id;
        this.bullets = new ArrayList<>();

        orientation = 3;
        mainImage = tankRight;
        getImageDimensions();

        randomGenerate();
    }

    // Konstruktor do generowania na konkretnej pozycji i z konkretną orientacją czołgu
    public Player(int id, int orientation, int x, int y) {

        super(x, y);

        this.id = id;
        this.bullets = new ArrayList<>();
        randomCreated = true;

        this.orientation = orientation;
        createTankOrientationMap();

        mainImage = tankOrientationMap.get(orientation);

        getImageDimensions();
    }

    public void setClient(Client client) {
        this.client = client;
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

    public List<Bullet> getBullets() {
        return bullets;
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

    public void updateBullets() {

        for (int i = 0; i < bullets.size(); i++) {

            if (bullets.get(i).isVisible()) {

                bullets.get(i).move();
            } else
                bullets.remove(i);
        }
    }

    public void shootUP(){
        bullets.add(new Bullet(2, x + width/2 - 5, y - 11, id));
    }

    public void shootDown(){
        bullets.add(new Bullet(4, x + width/2 - 5, y + height + 2, id));
    }

    public void shootRight() {
        bullets.add(new Bullet(3, x + width, y + height/2 - 5, id));
    }

    public void shootLeft(){
        bullets.add(new Bullet(1, x - 11, y + height/2 - 5, id));
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

                        log("client", "Assigned stats to: " + yourLogin + " [ " + destroyed + ", " + deaths + " ] ");
                        System.out.println("Properly assigned stats");
                    }
                    else{

                        log("client", "Assigning stats to " + yourLogin +  " is failed");
                        System.out.println("Can't assign stats");
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