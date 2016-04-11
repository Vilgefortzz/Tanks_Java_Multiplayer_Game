/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui;

public interface FrontEnd {

    boolean registerPlayer( int id, String name );

    void deregisterPlayer( int id );

    void movePlayer( int id, int x, int y );

    void clearPlayers();
}
