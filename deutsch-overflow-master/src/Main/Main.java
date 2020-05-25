package Main;

import Control.CommandInterpreter;
import Control.Game;
import Control.KeyboardInterpreter;
import GUI.InGame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;

public class Main {
    public static KeyboardInterpreter interpreter;

    public static void main(String[] args) {

        try(PrintStream out = new PrintStream(new FileOutputStream(new File("log.txt")))) {
            interpreter = new KeyboardInterpreter(out);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InGame.createWelcomeScreen();

    }

}