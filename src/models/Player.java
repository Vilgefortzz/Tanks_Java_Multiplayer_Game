/*
 * Copyright (c) 2016.
 * @gklimek
 */

package models;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import static gui.GamePanel.walls;
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
    private int dx;
    private int dy;
    private ArrayList<Missile> missiles = null;


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

        this.orientation = orientation;
        createTankOrientationMap();

        mainImage = tankOrientationMap.get(orientation);

        getImageDimensions();
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
        missiles.add(new Missile(2, x + width/2 - 5, y - 5));
    }

    public void shootDown(){
        missiles.add(new Missile(4, x + width/2 - 5, y + height + 2));
    }

    public void shootRight() {
        missiles.add(new Missile(3, x + width, y + height/2 - 5));
    }

    public void shootLeft(){
        missiles.add(new Missile(1, x - 10, y + height/2 - 5));
    }

    public void keyMoving(int key) {

            if (key == KeyEvent.VK_S){

                orientation = 4;
                dy = 2;
            }

            else if (key == KeyEvent.VK_A){

                orientation = 1;
                dx = -2;
            }

            else if (key == KeyEvent.VK_W){

                orientation = 2;
                dy = -2;
            }

            else if (key == KeyEvent.VK_D){

                orientation = 3;
                dx = 2;
            }

            else if (key == KeyEvent.VK_ESCAPE){

                System.exit(0);
            }
    }

    public void keyReleased(int key) {

        if (key == KeyEvent.VK_W)
            dy = 0;

        if (key == KeyEvent.VK_A)
            dx = 0;

        if (key == KeyEvent.VK_D)
            dx = 0;

        if (key == KeyEvent.VK_S)
            dy = 0;
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

    public void checkCollisionMissileWithWall(){

        for (Missile missile : missiles){
            for (Wall wall : walls){
                if (missile.getBounds().intersects(wall.getBounds()))
                    missile.setVisible(false);
            }
        }
    }

    public void restorePreviousPosition(){

        x = x - dx;
        y = y - dy;
    }
}