
package GUI;
import Control.Game;
import Main.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class InGame extends JFrame {
    protected static InGame inGame;

    public static InGame getInstance() {
        if(inGame == null) {
            inGame = new InGame();
        }
        return inGame;
    }

    public static GamePanel gamePanel = null;
    static Menu menu = new Menu();

    InGame(){
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 750);
        setMinimumSize(new Dimension(1400,750));
        setResizable(true);
        this.getContentPane().add(menu);
        setVisible(true);
    }

    public void changeToGameScreen(int playerNum){
        this.getContentPane().removeAll();
        gamePanel = new GamePanel();

        gamePanel.addKeyListener(Main.interpreter);
        gamePanel.setFocusable(true);
        Main.interpreter.startGame(playerNum);

        add(gamePanel);
        gamePanel.refreshComponents();
        gamePanel.requestFocus();
        revalidate();
    }

    public static void createWelcomeScreen() {
        inGame = new InGame();
    }
}