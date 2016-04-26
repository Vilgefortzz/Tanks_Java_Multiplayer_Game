/*
 * Copyright (c) 2016.
 * @gklimek
 */

package models;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static gui.GamePanel.walls;
import static io.LoadImages.*;

public class Player extends Sprite {

    /*
    Identifications
     */

    private int id;
    private String login;

    private boolean randomCreated = false;

    /*
    Basic things like hp, array with missiles etc.
     */

    private int hp;
    private int dx;
    private int dy;
    private ArrayList<Missile> missiles = null;

    // Konstruktor do losowego generowania
    public Player(int id, String login) {

        super();

        this.id = id;
        this.login = login;

        this.hp = 100;
        this.missiles = new ArrayList<>();

        mainImage = tankRight;
        getImageDimensions();

        randomGenerate();
    }

    // Konstruktor do generowania na konkretnej pozycji
    public Player(int id, String login, int x, int y) {

        super(x, y);

        this.id = id;
        this.login = login;

        this.hp = 100;
        this.missiles = new ArrayList<>();

        mainImage = tankRight;
        getImageDimensions();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public ArrayList<Missile> getMissiles() {
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

    private void randomGenerate(){

        boolean isIntersection;

        // Losowe wygenerowanie na mapie czołgu

            while (!isRandomCreated()){

                isIntersection = false;

                this.x = new Random().nextInt(1330 - this.width);
                this.y = new Random().nextInt(740 - this.height);

                System.out.println(this.x);
                System.out.println(this.y);

                for (int i = 0; i < walls.size(); i++){

                    if (getBounds().intersects(walls.get(i).getBounds())){
                        isIntersection = true;
                        break;
                    }
                }

                if (!isIntersection){
                    setRandomCreated(true);
                }
            }
    }

    private void move() {

        x += dx;
        y += dy;
    }

    private void shootUP(){
        missiles.add(new Missile(x + width/2 - 5, y - 5));
    }

    private void shootDown(){
        missiles.add(new Missile(x + width/2 - 5, y + height + 2));
    }

    private void shootRight() {
        missiles.add(new Missile(x + width, y + height/2 - 5));
    }

    private void shootLeft(){
        missiles.add(new Missile(x - 10, y + height/2 - 5));
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

            if (key == KeyEvent.VK_L) if (mainImage == tankDown) {
                shootDown();
                missiles.get(missiles.size() - 1).setMainImage(missileDown);
                missiles.get(missiles.size() - 1).getImageDimensions();
            } else if (mainImage == tankUp) {
                shootUP();
                missiles.get(missiles.size() - 1).setMainImage(missileUp);
                missiles.get(missiles.size() - 1).getImageDimensions();
            } else if (mainImage == tankLeft) {
                shootLeft();
                missiles.get(missiles.size() - 1).setMainImage(missileLeft);
                missiles.get(missiles.size() - 1).getImageDimensions();
            } else {
                if (mainImage == tankRight) {
                    shootRight();
                    missiles.get(missiles.size() - 1).setMainImage(missileRight);
                    missiles.get(missiles.size() - 1).getImageDimensions();
                }
            }

            else if (key == KeyEvent.VK_S) {

                    mainImage = tankDown;
                    getImageDimensions();
                    dy = 1;
            }

            else if (key == KeyEvent.VK_A) {

                    mainImage = tankLeft;
                    getImageDimensions();
                    dx = -1;
            }

            else if (key == KeyEvent.VK_W) {

                    mainImage = tankUp;
                    getImageDimensions();
                    dy = -1;
            }

            else if (key == KeyEvent.VK_D) {

                    mainImage = tankRight;
                    getImageDimensions();
                    dx = 1;
            }

            else if (key == KeyEvent.VK_ESCAPE) {

                System.exit(0);
            }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            dy = 0;
        }

        if (key == KeyEvent.VK_A) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D) {
            dx = 0;
        }

        if (key == KeyEvent.VK_S) {
            dy = 0;
        }

    }

    public void draw( Graphics2D g2d )
    {
        g2d.drawImage(mainImage, x, y, null);
    }

    public void updateMissiles() {

        for (int i = 0; i < missiles.size(); i++) {

            if (missiles.get(i).isVisible()){

                missiles.get(i).move();
            }
            else
               missiles.remove(i);
        }
    }
    public void updateTank() {

            move();
    }

    public void checkCollisionWithWall(){

        // Sprawdzanie kolizji czołgów ze ścianami - tu jest wszystko dobrze i optymalnie

        for (Wall wall : walls) {

            if (getBounds().intersects(wall.getBounds())) {
                restorePreviousPosition();
            }
        }
    }

    public void checkCollisionMissileWithWall(){

        // Sprawdzanie kolizji pocisków ze ścianami - tu jest wszystko dobrze i optymalnie

        for (Missile missile : missiles) {

            for (Wall wall : walls) {

                if (missile.getBounds().intersects(wall.getBounds())) {

                    missile.setVisible(false);
                }
            }
        }
    }

    private void restorePreviousPosition(){

        x = x - dx;
        y = y - dy;
    }
}