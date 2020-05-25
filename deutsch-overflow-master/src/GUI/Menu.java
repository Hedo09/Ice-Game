package GUI;

import Control.Game;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import PlayerClasses.PlayerContainer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel  {
    JButton minus, plus, startGame;
    JLabel numLabel;
    Integer playerNum;

    Menu(){
        setLayout(null);
        playerNum = 4;
        numLabel = new JLabel(playerNum.toString());
        minus = new JButton();

        minus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(playerNum > 3) {
                    numLabel.setText((--playerNum).toString());
                    if(playerNum == 5) plus.validate();
                }
                else minus.invalidate();
            }
        });
        plus = new JButton();
        plus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(playerNum < 6) {
                    numLabel.setText((++playerNum).toString());
                    if(playerNum == 4) minus.validate();
                }
                else plus.invalidate();
            }
        });
        startGame = new JButton("startGame");
        startGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PlayerContainer.Initialize(playerNum, 3);
                RoundController.getInstance();
                PositionLUT.getInstance().randInit();
                InGame.getInstance().changeToGameScreen(playerNum);
            }
        });
        add(minus);
        add(numLabel);
        add(plus);
        add(startGame);
        minus.setLocation(500,300);
        minus.setSize(50,50);
        Image imgMinus = null;
        Image imgPlus= null;

        try {
            imgMinus = ImageIO.read(new File("src/GUI/Pack/minus.png"));
            imgPlus = ImageIO.read(new File("src/GUI/Pack/plus.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Image newimgMinus = imgMinus.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;
        Image newimgPlus = imgPlus.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;

        minus.setIcon(new ImageIcon(newimgMinus));
        plus.setIcon(new ImageIcon(newimgPlus));
        startGame.setOpaque(false);
        startGame.setContentAreaFilled(false);

        plus.setLocation(600,300);
        plus.setSize(50,50);
        startGame.setLocation(500,350);
        startGame.setSize(150,80);
        numLabel.setLocation(565,300);
        numLabel.setSize(50,50);
        numLabel.setFont(new Font("Serif", Font.PLAIN, 34));
        startGame.setFont(new Font("Serif", Font.PLAIN, 24));
        setVisible(true);
    }

    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/GUI/Pack/start.jpg"));
        } catch (IOException e) {
        }
        g2d.drawImage(image, 0, 0,null);
    }

}
