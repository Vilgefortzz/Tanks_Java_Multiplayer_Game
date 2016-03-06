/*
 * Copyright (c) 2016.
 * Made by Grzegorz Klimek
 */

package main.gui.views;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame {

    private static int WINDOW_WIDTH = 1366;
    private static int WINDOW_HEIGHT = 768;

    private MenuPanel menuPanel;

    private JLabel title;
    private JLabel author;
    private JButton startGameBtn;
    private JButton optionsBtn;
    private JButton helpBtn;
    private JButton exitBtn;
    private JButton backBtn; // przycisk wstecz

    public GUI(){
        super("Tanks - Multiplayer by GK");
        setLookAndFeel("Nimbus"); // wygląd przycisków
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        // Ustawienie ikonki aplikacji

        try {
            setIconImage(ImageIO.read(getClass().getResource("/main/resources/tank.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        init();
    }

    private void init() {
        menuPanel = new MenuPanel();
        add(menuPanel);

        Box boxMenu = createMenu();
        menuPanel.add(boxMenu);

        // Akcje dla przycisków

        createActions(boxMenu);

    }

    // Funkcja ustawia wygląd przycisków oraz innych elementów

    private void setLookAndFeel(String lookAndFeel) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (lookAndFeel.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Box createMenu() {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20)); // przerwa między elementami w boxie

        // Stworzenie napisu głównego

        title = new JLabel("TANKS - MULTIPLAYER");
        title.setFont(new Font("Courier New", Font.BOLD, 75));
        title.setForeground(Color.WHITE);
        box.add(title);

        // Dodanie autora

        box.add(Box.createVerticalStrut(-16));

        author = new JLabel("Made by Grzegorz Klimek");
        author.setFont(new Font("Helvetica", Font.BOLD, 9));
        author.setForeground(Color.BLACK);
        box.add(author);

        // Tworzenie buttonów

        box.add(Box.createVerticalStrut(20));

        startGameBtn = new JButton("Start game");
        startGameBtn.setForeground(Color.WHITE);
        startGameBtn.setBackground(Color.BLACK);
        box.add(startGameBtn);

        box.add(Box.createVerticalStrut(6));

        optionsBtn = new JButton("Options");
        optionsBtn.setForeground(Color.WHITE);
        optionsBtn.setBackground(Color.BLACK);
        box.add(optionsBtn);

        box.add(Box.createVerticalStrut(6));

        helpBtn = new JButton("Help");
        helpBtn.setForeground(Color.WHITE);
        helpBtn.setBackground(Color.BLACK);
        box.add(helpBtn);

        box.add(Box.createVerticalStrut(6));

        exitBtn = new JButton("Exit");
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(Color.BLACK);
        box.add(exitBtn);

        return box;
    }

    private Box createOptionsMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        JLabel language = new JLabel("Language");
        language.setForeground(Color.ORANGE);
        language.setFont(new Font("Courier New", Font.ITALIC, 75));
        box.add(language);

        box.add(Box.createVerticalStrut(6));

        // Stworzenie radioButtona do zaznaczania wyboru

        JRadioButton choose1 = new JRadioButton("Polish");
        choose1.setFont(new Font("Courier New", Font.ITALIC, 45));
        box.add(choose1);

        box.add(Box.createVerticalStrut(6));

        JRadioButton choose2 = new JRadioButton("English");
        choose2.setFont(new Font("Courier New", Font.ITALIC, 45));
        box.add(choose2);

        // Grupowanie aby możliwe było zaznaczenie z wykluczeniem drugiego wyboru

        ButtonGroup group = new ButtonGroup();
        group.add(choose1);
        group.add(choose2);

        // Stworzenie przycisku wstecz

        box.add(Box.createVerticalStrut(6));

        backBtn = backButton();
        box.add(backBtn);

        return box;
    }

    private Box createHelpMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        JLabel helpText = new JLabel("<html>You are driving a tank. Your goal is to eliminate " +
                "other tanks.<br><br>1) Sign up entering your login and password" +
                "<br><br>2) After registration sign in" +
                "<br><br>3) Now you can join to the game<br><br>Your stats are remembered.<br><br>To move use:<br>W - up<br>A - left" +
                "<br>D - right<br>S - down" + "<br>Space - Attack<br><br>Gl and Hf</html>");
        helpText.setFont(new Font("Courier New", Font.ITALIC, 26));
        helpText.setForeground(Color.WHITE);
        box.add(helpText);

        box.add(Box.createVerticalStrut(6));

        backBtn = backButton();
        box.add(backBtn);

        return box;
    }

    private void createActions(Box box){

        startGameBtn.addActionListener(e -> {
            box.setVisible(false);
            // TODO Rejestracja graczy + już ta właściwa częśc czyli wygenerowanie planszy, grywalność itp.
        });

        optionsBtn.addActionListener(e -> {
            box.setVisible(false);
            menuPanel.remove(box);
            Box boxOptionsMenu = createOptionsMenu();
            menuPanel.add(boxOptionsMenu);

            backBtn.addActionListener(e1 -> {
                boxOptionsMenu.setVisible(false);
                menuPanel.remove(boxOptionsMenu);
                menuPanel.add(box);
                box.setVisible(true);
            });
        });

        helpBtn.addActionListener(e -> {
            box.setVisible(false);
            menuPanel.remove(box);
            Box boxHelpMenu = createHelpMenu();
            menuPanel.add(boxHelpMenu);

            backBtn.addActionListener(e1 -> {
                boxHelpMenu.setVisible(false);
                menuPanel.remove(boxHelpMenu);
                menuPanel.add(box);
                box.setVisible(true);
            });
        });

        exitBtn.addActionListener(e -> dispose());
    }

    private JButton backButton(){
        JButton backBtn = new JButton("Back");
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.BLACK);
        return  backBtn;
    }

}

