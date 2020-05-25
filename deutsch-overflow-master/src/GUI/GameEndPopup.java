package GUI;

import GlobalControllers.GameEndCause;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class GameEndPopup {
    public static void showPopup(GameEndCause cause) {
        String title;
        if(cause.equals(GameEndCause.win)) title = "Game won";
        else title = "Game lost";

        ImageIcon icon = new ImageIcon(cause.iconName, "Icon of cause");
        JOptionPane.showMessageDialog(null, cause.output, title,JOptionPane.PLAIN_MESSAGE,icon);
    }
}
