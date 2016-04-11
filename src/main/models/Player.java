/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static main.gui.Game.walls;

public class Player extends Sprite implements Runnable{

    private int id;
    private String name;

    private boolean randomCreated = false;

    private int hp;
    private int dx;
    private int dy;
    private ArrayList<Missile> missiles = null;
    private int tankOrientation;

    //private Thread t = new Thread(this); // czołg jest wątkiem

    private long updated = 0;
    private long delay = 1;

    public Player(int id, String name) {

        super();

        this.id = id;
        this.name = name;

        this.hp = 100;
        this.missiles = new ArrayList<>();
        loadImage("Tank_Right.png");
        getImageDimensions();
        this.tankOrientation = 1;

        randomGenerate();

        //t.start(); // startujemy wątek
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    private void loadImage(String imageName) {

        this.image = new ImageIcon(getClass().getResource("/main/resources/sprites/tanks/team_purple/" + imageName)).getImage();
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

            if (key == KeyEvent.VK_L) if (tankOrientation == 4) {
                shootDown();
                missiles.get(missiles.size() - 1).loadImage("Missile_Down.gif");
                missiles.get(missiles.size() - 1).setMissileOrientation(4);
            } else if (tankOrientation == 2) {
                shootUP();
                missiles.get(missiles.size() - 1).loadImage("Missile_Up.gif");
                missiles.get(missiles.size() - 1).setMissileOrientation(2);
            } else if (tankOrientation == 3) {
                shootLeft();
                missiles.get(missiles.size() - 1).loadImage("Missile_Left.gif");
                missiles.get(missiles.size() - 1).setMissileOrientation(3);
            } else {
                if (tankOrientation == 1) {
                    shootRight();
                    missiles.get(missiles.size() - 1).loadImage("Missile_Right.gif");
                    missiles.get(missiles.size() - 1).setMissileOrientation(1);
                }
            }

            else if (key == KeyEvent.VK_S) {

                    loadImage("Tank_Down.png");
                    getImageDimensions();
                    dy = 1;
                    tankOrientation = 4;
            }

            else if (key == KeyEvent.VK_A) {

                    loadImage("Tank_Left.png");
                    getImageDimensions();
                    dx = -1;
                    tankOrientation = 3;
            }

            else if (key == KeyEvent.VK_W) {

                    loadImage("Tank_Up.png");
                    getImageDimensions();
                    dy = -1;
                    tankOrientation = 2;
            }

            else if (key == KeyEvent.VK_D) {

                    loadImage("Tank_Right.png");
                    getImageDimensions();
                    dx = 1;
                    tankOrientation = 1;
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
        g2d.drawImage(image, x, y, null);
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

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = delay - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}