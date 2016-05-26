/*
 * Copyright (c) 2016.
 * @gklimek
 */

package main.gui;

import main.client.Client;
import main.regexes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static main.client.Client.database;
import static main.logs.Logs.log;
import static main.utilities.Utilities.passwordHashing;

public class MainFrame extends JFrame implements ActionListener{

    // Rozmiar okna

    public static final int sizeX = 1366;
    public static final int sizeY = 768;

    // Klient, który posiada to okno

    private Client client = null;

    public static String yourLogin;

    // Panele 1) menu główne + poboczne 2) właściwa gra

    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    // przyciski do menu głównego (widok dla niezalogowanego użytkownika)

    private JLabel title;
    private JLabel author;
    private JButton signInBtn;
    private JButton creditsBtn;
    private JButton exitBtn;

    // przyciski do menu pobocznego (logowanie)

    private JLabel logIn;
    private JLabel loginWriteLog;
    private JLabel passwordWriteLog;
    private JButton loginBtn;
    private JLabel registrationInfo;
    private JButton goToRegistration;
    private JButton backBtn1;

    private JTextField loginLog;
    private JPasswordField passwordLog;

    // przyciski do menu pobocznego (rejestracja)

    private JLabel registration;
    private JLabel loginWriteReg;
    private JLabel passwordWriteReg;
    private JLabel firstNameWriteReg;
    private JLabel lastNameWriteReg;
    private JLabel emailWriteReg;
    private JButton createAccountBtn;
    private JButton backBtn2;

    private JTextField loginReg;
    private JPasswordField passwordReg;
    private JTextField firstNameReg;
    private JTextField lastNameReg;
    private JTextField emailReg;

    private JProgressBar passwordStrength;
    private JLabel textPasswdStrength;
    private JLabel passwordStrenghtInfo;

    // przyciski do menu pobocznego (zalogowany użytkownik)

    private JLabel playroom;
    private JButton playBtn;
    private JButton statsBtn;
    private JButton helpBtn;
    private JButton logOutBtn;

    // przyciski do menu pobocznego (statystyki, wyniki)

    private JLabel rank;
    private JTable rankTable;
    private JButton backBtn3;

    // przyciski do menu pobocznego (sterowanie)

    private JLabel helpText;
    private JButton backBtn4;

    // przyciski do menu pobocznego (credits)

    private JLabel creditsInfo;
    private JButton backBtn5;

    // Boxy czyli odpowiednie sekcje w menu

    private Box boxMenu;
    private Box boxCredits;
    private Box boxLogIn;
    private Box boxSignUp;
    public  Box boxLoggedUser;
    private Box boxStats;
    private Box boxHelp;

