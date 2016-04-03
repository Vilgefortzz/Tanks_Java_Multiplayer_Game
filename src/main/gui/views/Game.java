/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import main.io.MapReader;
import main.models.Missile;
import main.models.Player;
import main.models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, Runnable{

    private final int DELAY = 6;
    private Thread animation;

    private int spaceWallWidth = 0;
    private int spaceWallHeight = 0;

    private final int SPACE = 24; // rozmiar ściany - obrazka

    private ArrayList<Wall> walls;
    private ArrayList<Player> players;

    private MapReader map;

    public Game() {
        init();
    }

    private void init() {

        addKeyListener(this);
        setFocusable(true);

        initWorld();

        setBackground(new Color(112, 113, 76));
        setDoubleBuffered(true);
    }

    private void initWorld() {

        players = new ArrayList<>();

        players.add(new Player(200, 400));
        players.add(new Player(800, 300));

        walls = new ArrayList<>();
        map = new MapReader("mapDeathMatch.txt");

        ArrayList<String> lines = map.getLines();

        for (int i = 0; i < lines.size(); i++) {
            for (int j=0; j < lines.get(i).length(); j++){
                char c = lines.get(i).charAt(j);

                if (c == '-') {
                    spaceWallWidth += SPACE;

                }

                if (c == 'W') {

                    walls.add(new Wall(spaceWallWidth, spaceWallHeight));
                    spaceWallWidth += SPACE;
                }
            }
            spaceWallWidth = 0;
            spaceWallHeight += SPACE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawMenuBar(g2d);
        paintWorld(g2d);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMenuBar(Graphics2D g2d){

        g2d.drawImage(new ImageIcon(getClass().getResource("/main/resources/heart.png")).getImage(), 0, 0, this);
        g2d.setColor(new Color(182, 14, 14));
        g2d.setFont(new Font("Arial", Font.BOLD, 17));

        /* Narazie może wygrać tylko pierwszy czołg - w innym przpyadku program się zawiesza z powodu
           wyświetlania paska życia dla pierwszego czołgu na sztywno
         */

        g2d.drawString(String.valueOf(players.get(0).getHp()), 24, 16);

        if (players.size() == 2){
            g2d.drawString(String.valueOf(players.get(1).getHp()), 300, 16);
        }


        g2d.drawImage(new ImageIcon(getClass().getResource("/main/resources/explosion.png")).getImage(), 60, 0, this);
        // TODO Wyświetlanie zniszczonych czołgów
        g2d.drawImage(new ImageIcon(getClass().getResource("/main/resources/skull.png")).getImage(), 120, 0, this);
        // TODO Wyświetlanie ilości respawnów
    }

    private void paintWorld(Graphics2D g2d) {

        // Ścian nie można niszczyć, więc przy tworzeniu są one zawsze widoczne

        for (int i=0; i < walls.size(); i++){
            g2d.drawImage(walls.get(i).getImage(), walls.get(i).getX(), walls.get(i).getY(), this);
        }

        // Obiekty, które mogą być niszczone - czołgi(gracze)

        for (int i=0;i<players.size();i++) {

            g2d.drawImage(players.get(i).getImage(), players.get(i).getX(), players.get(i).getY(), this);
        }

        // Rysowanie pocisków, jeżeli gracz żyje to rysuje

        for (int i=0;i<players.size();i++){

            for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                if (players.get(i).getMissiles().get(j).isVisible()){
                    g2d.drawImage(players.get(i).getMissiles().get(j).getImage(), players.get(i).getMissiles().get(j).getX(),
                            players.get(i).getMissiles().get(j).getY(), this);
                }
            }
        }
    }

    private void updateMissiles() {

        for (int i=0;i<players.size();i++) {

            for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                if (players.get(i).getMissiles().get(j).isVisible()){

                    players.get(i).getMissiles().get(j).move();
                }
                else
                    players.get(i).getMissiles().remove(j);
            }
        }
    }
    private void updateTank() {

        for (int i=0; i<players.size();i++)
            players.get(i).move();
    }

    private void checkCollisions() {

        for (int i = 0; i < players.size(); i++){

            // Sprawdzanie kolizji czołgu z innym czołgiem - wszystko na zasadzie prostokątów i ich krzyżowania się
            // Narazie nie ma

            // Sprawdzanie kolizji czołgów ze ścianami - tu jest wszystko dobrze i optymalnie

            for (int j = 0; j < walls.size(); j++){

                if (players.get(i).getBounds().intersects(walls.get(j).getBounds())) {
                    players.get(i).restorePreviousPosition();
                }
            }

            // Sprawdzanie kolizji pocisków z czołgami - tu jest wszystko dobrze i optymalnie

            // Narazie tylko dla dwóch czołgów - problem z iteracją

            if (players.size() != 1){

                if (i == 0){

                    for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                        if (players.get(i).getMissiles().get(j).getBounds().intersects(players.get(i + 1).getBounds())){

                            if (players.get(i + 1).getHp() != 1){

                                // tracenie życia

                                players.get(i + 1).setHp(players.get(i + 1).getHp() - players.get(i).getMissiles().get(j).getDamage());
                                players.get(i).getMissiles().get(j).setVisible(false);
                            }
                            else
                                players.remove(players.get(i + 1)); // usuwanie czołgu z mapy
                        }
                    }
                }
                else if (i == 1){

                    for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                        if (players.get(i).getMissiles().get(j).getBounds().intersects(players.get(i - 1).getBounds())){

                            if (players.get(i - 1).getHp() != 1){

                                players.get(i - 1).setHp(players.get(i - 1).getHp() - players.get(i).getMissiles().get(j).getDamage()); // tracenie życia
                                players.get(i).getMissiles().get(j).setVisible(false);
                            }
                            else
                                players.remove(players.get(i - 1)); // usuwanie czołgu z mapy
                        }
                    }
                }
            }


            // Sprawdzanie kolizji pocisków ze ścianami - tu jest wszystko dobrze i optymalnie

            for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                for (int k = 0; k < walls.size(); k++ ){

                    if (players.get(i).getMissiles().get(j).getBounds().intersects(walls.get(k).getBounds())){

                        players.get(i).getMissiles().get(j).setVisible(false);
                    }
                }

                if (players.get(i).getMissiles().get(j).getBounds().intersects(players.get(i).getBounds())){

                    players.get(i).getMissiles().get(j).setVisible(false);
                }
            }
        }
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

            checkCollisions();

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

        for (int i=0;i<players.size();i++)
            players.get(i).keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (int i=0;i<players.size();i++)
            players.get(i).keyReleased(e);
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }
}