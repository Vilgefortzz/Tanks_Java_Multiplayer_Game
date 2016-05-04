/*
 * Copyright (c) 2016.
 * @gklimek
 */

package gui;

import client.Client;
import io.KeyInput;
import io.MapReader;
import models.Player;
import models.Wall;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

import static client.Client.sendYourCollisionTankWithWall;
import static io.LoadImages.*;

public class GamePanel extends JPanel implements Runnable{

    private final int DELAY = 8;
    private Thread animation = null;
    private boolean animating;

    private final int SPACE = 24; // rozmiar ściany - obrazka

    public static ArrayList<Wall> walls = null; // statyczna lista ścian - potrzebna przy badaniu kolizji
    private MapReader map = null;

    private Map<Integer, Player> players = null; // lista playerów w postaci mapy < klucz , obiekt >

    private MainFrame frame = null;
    private MenuPanel menuPanel = null;
    private KeyInput keyboard = null;

    private JButton backToPlayroom = null;


    public GamePanel(MainFrame frame, MenuPanel menuPanel) {

        this.frame = frame;
        this.menuPanel = menuPanel;
        keyboard = new KeyInput();

        addKeyListener(keyboard);
        setFocusable(true);

        initWorld();
        makeButton();

        setBackground(new Color(113, 99, 96));
        setDoubleBuffered(true);
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
    }

    public KeyInput getKeyboard() {
        return keyboard;
    }

    private void initWorld() {

        map = new MapReader("mapDeathMatch.txt");
        walls = new ArrayList<>();
        players = new HashMap<>();

        createTankOrientationMap();
        createMissileOrientationMap();

        generateMap();
    }

    private void makeButton(){

        backToPlayroom = new JButton("Go to the playroom");
        backToPlayroom.setForeground(Color.WHITE);
        backToPlayroom.setBackground(Color.BLACK);
        backToPlayroom.setFont(new Font("Arial", Font.BOLD, 10));

        add(backToPlayroom, BorderLayout.NORTH);

        backToPlayroom.addActionListener(e -> SwingUtilities.invokeLater(() -> {

            Client klient = frame.getClient();
            klient.disconnect();
        }));
    }

    private void generateMap(){

        ArrayList<String> lines = map.getLines();
        int spaceWallWidth = 0;
        int spaceWallHeight = 0;

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

        //g2d.drawString(String.valueOf(players.get(1).getHp()), 24, 16);

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

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (animating) {

            for (Player player : players.values()){

                player.updateMissiles();

                if (player.checkCollisionWithWall()){
                    sendYourCollisionTankWithWall(player.getId());
                }

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

    public void runAnimationThread(){

        /*
        Startowanie wątku animacji
         */

        animation = new Thread(this);
        animation.start();
    }

    public void registerPlayer(int id, int orientation, int x, int y){

        if (players.containsKey(id)){
            return;
        }

        System.out.println("player X: " + x);
        System.out.println("player Y: " + y);

        players.put(id, new Player(id, orientation, x, y));
    }

    public void unRegisterPlayer(int id){

        players.remove(id);
    }

    public void movePlayer(int id, int orientation, int dx, int dy){

        players.get(id).setMainImage(tankOrientationMap.get(orientation));
        players.get(id).getImageDimensions();
        players.get(id).setDx(dx);
        players.get(id).setDy(dy);
        players.get(id).updateMovement();
    }

    public void playerFire(int id, int orientation){

        if (orientation == 1)
            players.get(id).shootLeft();
        else if (orientation == 2)
            players.get(id).shootUP();
        else if (orientation == 3)
            players.get(id).shootRight();
        else
            players.get(id).shootDown();
    }

    /*
    Collisions
     */

    public void collisionTankWithWall(int id){

        players.get(id).restorePreviousPosition();
    }

    public void deletePlayers(){

        players.clear();
    }
}