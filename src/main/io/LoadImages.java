/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LoadImages {

    /*
    Top bar into a game
     */

    public static Image heart = new ImageIcon("res\\heart.png").getImage();
    public static Image explosion = new ImageIcon("res\\explosion.png").getImage();
    public static Image skull = new ImageIcon("res\\skull.png").getImage();

    /*
    Tank directions
     */

    private static Image tankUp = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Up.png").getImage();
    private static Image tankDown = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Down.png").getImage();
    public static Image tankRight = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Right.png").getImage();
    private static Image tankLeft = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Left.png").getImage();

    public static Map<Integer, Image> tankOrientationMap = new HashMap<>(); // Mapa < orientacja czołgu, obrazek odpowiedni >

    /*
    Missiles directions
     */

    private static Image missileUp = new ImageIcon("res\\sprites\\missiles\\Missile_Up.gif").getImage();
    private static Image missileDown = new ImageIcon("res\\sprites\\missiles\\Missile_Down.gif").getImage();
    private static Image missileRight = new ImageIcon("res\\sprites\\missiles\\Missile_Right.gif").getImage();
    private static Image missileLeft = new ImageIcon("res\\sprites\\missiles\\Missile_Left.gif").getImage();

    public static Map<Integer, Image> missileOrientationMap = new HashMap<>(); // Mapa < orientacja czołgu, obrazek odpowiedni >

    /*
    Wall
     */

    public static Image wall = new ImageIcon("res\\sprites\\walls\\brick.png").getImage();

    /*
    Skull inform about death
     */

    public static Image death_inform = new ImageIcon("res\\death_inform.png").getImage();


    public static void createTankOrientationMap(){

        tankOrientationMap.put(1, tankLeft);
        tankOrientationMap.put(2, tankUp);
        tankOrientationMap.put(3, tankRight);
        tankOrientationMap.put(4, tankDown);
    }

    public static void createMissileOrientationMap(){

        missileOrientationMap.put(1, missileLeft);
        missileOrientationMap.put(2, missileUp);
        missileOrientationMap.put(3, missileRight);
        missileOrientationMap.put(4, missileDown);
    }
}