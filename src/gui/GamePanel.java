/*
 * Copyright (c) 2016.
 * @gklimek
 */

package gui;

import io.KeyInput;
import io.MapReader;
import models.Player;
import models.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static client.Client.myPlayer;
import static client.Client.sendYourCollisionTankWithWall;
import static gui.MainFrame.yourLogin;
import static io.LoadImages.*;

public class GamePanel extends JPanel{

    public Thread gameLoop = null;
    private boolean looping = false;
    private BufferedImage buffer = null;

    private final int SPACE = 24; // rozmiar ściany - obrazka

    public static List<Wall> walls = null; // statyczna lista ścian - potrzebna przy badaniu kolizji
    private MapReader map = null;

    private Map<Integer, Player> players = null; // lista playerów w postaci mapy < klucz , obiekt >

    private KeyInput keyboard = null;
    private MainFrame frame = null;

    public GamePanel(MainFrame frame) {

        this.frame = frame;

        keyboard = new KeyInput();
        addKeyListener(keyboard);

        setIgnoreRepaint(false);
        setFocusable(true);

        generateMap();
        players = new HashMap<>();

        createTankOrientationMap();
        createMissileOrientationMap();
    }

    public KeyInput getKeyboard() {
        return keyboard;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    private void generateMap(){

        // Zczytanie z pliku tekstowego mapy
        map = new MapReader("mapDeathMatch.txt");
        walls = new ArrayList<>();

        // Stworzenie obiektów na podstawie mapy
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

    private void initialize(){

        // Stworzenie bufora w celu wydajniejszego rysowania
        buffer = new BufferedImage(1366,768,BufferedImage.TYPE_INT_RGB);
    }

    private void update(){

        // Sprawdzanie naciśniętych klawiszy
        keyboard.update();
        // Ruch w konkretną stronę lub strzał - powiadomienie servera
        myPlayer.tankMovement();

        // Ruch pocisków
        players.values().forEach(Player::updateMissiles);
    }

    private void checkCollisions(){

        // Sprawdzanie kolizji czołgu(mojego gracza) ze ścianami oraz powiadomienie innych w przypadku kolizji
        if (players.get(myPlayer.getId()) != null && players.get(myPlayer.getId()).checkCollisionWithWall()){
            sendYourCollisionTankWithWall(myPlayer.getId());
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
    }

    private void drawBuffer(){

        Graphics2D g2d = buffer.createGraphics();

        g2d.setColor(new Color(114, 127, 119));
        g2d.fillRect(0,0,1366,768);

        drawMenuBar(g2d);

        // Rysowanie ścian
        for (Wall wall : walls) {
            wall.draw(g2d);
        }

        // Rysowanie playerów
        for (Player player : players.values()) {
            player.draw(g2d);
        }

        // Rysowanie pocisków
        for (Player player : players.values()) {
            for (int i = 0; i < player.getMissiles().size(); i++) {
                if (player.getMissiles().get(i).isVisible()) {
                    player.getMissiles().get(i).draw(g2d);
                }
            }
        }

        g2d.dispose();
    }

    private void drawScreen(){

        Graphics2D g2d = (Graphics2D)this.getGraphics();

        g2d.drawImage(buffer,0,0,this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
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

    public void runGameLoopThread(){

        gameLoop = new Thread(() -> {

            // Inicjalizacja bufora do rysowania
            initialize();

            // to sie stanie raz po uruchomieniu
            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();
            final double ns = 1000000000.0 / 60.0;
            double delta = 0;
            int frames = 0;
            int updates = 0;

            while (looping) {

                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {

                    update(); // logika
                    checkCollisions(); // kolizje
                    updates++;
                    delta--;
                }
                drawBuffer(); // wyswietlanie
                drawScreen();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {

                    timer += 1000;
                    frame.setTitle("Client logged as: " + yourLogin + "  |  " + updates + " ups, " + frames + " fps");
                    updates = 0;
                    frames = 0;
                }
            }
        });

        looping = true;
        gameLoop.start();
    }

    public void registerPlayer(int id, int orientation, int x, int y){

        if (players.containsKey(id)){
            return;
        }

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

            myPlayer.setHp(100); // tylko hp wystarczy
            myPlayer.setDeaths(myPlayer.getDeaths() + 1);
        }
    }

    public void destroyedByPlayer(int id){

        if (players.get(id).getId() == myPlayer.getId())
            myPlayer.setDestroyed(myPlayer.getDestroyed() + 1);
    }

    public void cleanGamePanel(){

        players.clear();
    }

    /*
    Collisions
     */

    public void collisionTankWithWall(int id){

        players.get(id).restorePreviousPosition();
    }
}