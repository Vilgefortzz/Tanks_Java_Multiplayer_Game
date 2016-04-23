/*
 * Copyright (c) 2016.
 * @gklimek
 */

package io;

import javax.swing.*;
import java.awt.*;

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

    public static Image tankUp = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Up.png").getImage();
    public static Image tankDown = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Down.png").getImage();
    public static Image tankRight = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Right.png").getImage();
    public static Image tankLeft = new ImageIcon("res\\sprites\\tanks\\team_grey\\Tank_Left.png").getImage();

    /*
    Missiles directions
     */

    public static Image missileUp = new ImageIcon("res\\sprites\\missiles\\Missile_Up.gif").getImage();
    public static Image missileDown = new ImageIcon("res\\sprites\\missiles\\Missile_Down.gif").getImage();
    public static Image missileRight = new ImageIcon("res\\sprites\\missiles\\Missile_Right.gif").getImage();
    public static Image missileLeft = new ImageIcon("res\\sprites\\missiles\\Missile_Left.gif").getImage();

    /*
    Wall
     */

    public static Image wall = new ImageIcon("res\\sprites\\walls\\brick.png").getImage();
}