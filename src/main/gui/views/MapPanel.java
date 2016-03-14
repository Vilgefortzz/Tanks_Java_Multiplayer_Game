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

public class MapPanel extends JPanel implements ActionListener{

    private Timer timer;
    private Tank tank;
    private final int DELAY = 8;

    public MapPanel() {
        init();
    }

    private void init() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        setDoubleBuffered(true);

        tank = new Tank();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tank.getImage(), tank.getX(), tank.getY(), this);

        ArrayList ms = tank.getMissiles();

        for (int i = 0; i < ms.size(); i++) {
            Missile m = (Missile) ms.get(i);
            g2d.drawImage(m.getImage(), m.getX(),
                    m.getY(), this);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateMissiles();
        updateTank();
        repaint();
    }

    private void updateMissiles() {

        ArrayList ms = tank.getMissiles();

        for (int i = 0; i < ms.size(); i++) {

            Missile m = (Missile) ms.get(i);

            if (m.isVisible()) {

                m.move();
            } else {

                ms.remove(i);
            }
        }
    }

    private void updateTank() {

        tank.move();
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
