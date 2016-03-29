/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import main.io.MapReader;
import main.models.Missile;
import main.models.Sprite;
import main.models.Tank;
import main.models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, Runnable{

    private final int DELAY = 5;
    private Thread animation;

    private int spaceWallWidth = 0;
    private int spaceWallHeight = 0;

    private final int SPACE = 24; // rozmiar obrazka

    private ArrayList walls = new ArrayList();

    private MapReader map;
    private Tank tank;


    public Game() {
        init();
    }

    private void init() {

        addKeyListener(this);
        setFocusable(true);
        setBackground(new Color(112, 113, 76));
        setDoubleBuffered(true);
    }

    private void initWorld() {

        tank = new Tank(200, 400);
        map = new MapReader("mapDeathMatch.txt");

        Wall wall;
        ArrayList<String> lines = map.getLines();

        for (int i = 0; i < lines.size(); i++) {
            for (int j=0; j < lines.get(i).length(); j++){
                char c = lines.get(i).charAt(j);

                if (c == '-') {
                    spaceWallWidth += SPACE;

                }

                if (c == 'W') {
                    wall = new Wall(spaceWallWidth, spaceWallHeight);
                    walls.add(wall);
                    spaceWallWidth += SPACE;
                }
            }
            spaceWallWidth = 0;
            spaceWallHeight += SPACE;
        }
    }

    public void buildWorld(Graphics2D g2d) {

        for (int i=0;i < walls.size();i++){

            Wall wall = (Wall) walls.get(i);
            g2d.drawImage(wall.getImage(), wall.getX(), wall.getY(), this);
        }

        g2d.drawImage(tank.getImage(), tank.getX(), tank.getY(), this);

        ArrayList<Missile> ms = tank.getMissiles();

        for (int j = 0; j < ms.size(); j++) {
            g2d.drawImage(ms.get(j).getImage(), ms.get(j).getX(),
                    ms.get(j).getY(), this);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        buildWorld(g2d);
        drawMenuBar(g2d);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMenuBar(Graphics2D g2d){

        g2d.drawImage(new ImageIcon(getClass().getResource("/main/resources/heart.png")).getImage(), 0, 0, this);
        g2d.setColor(new Color(182, 14, 14));
        g2d.setFont(new Font("Arial", Font.BOLD, 17));
        g2d.drawString(String.valueOf(tank.getHp()), 24, 16);

        g2d.drawImage(new ImageIcon(getClass().getResource("/main/resources/explosion.png")).getImage(), 60, 0, this);
        // TODO Wyświetlanie zniszczonych czołgów
        g2d.drawImage(new ImageIcon(getClass().getResource("/main/resources/skull.png")).getImage(), 120, 0, this);
        // TODO Wyświetlanie ilości respawnów
    }

    private void updateMissiles() {

        ArrayList<Missile> ms = tank.getMissiles();

        for (int i = 0; i < ms.size(); i++) {

            if (ms.get(i).isVisible()) {

                ms.get(i).move();
            } else {

                ms.remove(i);
            }
        }
    }

    private void updateTank() {

        tank.move();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animation = new Thread(this);
        animation.start();
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        initWorld();

        while (true) {

            updateTank();
            updateMissiles();

            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        tank.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        tank.keyReleased(e);
    }
}