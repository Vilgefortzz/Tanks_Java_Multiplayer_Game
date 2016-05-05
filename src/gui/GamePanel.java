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
import java.util.*;

import static client.Client.myPlayer;
import static client.Client.sendYourCollisionTankWithWall;
import static client.Client.sendYourRespawn;
import static io.LoadImages.*;

public class GamePanel extends JPanel{

    private Thread repainting = null;
    private boolean animating = false;

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

    public KeyInput getKeyboard() {
        return keyboard;
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
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

    public void runRepaintingThread(){

        repainting = new Thread(() -> {

            while (animating) {
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        animating = true;
        repainting.start();
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
        g2d.drawString(String.valueOf(myPlayer.getHp()), 24, 16);

        g2d.drawImage(explosion, 60, 0, this);

        g2d.setColor(new Color(210, 191, 37));
        g2d.setFont(new Font("Arial", Font.BOLD, 17));
        g2d.drawString(String.valueOf(myPlayer.getDestroyed()), 84, 16);

        g2d.drawImage(skull, 120, 0, this);

        g2d.setColor(new Color(0,0,0));
        g2d.setFont(new Font("Arial", Font.BOLD, 17));
        g2d.drawString(String.valueOf(myPlayer.getDeaths()), 144, 16);
    }

    private void paintWorld(Graphics2D g2d) {

        // Rysowanie ścian
        for (Wall wall : walls) {
            wall.draw(g2d);
        }

        // Sprawdzanie kolizji czołgu ze ścianami oraz powiadomienie innych w przypadku kolizji
        if (myPlayer.checkCollisionWithWall()) {
            sendYourCollisionTankWithWall(myPlayer.getId());
        }

        // Rysowanie playerów
        for (Player player : players.values()) {
            player.draw(g2d);
        }

        // Sprawdzanie kolizji pocisków ze ścianami
        for (Wall wall : walls){
            for (Player player : players.values()){
                for (int i = 0; i < player.getMissiles().size(); i++) {
                    player.getMissiles().get(i).hitWall(wall);
                }
            }
        }

        // Sprawdzanie kolizji pocisków z czołgami oraz zadawanie obrażeń jeżeli jest kolizja
        for (Player player : players.values()) {
            for (int i = 0; i < player.getMissiles().size(); i++) {
                player.getMissiles().get(i).hitPlayers(players);
            }
        }

        // Srawdzanie czy gracz żyje, jeżeli nie to respawn oraz powiadomienie innych o nowej pozycji gracza
        if (myPlayer.getHp() == 0){
            Player tempPlayer = new Player(myPlayer.getId());
            sendYourRespawn(tempPlayer.getId(), tempPlayer.getX(), tempPlayer.getY());
        }

        // Rysowanie pocisków oraz aktualizowanie ich pozycji
        for (Player player : players.values()) {
            for (int i = 0; i < player.getMissiles().size(); i++) {
                if (player.getMissiles().get(i).isVisible()) {
                    player.getMissiles().get(i).draw(g2d);
                }
            }

            player.updateMissiles();
        }
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

        if (players.get(id).getId() == myPlayer.getId()){

            myPlayer.setMainImage(tankOrientationMap.get(orientation));
            myPlayer.getImageDimensions();
            myPlayer.setX(players.get(id).getX());
            myPlayer.setY(players.get(id).getY());
        }
    }

    public void firePlayer(int id, int orientation){

        if (orientation == 1)
            players.get(id).shootLeft();
        else if (orientation == 2)
            players.get(id).shootUP();
        else if (orientation == 3)
            players.get(id).shootRight();
        else
            players.get(id).shootDown();
    }

    public void respawnPlayer(int id, int x, int y){

        players.get(id).respawn(x, y);

        if (players.get(id).getId() == myPlayer.getId()){
            myPlayer.respawn(x, y);
            myPlayer.setDeaths(myPlayer.getDeaths() + 1);
        }
    }

    public void deletePlayers(){

        players.clear();
    }

    /*
    Collisions
     */

    public void collisionTankWithWall(int id){

        players.get(id).restorePreviousPosition();
    }
}