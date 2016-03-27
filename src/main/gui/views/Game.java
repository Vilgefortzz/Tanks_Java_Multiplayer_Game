/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import main.models.Missile;
import main.models.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game extends JPanel implements Runnable{

    private final int DELAY = 5;
    private Thread animation;
    private Tank tank;

    public Game() {
        init();
    }

    private void init() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        setDoubleBuffered(true);

        tank = new Tank();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        drawMenuBar(g);
        doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMenuBar(Graphics g){
        g.setColor(new Color(0,0,0, 1));
        g.drawRect(0,0, 200, 30);
        g.drawImage(new ImageIcon(getClass().getResource("/main/resources/heart.png")).getImage(), 0, 0, this);
        // TODO Wyświetlanie życia
        g.drawImage(new ImageIcon(getClass().getResource("/main/resources/explosion.png")).getImage(), 80, 0, this);
        // TODO Wyświetlanie zniszczonych czołgów
        g.drawImage(new ImageIcon(getClass().getResource("/main/resources/skull.png")).getImage(), 160, 0, this);
        // TODO Wyświetlanie ilości respawnów
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tank.getImage(), tank.getX(), tank.getY(), this);

        ArrayList<Missile> ms = tank.getMissiles();

        for (int i = 0; i < ms.size(); i++) {
            g2d.drawImage(ms.get(i).getImage(), ms.get(i).getX(),
                    ms.get(i).getY(), this);
        }
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

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            tank.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            tank.keyPressed(e);
        }
    }
}
