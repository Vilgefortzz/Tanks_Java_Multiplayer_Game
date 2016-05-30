/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Images {

    /*
    Top bar into a game
     */

    public static BufferedImage heart;
    public static BufferedImage explosion;
    public static BufferedImage skull;

    /*
    Tank directions
     */

    private static BufferedImage tankUp;
    private static BufferedImage tankDown;
    public static BufferedImage tankRight;
    private static BufferedImage tankLeft;

    public static Map<Integer, BufferedImage> tankOrientationMap; // Mapa < orientacja czołgu, obrazek odpowiedni >

    /*
    Bullets directions
     */

    private static BufferedImage bulletUp;
    private static BufferedImage bulletDown;
    private static BufferedImage bulletRight;
    private static BufferedImage bulletLeft;

    public static Map<Integer, BufferedImage> bulletOrientationMap; // Mapa < orientacja czołgu, obrazek odpowiedni >

    /*
    Wall
     */

    public static BufferedImage wall;

    /*
    Skull inform about death
     */

    public static BufferedImage death_inform;


    public void loadAllImages() throws IOException {

        heart = ImageIO.read(getClass().getResource("/heart.png"));
        explosion = ImageIO.read(getClass().getResource("/explosion.png"));
        skull = ImageIO.read(getClass().getResource("/skull.png"));

        tankUp = ImageIO.read(getClass().getResource("/sprites/tanks/team_grey/Tank_Up.png"));
        tankDown = ImageIO.read(getClass().getResource("/sprites/tanks/team_grey/Tank_Down.png"));
        tankRight = ImageIO.read(getClass().getResource("/sprites/tanks/team_grey/Tank_Right.png"));
        tankLeft = ImageIO.read(getClass().getResource("/sprites/tanks/team_grey/Tank_Left.png"));

        bulletUp = ImageIO.read(getClass().getResource("/sprites/bullets/Bullet_Up.gif"));
        bulletDown = ImageIO.read(getClass().getResource("/sprites/bullets/Bullet_Down.gif"));
        bulletRight = ImageIO.read(getClass().getResource("/sprites/bullets/Bullet_Right.gif"));
        bulletLeft = ImageIO.read(getClass().getResource("/sprites/bullets/Bullet_Left.gif"));

        wall = ImageIO.read(getClass().getResource("/sprites/walls/brick.png"));

        death_inform = ImageIO.read(getClass().getResource("/death_inform.png"));
    }

    public static void createTankOrientationMap(){

        tankOrientationMap = new HashMap<>(); // Mapa < orientacja czołgu, obrazek odpowiedni >

        tankOrientationMap.put(1, tankLeft);
        tankOrientationMap.put(2, tankUp);
        tankOrientationMap.put(3, tankRight);
        tankOrientationMap.put(4, tankDown);
    }

    public static void createBulletOrientationMap(){

        bulletOrientationMap = new HashMap<>(); // Mapa < orientacja czołgu, obrazek odpowiedni >

        bulletOrientationMap.put(1, bulletLeft);
        bulletOrientationMap.put(2, bulletUp);
        bulletOrientationMap.put(3, bulletRight);
        bulletOrientationMap.put(4, bulletDown);
    }
}