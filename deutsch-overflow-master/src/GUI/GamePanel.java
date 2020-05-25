package GUI;

import Control.Game;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import ItemClasses.Item;
import ItemClasses.ItemState;
import PlayerClasses.Player;
import PlayerClasses.PlayerContainer;
import TileClasses.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel {

    public static boolean capacityEnabled = false;
    public static int capacityOnTile;
    public static int capacityX;
    public static int capacityY;

    private int tileSize;
    private int tilePadding;
    private int margin = 50;
    private int playerPadding;
    private int playerBoxX;
    private int playerBoxY;
    private int messageBoxX;
    private int messageBoxY;
    private int posRightSideX;
    private int posStatusBoxUpSideY;
    private int statusBoxIconSize;
    private int itemSize;
    private int posMessageBoxUpSideY;

    private Map<String, DrawingGUI> icons = new HashMap<String, DrawingGUI>();
    /**
     * collection of components from positionLUT
     */
    ArrayList<DrawingGUI> components = new ArrayList<>();

    GamePanel(){
        setLayout(new GridBagLayout());
        ComponentListener listener = new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
            }

            public void componentHidden(ComponentEvent evt) {
            }

            public void componentMoved(ComponentEvent evt) {
            }

            public void componentResized(ComponentEvent evt) {
                Game.dirty=true;
                Rectangle b = evt.getComponent().getBounds();
                evt.getComponent().setBounds(b.x, b.y, b.width, b.width*750/1400);
            }

        };
        addComponentListener(listener);
        initIcons();
    }

    private void rescaleAnchors() {
        int panelHeight = getHeight();
        int panelWidth = getWidth();
        if(panelWidth < 1.7 * panelHeight) {
            panelHeight=(int)(panelWidth*0.55);
            margin = (int) (panelWidth * 0.01);
        }
        tilePadding = 0;
        tileSize = (panelHeight - 2 * margin - tilePadding * 5)/6;
        itemSize = tileSize/3-4;
        playerBoxX = (panelWidth - panelHeight - margin)/3 ;
        playerBoxY = (panelHeight - 2 * margin - 2 * playerPadding)/9;
        messageBoxY =  2 * (playerBoxY/3);
        messageBoxX = (panelWidth - panelHeight -  margin);
        playerPadding = playerBoxY/2;
        statusBoxIconSize = (int) (1.8*playerBoxY);
        posRightSideX = panelHeight + margin;
        posStatusBoxUpSideY = panelHeight- statusBoxIconSize - margin;
        posMessageBoxUpSideY = posStatusBoxUpSideY-2*messageBoxY;
    }


    String capacity = "-";
    private void initIcons() {
        //loading players
        //in runtime revealed whether a player is eskimo or researcher
        for (int i = 0; i < PlayerContainer.getInstance().getPlayerNum(); i++) {
            String p = PlayerContainer.getInstance().getPlayer(i).toString();
            icons.put(p, new DrawingGUI(p));
            icons.put(p +"-a", new DrawingGUI(p + "-a"));
        }

        //loading polarbear
        icons.put("P", new DrawingGUI("P"));
        icons.put("P-a", new DrawingGUI("P-a"));

        //loading items
        icons.put("divingSuit", new DrawingGUI("divingSuit"));
        icons.put("divingSuitF", new DrawingGUI("divingSuitF"));
        icons.put("food", new DrawingGUI("food"));
        icons.put("foodF", new DrawingGUI("foodF"));
        icons.put("fragileShovel", new DrawingGUI("fragileShovel"));
        icons.put("fragileShovelF", new DrawingGUI("fragileShovelF"));
        icons.put("signalflarePart1", new DrawingGUI("signalflarePart1"));
        icons.put("signalflarePart1F", new DrawingGUI("signalflarePart1F"));
        icons.put("signalflarePart2", new DrawingGUI("signalflarePart2"));
        icons.put("signalflarePart2F", new DrawingGUI("signalflarePart2F"));
        icons.put("signalflarePart0", new DrawingGUI("signalflarePart0"));
        icons.put("signalflarePart0F", new DrawingGUI("signalflarePart0F"));
        icons.put("rope", new DrawingGUI("rope"));
        icons.put("ropeF", new DrawingGUI("ropeF"));
        icons.put("shovel", new DrawingGUI("shovel"));
        icons.put("shovelF", new DrawingGUI("shovelF"));

        //other actions
        icons.put("capacity", new DrawingGUI("capacity"));
        icons.put("clearSnow", new DrawingGUI("clearSnow"));
        icons.put("digUp", new DrawingGUI("digUp"));
        icons.put("igloo", new DrawingGUI("igloo"));
        icons.put("passRound", new DrawingGUI("passRound"));
        icons.put("pickUp", new DrawingGUI("pickUp"));
        icons.put("tent", new DrawingGUI("tent"));
        icons.put("tentF", new DrawingGUI("tentF"));
        //loading tiles
        icons.put("tileNoSnow", new DrawingGUI("tileNoSnow"));
        icons.put("tileSnow", new DrawingGUI("tileSnow"));
        icons.put("tileWater", new DrawingGUI("tileWater"));
        //other icons
        icons.put("snow", new DrawingGUI("snow"));
        icons.put("inWater", new DrawingGUI("inWater"));
        icons.put("heart", new DrawingGUI("heart"));
        icons.put("warning", new DrawingGUI("warning"));
        icons.put("workingPoints", new DrawingGUI("workingPoints"));
    }


    /**
     * clears former list elements
     * fills up components from positionLUT
     *
     */
    void refreshComponents() {
        components.clear();
        removeAll();
        rescaleAnchors();

        //Tiles grid
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                DrawingGUI dgui = icons.get(PositionLUT.getTile(x, y).toString())
                        .getImage(margin + x * (tileSize + tilePadding), margin + y * (tileSize + tilePadding), tileSize);

                components.add(dgui);
            }
        }
        //Players in TileGrid
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6;
                 y++) {
                ArrayList<Player> players = PositionLUT.getPlayersOnTile(PositionLUT.getTile(x, y));
                int count=0;
                int count2=0;
                for (Player p : players) {
                    DrawingGUI dgui = icons.get(p.toString())
                            .getImage(margin + x * (tileSize + tilePadding)+count2*itemSize+5, margin + y * (tileSize + tilePadding)+2*itemSize-count*itemSize+5, itemSize);
                    components.add(dgui);
                    count++;
                    if(count%3==0){
                        count2=1;
                        count=0;
                    }
                }
            }
        }

        //Item in TileGrid
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                ArrayList<Item> iList = PositionLUT.getItemOnTile(PositionLUT.getTile(x, y));
                for (Item item : iList) {
                    if(PositionLUT.getTile(x, y).getSnow() == 0){
                        DrawingGUI dgui = icons.get(item.toString())
                                .getImage(margin + x * (tileSize + tilePadding) + 2*itemSize+5, margin + y * (tileSize + tilePadding) + 2*itemSize+5, itemSize);
                        components.add(dgui);
                    }

                }
            }
        }
        //loading shelters
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                if(PositionLUT.getTile(x,y).getIglooOn()){
                    DrawingGUI dgui = icons.get("igloo")
                            .getImage(margin + x * (tileSize + 1) +tileSize/4, margin + y * (tileSize + 1) + tileSize/4, tileSize/2);
                    components.add(dgui);
                }else if(PositionLUT.getTile(x,y).tentOn){
                    DrawingGUI dgui = icons.get("tent")
                            .getImage(margin + x * (tileSize + 1) + tileSize/4, margin + y * (tileSize + 1) + tileSize/4, tileSize/2);
                    components.add(dgui);
                }
            }
        }

        //PolarBear on TileGrid
        Tile tile = PositionLUT.getInstance().getPosition(RoundController.getInstance().polarbear);
        int x= tile.getX();
        int y = tile.getY();
        DrawingGUI polargui = icons.get(RoundController.getInstance().polarbear.toString())
                .getImage(margin + x * (tileSize + tilePadding) + itemSize+5, margin + y * (tileSize + tilePadding) + itemSize+5, itemSize);
        components.add(polargui);


        //PlayerBox
        for(int i = 0; i <PlayerContainer.getInstance().getPlayerNum(); i++ ){
            DrawingGUI dgui = icons.get(PlayerContainer.getInstance().getPlayer(i).toString())
                    .getImage(posRightSideX + (i%3)*playerBoxX, margin + (i/3)*(playerBoxY + playerPadding), 50);
            components.add(dgui);

            DrawingGUI heartgui = icons.get("heart")
                    .getImage(posRightSideX + (i%3)*playerBoxX + 70, margin + (i/3)*(playerBoxY + playerPadding), 30);
            components.add(heartgui);

            Label label = new Label(Integer.toString(PlayerContainer.getInstance().getPlayer(i).getBodyHeat()));
            this.add(label);
            label.setSize(15,30);
            label.setLocation(heartgui.x+40,heartgui.y);



            if(PlayerContainer.getInstance().getPlayer(i).getItemsOnHand().size() == 1){
                DrawingGUI itemgui = icons.get(PlayerContainer.getInstance().getPlayer(i).getItemsOnHand().get(0).toString())
                        .getImage(posRightSideX + (i%3)*playerBoxX + 70, margin + (i/3)*(playerBoxY + playerPadding) + 35, 30);
                components.add(itemgui);
            }else if(PlayerContainer.getInstance().getPlayer(i).getItemsOnHand().size() == 2){
                DrawingGUI itemgui = icons.get(PlayerContainer.getInstance().getPlayer(i).getItemsOnHand().get(0).toString())
                        .getImage(posRightSideX + (i%3)*playerBoxX + 70, margin + (i/3)*(playerBoxY + playerPadding) + 35, 30);
                components.add(itemgui);

                DrawingGUI divingsuitgui = icons.get(PlayerContainer.getInstance().getPlayer(i).getItemsOnHand().get(1).toString())
                        .getImage(posRightSideX + (i%3)*playerBoxX + 105, margin + (i/3)*(playerBoxY + playerPadding) + 35, 30);
                components.add(divingsuitgui);
            }

        }

        Player activePlayer = PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID());
        DrawingGUI dguiP = icons.get(activePlayer.toString()+"-a").getImage(posRightSideX, posStatusBoxUpSideY, statusBoxIconSize);
        components.add(dguiP);

        DrawingGUI dguiS = icons.get("snow").getImage(posRightSideX + statusBoxIconSize + 15, posStatusBoxUpSideY+70, 30);
        components.add(dguiS);

        DrawingGUI dguiW = icons.get("workingPoints").getImage(posRightSideX + statusBoxIconSize + 10, posStatusBoxUpSideY+10, 40);
        components.add(dguiW);

        DrawingGUI capacitygui = icons.get("capacity").getImage(posRightSideX+100, posMessageBoxUpSideY, 30);
        components.add(capacitygui);



        Label snowLabel= new Label(Integer.toString(PositionLUT.getPosition(activePlayer).getSnow()));
        this.add(snowLabel);
        snowLabel.setLocation(posRightSideX + statusBoxIconSize +60,posStatusBoxUpSideY+70);
        snowLabel.setSize(30,30);
        snowLabel.setFont(new Font("Serif", Font.PLAIN, 34));

        Label workingPointsLabel= new Label(Integer.toString(activePlayer.workPoints));
        this.add(workingPointsLabel);
        workingPointsLabel.setLocation(posRightSideX + statusBoxIconSize +60,posStatusBoxUpSideY+10);
        workingPointsLabel.setSize(30,30);
        workingPointsLabel.setFont(new Font("Serif", Font.PLAIN, 34));


        if(capacityEnabled){
            capacity = "Capacity on Tile (" +Integer.toString(capacityX) + ";" +Integer.toString(capacityY) + " ) is "+ Integer.toString(capacityOnTile) + ".";
        }
        Label capacityLabel= new Label(capacity);
        this.add(capacityLabel);
        capacityLabel.setLocation(posRightSideX+150,posMessageBoxUpSideY);
        capacityLabel.setSize(200,30);


    }

    Integer i = 0;



    @Override
    public void paint(Graphics g)
    {
        if(Game.dirty){
            refreshComponents();
        }
        Game.dirty = false;
        Graphics2D g2d = (Graphics2D) g;
        for (DrawingGUI dg : components) {
            g2d.drawImage(dg.texture, dg.x, dg.y, dg.width, dg.height, null);
        }
        setVisible(true);

    }

}
