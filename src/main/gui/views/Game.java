/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import main.io.MapReader;
import main.models.Player;
import main.models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements Runnable, KeyListener{

    private final int DELAY = 6;
    private Thread animation = null;

    private int spaceWallWidth = 0;
    private int spaceWallHeight = 0;

    private boolean initialized = false;

    private final int SPACE = 24; // rozmiar ściany - obrazka

    private ArrayList<Wall> walls = null;
    private ArrayList<Player> players = null;

    private MapReader map = null;

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

        /*
        Zczytanie mapy oraz dodanie ścian do listy
         */

        map = new MapReader("mapDeathMatch.txt");
        walls = new ArrayList<>();

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

        /*
        Stworzenie graczy
         */

        players = new ArrayList<>();

        /*
        2 graczy
         */

        players.add(new Player());
        players.add(new Player());

        boolean isIntersection;

        // Losowe generowanie na mapie czołgów

        for (int i=0;i<players.size();i++){

            while (!players.get(i).isRandomCreated()){

                isIntersection = false;

                players.get(i).setX(new Random().nextInt(1330 - players.get(i).getWidth()));
                players.get(i).setY(new Random().nextInt(740 - players.get(i).getHeight()));
                System.out.println(players.get(i).getX());
                System.out.println(players.get(i).getY());

                for (int j=0;j<walls.size();j++){

                    if (players.get(i).getBounds().intersects(walls.get(j).getBounds())){
                        isIntersection = true;
                        break;
                    }
                }

                if (!isIntersection){
                    players.get(i).setRandomCreated(true);
                }
            }
        }

        initialized = true;
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

        /* Narazie może wygrać tylko pierwszy czołg - w innym przypadku program się zawiesza z powodu
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

        if (initialized){

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

            for (int i=0;i<players.size();i++){

                players.get(i).updateTank();
                players.get(i).updateMissiles();
            }

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

        for (int i=0;i<players.size();i++){
            players.get(i).keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        for (int i=0;i<players.size();i++){
            players.get(i).keyReleased(e);
        }
    }
}