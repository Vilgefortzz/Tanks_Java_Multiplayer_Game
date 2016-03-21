/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import main.io.MapReader;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel{

    private ControlingPanel controlingPanel;
    private MapReader map;
    private int spaceWidth = 0;
    private int spaceHeight = 0;

    public MapPanel() {
        init();
    }

    private void init(){
        map = new MapReader("mapDeathMatch.txt");
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        drawMap(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMap(Graphics g){

        String [][] fields = map.getFieldsArray();

        for (int i=0;i<15;i++){
            for (int j=0;j<27;j++){
                if ("W".equals(fields[i][j])){
                    g.drawImage(new ImageIcon(getClass().getResource("/main/resources/sprites/walls/wall.png")).getImage(), spaceWidth, spaceHeight, this);
                    System.out.println("Wchodze");
                }
                else if ("G".equals(fields[i][j])){
                    g.drawImage(new ImageIcon(getClass().getResource("/main/resources/sprites/ground/ground.png")).getImage(), spaceWidth, spaceHeight, this);
                    System.out.println("Wchodze");
                }
                spaceWidth += 50;
            }
            spaceWidth = 0;
            spaceHeight += 50;
        }
    }
}