    public MainFrame(){

        setLookAndFeel("Nimbus"); // wygląd przycisków
        init();

        // Ustawienie ikonki aplikacji

        try {
            setIconImage(ImageIO.read(getClass().getResource("/tank_icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        // Stworzenie odpowiednich boxów dla wszystkich sekcji

        boxMenu = createMenu();
        boxCredits = createCreditsMenu();
        boxLogIn = createLogInMenu();
        boxSignUp = createSignUpMenu();
        boxLoggedUser = createLoggedUserMenu();
        boxHelp = createHelpMenu();

        // Dodanie akcji do buttonów (oprócz statystyk, które są boxem zmiennym aktualizowanym)

        signInBtn.addActionListener(this);
        creditsBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        loginBtn.addActionListener(this);
        goToRegistration.addActionListener(this);
        backBtn1.addActionListener(this);

        // Dodanie document listenera do nasłuchiwania zmian w password fieldzie (DocumentCheck - klasa wewnętrzna)
        passwordReg.getDocument().addDocumentListener(new DocumentCheck());

        createAccountBtn.addActionListener(this);
        backBtn2.addActionListener(this);

        playBtn.addActionListener(this);
        statsBtn.addActionListener(this);
        helpBtn.addActionListener(this);
        logOutBtn.addActionListener(this);

        backBtn4.addActionListener(this);

        backBtn5.addActionListener(this);

        // Stworzenie paneli z menu oraz grą

        menuPanel = new MenuPanel();
        menuPanel.add(boxMenu);
        gamePanel = new GamePanel(this);

        // Rozpoczęcie (menu główne)

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
            log("client", e.getMessage());
            System.exit(0);
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private Box createMenu() {

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20)); // przerwa między elementami w boxie

        // Stworzenie napisu głównego

        title = new JLabel("TANKS - MULTIPLAYER");
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
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

        signInBtn = new JButton("SIGN IN");
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setBackground(Color.BLACK);
        signInBtn.setFont(new Font("Arial", Font.PLAIN, 50));
        box.add(signInBtn);

        box.add(Box.createVerticalStrut(6));

        creditsBtn = new JButton("CREDITS");
        creditsBtn.setForeground(Color.WHITE);
        creditsBtn.setBackground(Color.BLACK);
        creditsBtn.setFont(new Font("Arial", Font.PLAIN, 50));
        box.add(creditsBtn);

        box.add(Box.createVerticalStrut(6));

        exitBtn = new JButton("EXIT");
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(Color.BLACK);
        exitBtn.setFont(new Font("Arial", Font.PLAIN, 50));
        box.add(exitBtn);

        return box;
    }

    private Box createCreditsMenu(){

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        creditsInfo = new JLabel("<html>This is a project for java.<br><br>The goal is to create a game" +
                "Tanks with the possibility of playing by many players,<br>gathering stats and much more." +
                "<br>Everything based on main.client-main.server structure and with the databases connections." +
                "<br><br><br><br><br>Have fun!," +
                "<br>@gklimek</html>");
        creditsInfo.setFont(new Font("Courier New", Font.BOLD, 24));
        creditsInfo.setForeground(Color.WHITE);
        box.add(creditsInfo);

        box.add(Box.createVerticalStrut(6));

        backBtn5 = backButton();
        box.add(backBtn5);

        return box;
    }

    private Box createLogInMenu(){

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        logIn = new JLabel("LOG IN");
        logIn.setForeground(Color.WHITE);
        logIn.setFont(new Font("Comic Sans MS", Font.BOLD, 75));
        box.add(logIn);

        box.add(Box.createVerticalStrut(10));
        loginWriteLog = new JLabel("Login:");
        loginWriteLog.setForeground(Color.WHITE);
        loginWriteLog.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(loginWriteLog);

        box.add(Box.createVerticalStrut(0));
        loginLog = new JTextField();
        loginLog.setMaximumSize(new Dimension(300, 30));
        box.add(loginLog);

        box.add(Box.createVerticalStrut(15));
        passwordWriteLog = new JLabel("Password:");
        passwordWriteLog.setForeground(Color.WHITE);
        passwordWriteLog.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(passwordWriteLog);

        box.add(Box.createVerticalStrut(0));
        passwordLog = new JPasswordField();
        passwordLog.setMaximumSize(new Dimension(300, 30));
        box.add(passwordLog);

        box.add(Box.createVerticalStrut(10));
        loginBtn = new JButton("Sign in");
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setBackground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 20));
        box.add(loginBtn);

        box.add(Box.createVerticalStrut(50));
        registrationInfo = new JLabel("<html>You don't have an account yet?" +
                "<br> Don't think twice,<br>click the button below -></html>");
        registrationInfo.setForeground(new Color(225, 226, 16));
        registrationInfo.setFont(new Font("Arial", Font.ITALIC, 18));
        box.add(registrationInfo);

        box.add(Box.createVerticalStrut(12));
        goToRegistration = new JButton("Create an account for FREE");
        goToRegistration.setForeground(Color.WHITE);
        goToRegistration.setBackground(Color.BLACK);
        goToRegistration.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(goToRegistration);

        box.add(Box.createVerticalStrut(70));
        backBtn1 = backButton();
        box.add(backBtn1);

        return box;
    }

    private Box createSignUpMenu(){

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        registration = new JLabel("REGISTRATION");
        registration.setForeground(Color.WHITE);
        registration.setFont(new Font("Comic Sans MS", Font.BOLD, 75));
        box.add(registration);

        box.add(Box.createVerticalStrut(10));
        loginWriteReg = new JLabel("Login:");
        loginWriteReg.setForeground(Color.WHITE);
        loginWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(loginWriteReg);

        box.add(Box.createVerticalStrut(0));
        loginReg = new JTextField();
        loginReg.setEditable(true);
        loginReg.setMaximumSize(new Dimension(300, 30));
        box.add(loginReg);

        box.add(Box.createVerticalStrut(15));
        passwordWriteReg = new JLabel("Password:");
        passwordWriteReg.setForeground(Color.WHITE);
        passwordWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(passwordWriteReg);

        box.add(Box.createVerticalStrut(0));
        passwordReg = new JPasswordField();
        passwordReg.setEditable(true);
        passwordReg.setMaximumSize(new Dimension(300, 30));
        box.add(passwordReg);

        box.add(Box.createVerticalStrut(20));
        textPasswdStrength = new JLabel("Password Strength");
        textPasswdStrength.setForeground(Color.WHITE);
        textPasswdStrength.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        box.add(textPasswdStrength);

        box.add(Box.createVerticalStrut(10));
        passwordStrength = new JProgressBar();
        passwordStrength.setPreferredSize(new Dimension(260, 13));
        passwordStrength.setMaximumSize(new Dimension(260, 13));
        box.add(passwordStrength);

        box.add(Box.createVerticalStrut(0));
        passwordStrenghtInfo = new JLabel();
        passwordStrenghtInfo.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        box.add(passwordStrenghtInfo);

        box.add(Box.createVerticalStrut(30));
        firstNameWriteReg = new JLabel("First name:");
        firstNameWriteReg.setForeground(Color.WHITE);
        firstNameWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(firstNameWriteReg);

        box.add(Box.createVerticalStrut(0));
        firstNameReg = new JTextField();
        firstNameReg.setEditable(true);
        firstNameReg.setMaximumSize(new Dimension(300, 30));
        box.add(firstNameReg);

        box.add(Box.createVerticalStrut(15));
        lastNameWriteReg = new JLabel("Last name:");
        lastNameWriteReg.setForeground(Color.WHITE);
        lastNameWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(lastNameWriteReg);

        box.add(Box.createVerticalStrut(0));
        lastNameReg = new JTextField();
        lastNameReg.setEditable(true);
        lastNameReg.setMaximumSize(new Dimension(300, 30));
        box.add(lastNameReg);

        box.add(Box.createVerticalStrut(15));
        emailWriteReg = new JLabel("Email:");
        emailWriteReg.setForeground(Color.WHITE);
        emailWriteReg.setFont(new Font("Arial", Font.BOLD, 15));
        box.add(emailWriteReg);

        box.add(Box.createVerticalStrut(0));
        emailReg = new JTextField();
        emailReg.setEditable(true);
        emailReg.setMaximumSize(new Dimension(300, 30));
        box.add(emailReg);

        box.add(Box.createVerticalStrut(10));
        createAccountBtn = new JButton("Sign up");
        createAccountBtn.setForeground(Color.BLACK);
        createAccountBtn.setBackground(Color.WHITE);
        createAccountBtn.setFont(new Font("Arial", Font.BOLD, 20));
        box.add(createAccountBtn);

        box.add(Box.createVerticalStrut(70));
        backBtn2 = backButton();
        box.add(backBtn2);

        return box;
    }

    private Box createLoggedUserMenu(){

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        playroom = new JLabel("Playroom");
        playroom.setForeground(new Color(67, 209, 17));
        playroom.setFont(new Font("Comic Sans MS", Font.BOLD, 80));
        box.add(playroom);

        box.add(Box.createVerticalStrut(6));
        playBtn = new JButton("Play");
        playBtn.setForeground(Color.WHITE);
        playBtn.setBackground(Color.BLACK);
        playBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(playBtn);

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
        logOutBtn = new JButton("Log out"); // wylogowanie się
        logOutBtn.setForeground(Color.BLACK);
        logOutBtn.setBackground(Color.WHITE);
        logOutBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        box.add(logOutBtn);

        return box;
    }

    private Box createStatsMenu(){

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        rank = new JLabel("GLOBAL STATISTICS");
        rank.setForeground(Color.WHITE);
        rank.setFont(new Font("Comic Sans MS", Font.BOLD, 75));
        box.add(rank);

        box.add(Box.createVerticalStrut(10));

        // Stworzenie tabeli ze statystykami
        prepareStatsTable();

        // Dodanie paska przewijającego do tabeli oraz do boxa

        if (rankTable != null){
            box.add(new JScrollPane(rankTable));
        }

        box.add(Box.createVerticalStrut(6));
        backBtn3 = backButton();
        // Dodanie akcji za każdym razem do przycisku wstecz
        backBtn3.addActionListener(this);
        box.add(backBtn3);

        return box;
    }

    private Box createHelpMenu(){
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));

