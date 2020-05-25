package GUI;

import javax.imageio.ImageIO;
import javax.imageio.ImageTranscoder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;

public class DrawingGUI {
    BufferedImage texture;
    int x, y, width, height;

    DrawingGUI(String textureName) {

        String name = null;
        try {
            name = "src/GUI/Pack/" + textureName + ".png" ;
            texture = ImageIO.read(new File(name));
        } catch (Exception e) {
            System.out.println("File cannot be opened");
            e.printStackTrace();
        }
    }
    private DrawingGUI(DrawingGUI other) {
        this.texture = other.texture;
    }

    public DrawingGUI getImage(int posX, int posY, int size) {
        DrawingGUI g = new DrawingGUI(this);
        g.x = posX;
        g.y = posY;
        g.width = size;
        g.height = size;
        return  g;
    }
}


