/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener{

    private static final long serialVersionUID = -2697372672378215128L;

    // Rozmiar okna

    public static final int sizeX = 1366;
    public static final int sizeY = 768;

    // Panele 1) menu główne + poboczne 2) właściwa gra

    private MenuPanel menuPanel;
    private Game gamePanel;

    // przyciski do menu głównego

    private JLabel title;
    private JLabel author;
    private JButton startBtn;
    private JButton optionsBtn;
    private JButton statsBtn;
    private JButton helpBtn;
    private JButton exitBtn;

    // przyciski do menu pobocznych

    private JLabel playRoom;
    private JButton playBtn;
    private JButton signUpBtn;
    private JButton signInBtn;
    private JButton statusBtn;
    private JButton backBtn1;

    private JLabel registration;
    private JLabel loginWriteReg;
    private JTextField loginReg;
    private JLabel passwordWriteReg;
    private JPasswordField passwordReg;
    private JButton backBtn2;

    private JLabel logIn;
    private JLabel loginWriteLog;
    private JTextField loginLog;
    private JLabel passwordWriteLog;
    private JPasswordField passwordLog;
    private JButton backBtn3;

    private JLabel yourStatus;
    private JLabel statusInfo;
    private JButton backBtn4;

    private JLabel language;
    private JRadioButton choose1;
    private JRadioButton choose2;
    private JButton backBtn5;

    private JLabel rank;
    private JButton backBtn6;

    private JLabel helpText;
    private JButton backBtn7;

    // Boxy czyli odpowiednie sekcje w menu

    private Box boxMenu;
    private Box boxStartMenu;
    private Box boxSignUpMenu;
    private Box boxSignInMenu;
    private  Box boxStatusMenu;
    private  Box boxOptionsMenu;
    private Box boxStatsMenu;
    private Box boxHelpMenu;

    public GUI(){

        super("Tanks - Multiplayer by GK");
        setSize(sizeX, sizeY);
        setResizable(false);

        setLookAndFeel("Nimbus"); // wygląd przycisków
        init();



        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // Ustawienie ikonki aplikacji

        try {
            setIconImage(ImageIO.read(getClass().getResource("/main/resources/tank_icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        // Stworzenie odpowiednich boxów dla wszystkich sekcji

        boxMenu = createMenu();
        boxStartMenu = createStartMenu();
        boxSignUpMenu = createSignUpMenu();
        boxSignInMenu = createSignInMenu();
        boxStatusMenu = createStatusMenu();
        boxOptionsMenu = createOptionsMenu();
        boxStatsMenu = createStatsMenu();
        boxHelpMenu = createHelpMenu();

        // Dodanie akcji do buttonów

        startBtn.addActionListener(this);
        playBtn.addActionListener(this);
        signUpBtn.addActionListener(this);
        signInBtn.addActionListener(this);
        statusBtn.addActionListener(this);
        optionsBtn.addActionListener(this);
        statsBtn.addActionListener(this);
        helpBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        backBtn1.addActionListener(this);
        backBtn2.addActionListener(this);
        backBtn3.addActionListener(this);
        backBtn4.addActionListener(this);
        backBtn5.addActionListener(this);
        backBtn6.addActionListener(this);
        backBtn7.addActionListener(this);

        // Rozpoczęcie (menu główne)

        menuPanel = new MenuPanel();
        menuPanel.add(boxMenu);
        add(menuPanel);
    }

    // Ta metoda ustawia wygląd przycisków oraz innych elementów

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
        author.setForeground(new Color(214, 208, 101));
        box.add(author);

        // Tworzenie buttonów

        box.add(Box.createVerticalStrut(20));

        startBtn = new JButton("Start");
        startBtn.setForeground(Color.WHITE);
        startBtn.setBackground(Color.BLACK);
        startBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(startBtn);

        box.add(Box.createVerticalStrut(6));

        optionsBtn = new JButton("Options");
        optionsBtn.setForeground(Color.WHITE);
        optionsBtn.setBackground(Color.BLACK);
        optionsBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(optionsBtn);

        box.add(Box.createVerticalStrut(6));

        statsBtn = new JButton("Stats");
        statsBtn.setForeground(Color.WHITE);
        statsBtn.setBackground(Color.BLACK);
        statsBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(statsBtn);

        box.add(Box.createVerticalStrut(6));

        helpBtn = new JButton("Help");
        helpBtn.setForeground(Color.WHITE);
        helpBtn.setBackground(Color.BLACK);
        helpBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(helpBtn);

        box.add(Box.createVerticalStrut(6));

        exitBtn = new JButton("Exit");
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(Color.BLACK);
        exitBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(exitBtn);

        return box;
    }

    private Box createStartMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        playRoom = new JLabel("PLAYROOM");
        playRoom.setForeground(Color.WHITE);
        playRoom.setFont(new Font("Courier New", Font.BOLD, 75));
        box.add(playRoom);

        box.add(Box.createVerticalStrut(6));

        playBtn = new JButton("Play");
        playBtn.setForeground(Color.WHITE);
        playBtn.setBackground(Color.BLACK);
        playBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(playBtn);

        box.add(Box.createVerticalStrut(6));

        signUpBtn = new JButton("Sign up");
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setBackground(Color.BLACK);
        signUpBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(signUpBtn);

        box.add(Box.createVerticalStrut(6));
        signInBtn = new JButton("Sign in");
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setBackground(Color.BLACK);
        signInBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(signInBtn);

        box.add(Box.createVerticalStrut(6));
        statusBtn = new JButton("Status"); // status o tym czy jesteśmy zalogowani oraz jako kto
        statusBtn.setForeground(Color.WHITE);
        statusBtn.setBackground(Color.BLACK);
        statusBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(statusBtn);

        box.add(Box.createVerticalStrut(6));
        backBtn1 = backButton();
        box.add(backBtn1);

        return box;
    }

    private Box createSignUpMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        registration = new JLabel("REGISTRATION");
        registration.setForeground(Color.WHITE);
        registration.setFont(new Font("Courier New", Font.BOLD, 75));
        box.add(registration);

        box.add(Box.createVerticalStrut(10));
        loginWriteReg = new JLabel("Login:");
        loginWriteReg.setForeground(Color.WHITE);
        loginWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(loginWriteReg);

        box.add(Box.createVerticalStrut(0));
        loginReg = new JTextField();
        loginReg.setEditable(true);
        loginReg.setToolTipText("Image login just as you wish");
        loginReg.setMaximumSize(new Dimension(300, 30));

        // TODO Sprawdzanie czy nie ma już użytkownika o danym loginie, jeżeli tak to odpowiedni komunikat

        box.add(loginReg);

        box.add(Box.createVerticalStrut(15));
        passwordWriteReg = new JLabel("Password:");
        passwordWriteReg.setForeground(Color.WHITE);
        passwordWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(passwordWriteReg);

        box.add(Box.createVerticalStrut(0));
        passwordReg = new JPasswordField();
        passwordReg.setEditable(true);
        passwordReg.setToolTipText("Image your password - try choose not easy one");
        passwordReg.setMaximumSize(new Dimension(300, 30));
        box.add(passwordReg);

        box.add(Box.createVerticalStrut(10));
        JButton registerBtn = new JButton("Sign up");
        registerBtn.setForeground(Color.RED);
        registerBtn.setBackground(Color.BLACK);
        registerBtn.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        box.add(registerBtn);

        box.add(Box.createVerticalStrut(30));
        backBtn2 = backButton();
        box.add(backBtn2);

        return box;
    }

    private Box createSignInMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        logIn = new JLabel("LOG IN");
        logIn.setForeground(Color.WHITE);
        logIn.setFont(new Font("Courier New", Font.BOLD, 75));
        box.add(logIn);

        box.add(Box.createVerticalStrut(10));
        loginWriteLog = new JLabel("Login:");
        loginWriteLog.setForeground(Color.WHITE);
        loginWriteLog.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(loginWriteLog);

        box.add(Box.createVerticalStrut(0));
        loginLog = new JTextField();
        loginLog.setToolTipText("Write login of your account");
        loginLog.setMaximumSize(new Dimension(300, 30));
        box.add(loginLog);

        box.add(Box.createVerticalStrut(15));
        passwordWriteLog = new JLabel("Password:");
        passwordWriteLog.setForeground(Color.WHITE);
        passwordWriteLog.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(passwordWriteLog);

        box.add(Box.createVerticalStrut(0));
        passwordLog = new JPasswordField();
        passwordLog.setToolTipText("Write your password connected to your account");
        passwordLog.setMaximumSize(new Dimension(300, 30));
        box.add(passwordLog);

        box.add(Box.createVerticalStrut(10));
        JButton loginBtn = new JButton("Sign in");
        loginBtn.setForeground(Color.RED);
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        box.add(loginBtn);

        box.add(Box.createVerticalStrut(30));
        backBtn3 = backButton();
        box.add(backBtn3);

        return box;
    }

    private Box createStatusMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        yourStatus = new JLabel("YOUR STATUS");
        yourStatus.setForeground(Color.WHITE);
        yourStatus.setFont(new Font("Courier New", Font.BOLD, 75));
        box.add(yourStatus);

        box.add(Box.createVerticalStrut(15));

        statusInfo = new JLabel("You are ...");
        statusInfo.setForeground(Color.ORANGE);
        statusInfo.setFont(new Font("Courier New", Font.BOLD, 30));
        box.add(statusInfo);

        backBtn4 = backButton();
        box.add(backBtn4);

        return box;
    }

    private Box createOptionsMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        language = new JLabel("LANGUAGE");
        language.setForeground(Color.WHITE);
        language.setFont(new Font("Courier New", Font.BOLD, 75));
        box.add(language);

        box.add(Box.createVerticalStrut(6));

        // Stworzenie radioButtona do zaznaczania wyboru

        choose1 = new JRadioButton("English");
        choose1.setFont(new Font("Courier new", Font.BOLD, 40));
        choose1.setForeground(Color.ORANGE);
        choose1.setSelected(true); // domyślnie angielski na początku - można zmienić
        box.add(choose1);

        box.add(Box.createVerticalStrut(6));

        choose2 = new JRadioButton("Polish");
        choose2.setFont(new Font("Courier new", Font.BOLD, 40));
        choose2.setForeground(Color.ORANGE);
        box.add(choose2);

        // Grupowanie aby możliwe było zaznaczenie z wykluczeniem drugiego wyboru

        ButtonGroup group = new ButtonGroup();
        group.add(choose1);
        group.add(choose2);

        box.add(Box.createVerticalStrut(6));

        backBtn5 = backButton();
        box.add(backBtn5);

        return box;
    }

    private Box createStatsMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        rank = new JLabel("RANK");
        rank.setForeground(Color.WHITE);
        rank.setFont(new Font("Courier New", Font.BOLD, 75));
        box.add(rank);

        box.add(Box.createVerticalStrut(6));

        backBtn6 = backButton();
        box.add(backBtn6);

        return box;
    }

    private Box createHelpMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        helpText = new JLabel("<html>You are driving a tank. Your goal is to eliminate " +
                "other tanks.<br><br>1) Sign up entering your login and password" +
                "<br><br>2) After registration sign in" +
                "<br><br>3) Now you can join to the game<br><br>Your stats are remembered.<br><br>To move use:<br><br>W - up<br>A - left" +
                "<br>S - down<br>D - right" + "<br><br>L - Attack<br><br>Gl and Hf</html>");
        helpText.setFont(new Font("Courier New", Font.BOLD, 26));
        helpText.setForeground(Color.WHITE);
        box.add(helpText);

        box.add(Box.createVerticalStrut(6));

        backBtn7 = backButton();
        box.add(backBtn7);

        return box;
    }

    private JButton backButton(){
        JButton backBtn = new JButton("Back");
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.BLACK);
        backBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        return  backBtn;
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == startBtn){

            boxMenu.setVisible(false);
            menuPanel.remove(boxMenu);
            menuPanel.add(boxStartMenu);
            boxStartMenu.setVisible(true);
        }

        if (e.getSource() == playBtn){

            /* TODO Jeżeli niezalogowany to komunikat, że nie może wejść do gry i zostaje w menu. Jeśli zalogowany to wchodzi */

            boxStartMenu.setVisible(false);
            menuPanel.remove(boxStartMenu);
            menuPanel.setVisible(false);
            remove(menuPanel);

            gamePanel = new Game();
            add(gamePanel);
            gamePanel.requestFocusInWindow();
        }

        if (e.getSource() == signUpBtn){

            boxStartMenu.setVisible(false);
            menuPanel.remove(boxStartMenu);
            menuPanel.add(boxSignUpMenu);
            boxSignUpMenu.setVisible(true);

            // JOptionPane.showMessageDialog(null, "HELLO");
        }

        if (e.getSource() == backBtn2){

            boxSignUpMenu.setVisible(false);
            menuPanel.remove(boxSignUpMenu);
            menuPanel.add(boxStartMenu);
            boxStartMenu.setVisible(true);
        }

        if (e.getSource() == signInBtn){

            boxStartMenu.setVisible(false);
            menuPanel.remove(boxStartMenu);
            menuPanel.add(boxSignInMenu);
            boxSignInMenu.setVisible(true);
        }

        if (e.getSource() == backBtn3){

            boxSignInMenu.setVisible(false);
            menuPanel.remove(boxSignInMenu);
            menuPanel.add(boxStartMenu);
            boxStartMenu.setVisible(true);
        }

        if (e.getSource() == statusBtn){

            boxStartMenu.setVisible(false);
            menuPanel.remove(boxStartMenu);
            menuPanel.add(boxStatusMenu);
            boxStatusMenu.setVisible(true);
        }

        if (e.getSource() == backBtn4){

            boxStatusMenu.setVisible(false);
            menuPanel.remove(boxStatusMenu);
            menuPanel.add(boxStartMenu);
            boxStartMenu.setVisible(true);
        }

        if (e.getSource() == backBtn1){
            boxStartMenu.setVisible(false);
            menuPanel.remove(boxStartMenu);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);
        }

        if (e.getSource() == optionsBtn){

            boxMenu.setVisible(false);
            menuPanel.remove(boxMenu);
            menuPanel.add(boxOptionsMenu);
            boxOptionsMenu.setVisible(true);
        }

        if (e.getSource() == backBtn5){

            boxOptionsMenu.setVisible(false);
            menuPanel.remove(boxOptionsMenu);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);
        }

        if (e.getSource() == statsBtn){

            boxMenu.setVisible(false);
            menuPanel.remove(boxMenu);
            menuPanel.add(boxStatsMenu);
            boxStatsMenu.setVisible(true);
        }

        if (e.getSource() == backBtn6){

            boxStatsMenu.setVisible(false);
            menuPanel.remove(boxStatsMenu);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);
        }

        if (e.getSource() == helpBtn){

            boxMenu.setVisible(false);
            menuPanel.remove(boxMenu);
            menuPanel.add(boxHelpMenu);
            boxHelpMenu.setVisible(true);
        }

        if (e.getSource() == backBtn7){

            boxHelpMenu.setVisible(false);
            menuPanel.remove(boxHelpMenu);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);
        }

        if (e.getSource() == exitBtn){
            System.exit(0);
        }
    }
}