        helpText = new JLabel("<html>You are driving a tank. Your goal is to eliminate " +
                "other tanks.<br><br>1) Sign up entering your login and password" +
                "<br><br>2) After registration sign in" +
                "<br><br>3) Now you can join to the game<br><br>Your stats are remembered.<br><br>To move use:" +
                "<br><br>W - up<br>A - left" +
                "<br>S - down<br>D - right" + "<br><br>L - Attack<br><br>Gl and Hf</html>");

        helpText.setFont(new Font("Courier New", Font.BOLD, 26));
        helpText.setForeground(Color.WHITE);
        box.add(helpText);

        box.add(Box.createVerticalStrut(6));

        backBtn4 = backButton();
        box.add(backBtn4);

        return box;
    }

    private JButton backButton(){

        JButton backBtn = new JButton("Back");
        backBtn.setForeground(Color.WHITE);
        backBtn.setBackground(Color.BLACK);
        backBtn.setFont(new Font("Arial", Font.PLAIN, 10));
        return  backBtn;
    }

    private boolean checkLoginCorrectness(){

        boolean result = false;

        LoginValidator loginValidator = new LoginValidator();

        if (loginValidator.validate(loginReg.getText()))
            result = true;

        return result;
    }

    private boolean checkPasswordCorrectness(){

        boolean result = false;

        PasswordValidator passwordValidator = new PasswordValidator();

        if (passwordValidator.validate(String.valueOf(passwordReg.getPassword())))
            result = true;

        return result;
    }

    private boolean checkFirstNameCorrectness(){

        boolean result = false;

        FirstNameValidator firstNameValidator = new FirstNameValidator();

        if (firstNameValidator.validate(firstNameReg.getText()))
            result = true;

        return result;
    }

    private boolean checkLastNameCorrectness(){

        boolean result = false;

        LastNameValidator lastNameValidator = new LastNameValidator();

        if (lastNameValidator.validate(lastNameReg.getText()))
            result = true;

        return result;
    }

    private boolean checkEmailCorrectness(){

        boolean result = false;

        EmailValidator emailValidator = new EmailValidator();

        if (emailValidator.validate(emailReg.getText()))
            result = true;

        return result;
    }

    private boolean checkFieldsCorrectness(){

        return checkLoginCorrectness() && checkPasswordCorrectness() && checkFirstNameCorrectness() &&
                checkLastNameCorrectness() && checkEmailCorrectness();
    }

    private void prepareStatsTable(){

        // Nagłowki dla tabeli (nazwy kolumn)
        String[] columns = new String[] {
                "Login", "Destroyed Tanks", "Number of deaths", "Difference"
        };

        // Dane do tabeli z bazy danych (tablica 2-wymiarowa - wiersz, kolumna)
        Object[][] data = database.enterDataToTable();

        if (data != null){

            final Class[] columnClass = new Class[] {
                    String.class, Integer.class, Integer.class, Integer.class
            };

            // Stworzenie modelu z danymi
            DefaultTableModel model = new DefaultTableModel(data, columns) {

                @Override
                public boolean isCellEditable(int row, int column)
                {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex)
                {
                    return columnClass[columnIndex];
                }

            };

            // Stworzenie tabeli na podstawie modelu (początkowo pusta)
            rankTable = new JTable(model);

            // Wyśrodkowanie nazw kolum (headerów)
            ((DefaultTableCellRenderer)rankTable.getTableHeader().getDefaultRenderer())
                    .setHorizontalAlignment(SwingConstants.CENTER);

            // Ustawienie wyśrodkowania danych gromadzonych w tabeli
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            rankTable.setDefaultRenderer(String.class, centerRenderer);
            rankTable.setDefaultRenderer(Integer.class, centerRenderer);

            // Umożliwienie sortowania wierszy w.g odpowiednich kryteriów
            rankTable.setAutoCreateRowSorter(true);

            // Podświetlenie wybranego wiersza
            rankTable.addColumnSelectionInterval(0,3);
            rankTable.setSelectionBackground(new Color(225, 226, 16));
            rankTable.setSelectionForeground(new Color(0,0,0));

        }
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == signInBtn){

            boxMenu.setVisible(false);
            menuPanel.remove(boxMenu);
            menuPanel.add(boxLogIn);
            boxLogIn.setVisible(true);
        }

        if (e.getSource() == creditsBtn){

            boxMenu.setVisible(false);
            menuPanel.remove(boxMenu);
            menuPanel.add(boxCredits);
            boxCredits.setVisible(true);
        }

        if (e.getSource() == exitBtn){
            System.exit(0);
        }

        if (e.getSource() == loginBtn){

            // Haszowanie aby porównać hasła, z tym z bazy danych
            String nonHashedPassword = String.valueOf(passwordLog.getPassword());
            String hashedPassword = passwordHashing(nonHashedPassword);

            if (database.loginUser(loginLog.getText(), hashedPassword)){

                yourLogin = loginLog.getText();
                setTitle("Client logged as: " + yourLogin);
                log("client", yourLogin + " logged in");

                JOptionPane.showMessageDialog(null, "You are successfully logged in",
                        "Logged In", JOptionPane.INFORMATION_MESSAGE);

                boxLogIn.setVisible(false);
                menuPanel.remove(boxLogIn);
                menuPanel.add(boxLoggedUser);
                boxLoggedUser.setVisible(true);

                loginLog.setText("");
                passwordLog.setText("");
            }
            else
                JOptionPane.showMessageDialog(null, "Incorrect login or password or you haven't yet a free account. " +
                        "Go to registration to create account or try to log in with proper data",
                        "Log in failed", JOptionPane.ERROR_MESSAGE);
                log("client", "Failed login attempt to: " + yourLogin);
        }

        if (e.getSource() == goToRegistration){

            boxLogIn.setVisible(false);
            menuPanel.remove(boxLogIn);
            menuPanel.add(boxSignUp);
            boxSignUp.setVisible(true);

            loginLog.setText("");
            passwordLog.setText("");
        }

        if (e.getSource() == backBtn1){

            boxLogIn.setVisible(false);
            menuPanel.remove(boxLogIn);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);

            loginLog.setText("");
            passwordLog.setText("");
        }

        if (e.getSource() == createAccountBtn){

            // Sprawdzanie poprawności danych wprowadzonych dla poszczególnych pól

            if (checkFieldsCorrectness()){

                // Haszowanie hasła
                String nonHashedPassword = String.valueOf(passwordReg.getPassword());
                String hashedPassword = passwordHashing(nonHashedPassword);

                if (database.registerUser(loginReg.getText(), hashedPassword, firstNameReg.getText(),
                        lastNameReg.getText(), emailReg.getText())){

                    log("client", "Registered new user: " + "[ " + yourLogin + " ]");

                    JOptionPane.showMessageDialog(null, "Account successfully created! You can now log in",
                            "ACCOUNT CREATED", JOptionPane.INFORMATION_MESSAGE);

                    boxSignUp.setVisible(false);
                    menuPanel.remove(boxSignUp);
                    menuPanel.add(boxLogIn);
                    boxLogIn.setVisible(true);

                    loginReg.setText("");
                    passwordReg.setText("");
                    firstNameReg.setText("");
                    lastNameReg.setText("");
                    emailReg.setText("");
                }
                else
                    JOptionPane.showMessageDialog(null, "User with this login or with this e-mail has already exists!" +
                            " Change login or e-mail to create account",
                            "Registration failed", JOptionPane.ERROR_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, "Check your data format: " +
                        "length, email format correctness, uppercases",
                        "Invalid data format", JOptionPane.ERROR_MESSAGE);
        }

        if (e.getSource() == backBtn2){

            boxSignUp.setVisible(false);
            menuPanel.remove(boxSignUp);
            menuPanel.add(boxLogIn);
            boxLogIn.setVisible(true);

            loginReg.setText("");
            passwordReg.setText("");
            firstNameReg.setText("");
            lastNameReg.setText("");
            emailReg.setText("");

            passwordStrenghtInfo.setText("");
        }

        /* --------------------------------------------------------------------------------------------------------- */
        /* --------------------------------------------------------------------------------------------------------- */
        /* ---------------------------------Lączenie klienta z serverem--------------------------------------------- */
        /* --------------------------------------------------------------------------------------------------------- */
        /* --------------------------------------------------------------------------------------------------------- */

        if (e.getSource() == playBtn){

            // Stworzenie klienta - socketa do komunikacji

            final String HOST = "localhost";
            final int PORT = 8080;

            //HOST = JOptionPane.showInputDialog(null, "Server address of game: ", "Address", JOptionPane.PLAIN_MESSAGE);

            try {

                client.connect(HOST, PORT);

            } catch (IOException ex) {

                log("client", ex.getMessage());
                System.err.println(ex.getMessage());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            }

            if (client.isConnected()){

                log("client", "Client: " + yourLogin + " is connected to server");

                boxLoggedUser.setVisible(false);
                menuPanel.remove(boxLoggedUser);
                menuPanel.setVisible(false);
                remove(menuPanel);

                // Widok: panel z grą

                gamePanel.setVisible(true);
                add(gamePanel);
                gamePanel.requestFocusInWindow();

                gamePanel.runGameLoopThread(); // ustawienie flagi looping na true + uruchomienie wątku gameLoop
            }
        }

        if (e.getSource() == statsBtn){

            boxLoggedUser.setVisible(false);
            menuPanel.remove(boxLoggedUser);

            boxStats = createStatsMenu();

            menuPanel.add(boxStats);
            boxStats.setVisible(true);
        }

        if (e.getSource() == helpBtn){

            boxLoggedUser.setVisible(false);
            menuPanel.remove(boxLoggedUser);
            menuPanel.add(boxHelp);
            boxHelp.setVisible(true);
        }

        if (e.getSource() == logOutBtn){

            log("client", yourLogin + " logged out");
            setTitle("Client not logged now");

            JOptionPane.showMessageDialog(null, "You are succesfully logged out. I hope we will see you soon ;)",
                    "Logged out", JOptionPane.INFORMATION_MESSAGE);

            boxLoggedUser.setVisible(false);
            menuPanel.remove(boxLoggedUser);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);
        }

        if (e.getSource() == backBtn3){

            boxStats.setVisible(false);
            menuPanel.remove(boxStats);
            menuPanel.add(boxLoggedUser);
            boxLoggedUser.setVisible(true);
        }

        if (e.getSource() == backBtn4){

            boxHelp.setVisible(false);
            menuPanel.remove(boxHelp);
            menuPanel.add(boxLoggedUser);
            boxLoggedUser.setVisible(true);
        }

        if (e.getSource() == backBtn5){

            boxCredits.setVisible(false);
            menuPanel.remove(boxCredits);
            menuPanel.add(boxMenu);
            boxMenu.setVisible(true);
        }
    }

    // Klasa wewnętrzna do sprawdzania siły hasła

    private class DocumentCheck implements DocumentListener{

        @Override
        public void insertUpdate(DocumentEvent e) {
            check();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            check();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            check();
        }

        private void check() {

            String passwd;
            int counter = 0;

            passwd = String.valueOf(passwordReg.getPassword());

            if (passwd.length() > 6){
                counter++;
            }

            if (passwd.matches(".*[!@#$%&*+=:;,.?<>_-].*")){
                counter++;
            }

            if (passwd.matches(".*[A-Z].*")){
                counter++;
            }

            if (passwd.matches(".*[1-9].*")){
                counter++;
            }

            switch (counter){

                case 0:
                    passwordStrength.setValue(0);
                    passwordStrenghtInfo.setForeground(new Color(255, 34, 0));
                    passwordStrenghtInfo.setText("Very weak");
                    break;

                case 1:
                    passwordStrength.setValue(25);
                    passwordStrenghtInfo.setForeground(new Color(223, 110, 22));
                    passwordStrenghtInfo.setText("Weak");
                    break;

                case 2:
                    passwordStrength.setValue(50);
                    passwordStrenghtInfo.setForeground(new Color(196, 200, 38));
                    passwordStrenghtInfo.setText("Medium");
                    break;

                case 3:
                    passwordStrength.setValue(75);
                    passwordStrenghtInfo.setForeground(new Color(96, 124, 223));
                    passwordStrenghtInfo.setText("Strong");
                    break;

                case 4:
                    passwordStrength.setValue(100);
                    passwordStrenghtInfo.setForeground(new Color(45, 255, 7));
                    passwordStrenghtInfo.setText("Very Strong");
                    break;

                default:
                    break;
            }
        }
    }
}