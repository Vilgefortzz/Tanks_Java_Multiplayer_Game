/*
 * Copyright (c) 2016.
 * @gklimek
 */

package gui;

import io.MapReader;
import models.Player;
import models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import static io.LoadImages.explosion;
import static io.LoadImages.heart;
import static io.LoadImages.skull;

public class GamePanel extends JPanel implements Runnable, KeyListener{

    private final int DELAY = 6;
    private Thread animation = null;

    private int spaceWallWidth = 0;
    private int spaceWallHeight = 0;

    private final int SPACE = 24; // rozmiar ściany - obrazka

    public static ArrayList<Wall> walls = null; // statyczna lista ścian - potrzebna przy badaniu kolizji

    private Map<Integer, Player> players; // lista playerów w postaci mapy < klucz , obiekt >

    private MapReader map = null;

    public GamePanel() {

        addKeyListener(this);
        setFocusable(true);

        initWorld();

        setBackground(new Color(113, 99, 96));
        setDoubleBuffered(true);
    }

    private void initWorld() {

        /*
        Stworzenie mapy oraz ścian
         */

        map = new MapReader("mapDeathMatch.txt");
        walls = new ArrayList<>();

        /*
        Stworzenie graczy
         */

        players = new HashMap<>();

        /*
        Wygenerowanie mapy
         */

        generateMap();

        /*
        1 gracz
         */

        players.put(1, new Player(1, "Grzes"));

        /*
        Startowanie wątku animacji
         */

        animation = new Thread(this);
        animation.start();
    }

    private void generateMap(){

        ArrayList<String> lines = map.getLines();

        for (String line : lines) {
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);

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

        g2d.drawImage(heart, 0, 0, this);
        g2d.setColor(new Color(182, 14, 14));
        g2d.setFont(new Font("Arial", Font.BOLD, 17));

        g2d.drawString(String.valueOf(players.get(1).getHp()), 24, 16);

        g2d.drawImage(explosion, 60, 0, this);
        // TODO Wyświetlanie zniszczonych czołgów
        g2d.drawImage(skull, 120, 0, this);
        // TODO Wyświetlanie ilości respawnów
    }

    private void paintWorld(Graphics2D g2d) {

        // Ścian nie można niszczyć, więc przy tworzeniu są one zawsze widoczne

        for (Wall wall : walls) {

            wall.draw(g2d);
        }

        // Obiekty, które mogą być niszczone - czołgi(gracze)

            for ( Player player : players.values() ){

                player.draw(g2d);

                // Rysowanie pocisków, jeżeli gracz żyje to rysuje

                for (int i = 0; i < player.getMissiles().size(); i++) {

                    if (player.getMissiles().get(i).isVisible()) {

                        player.getMissiles().get(i).draw(g2d);
                    }
                }
            }
    }

    /*private void checkCollisions() {

        for (int i = 0; i < players.size(); i++){

            // Sprawdzanie kolizji czołgu z innym czołgiem - wszystko na zasadzie prostokątów i ich krzyżowania się
            // Narazie nie ma

            // Sprawdzanie kolizji pocisków z czołgami - tu jest wszystko dobrze i optymalnie

            // Narazie tylko dla dwóch czołgów - problem z iteracją

            if (players.size() != 1) {

                if (i == 0) {

                    for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                        if (players.get(i).getMissiles().get(j).getBounds().intersects(players.get(i + 1).getBounds())) {

                            if (players.get(i + 1).getHp() != 1) {

                                // tracenie życia

                                players.get(i + 1).setHp(players.get(i + 1).getHp() - players.get(i).getMissiles().get(j).getDamage());
                                players.get(i).getMissiles().get(j).setVisible(false);
                            } else
                                players.remove(players.get(i + 1)); // usuwanie czołgu z mapy
                        }
                    }
                } else if (i == 1) {

                    for (int j = 0; j < players.get(i).getMissiles().size(); j++) {

                        if (players.get(i).getMissiles().get(j).getBounds().intersects(players.get(i - 1).getBounds())) {

                            if (players.get(i - 1).getHp() != 1) {

                                players.get(i - 1).setHp(players.get(i - 1).getHp() - players.get(i).getMissiles().get(j).getDamage()); // tracenie życia
                                players.get(i).getMissiles().get(j).setVisible(false);
                            } else
                                players.remove(players.get(i - 1)); // usuwanie czołgu z mapy
                        }
                    }
                }
            }
        }
    }
    */

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            for (Player player : players.values()) {

                player.updateTank();
                player.updateMissiles();
                player.checkCollisionWithWall();
                player.checkCollisionMissileWithWall();
            }

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

        for (Player player : players.values()){

            player.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        for (Player player : players.values()){

            player.keyReleased(e);
        }
    }
}