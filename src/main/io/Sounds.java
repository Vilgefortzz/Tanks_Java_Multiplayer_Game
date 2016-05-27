/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.io;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class Sounds {

    private final URL res = getClass().getResource("/music/fire.wav");

    public void playFireSound() {

        new Thread(() -> {

            new JFXPanel();
            Media sound = new Media(res.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

        }).start();
    }
}