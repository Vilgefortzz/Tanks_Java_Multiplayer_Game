package main.gui.views;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private MenuPanel menuPanel;

    public GUI() {
        super("Tanks");
        setLookAndFeel("Nimbus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLayout(new BorderLayout());
        setVisible(true);

        init();
    }

    private void init() {
        menuPanel = new MenuPanel();
        Box box = createMenu();

        add(menuPanel);
        menuPanel.add(box);
    }

    //Funkcja ustawia wyglad przyciskow oraz innych elementow
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

        JLabel title = new JLabel("MAIN MENU");
        title.setFont(new Font("Courier New", Font.ITALIC, 65));
        title.setForeground(Color.WHITE);
        box.add(title);

        box.add(Box.createVerticalStrut(8));

        JButton startGameBtn = new JButton("Start game");
        startGameBtn.setForeground(Color.WHITE);
        startGameBtn.setBackground(Color.BLACK);
        box.add(startGameBtn);

        box.add(Box.createVerticalStrut(8));

        JButton optionsBtn = new JButton("Options");
        optionsBtn.setForeground(Color.WHITE);
        optionsBtn.setBackground(Color.BLACK);
        box.add(optionsBtn);

        box.add(Box.createVerticalStrut(8));

        JButton creditsBtn = new JButton("Credits");
        creditsBtn.setForeground(Color.WHITE);
        creditsBtn.setBackground(Color.BLACK);
        box.add(creditsBtn);

        box.add(Box.createVerticalStrut(8));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(Color.BLACK);
        box.add(exitBtn);


        return box;
    }

}

