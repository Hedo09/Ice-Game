package Control;

import GUI.InGame;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import ItemClasses.Item;
import PlayerClasses.Eskimo;
import PlayerClasses.Player;
import PlayerClasses.PlayerContainer;
import PlayerClasses.Researcher;
import TileClasses.Direction;
import TileClasses.Tile;
//import javafx.geometry.Pos;

import java.io.PrintStream;

import static GlobalControllers.RoundController.getInstance;

/**
 * An instance of the game, starts the game when created
 * @author Zsófi
 * @noinspection ALL
 */
public class Game {
    /**
     * Stores, if this game is deterministic or random.
     */
    static public boolean isDeterministic;

    /**
     * If this switch is on, then the storm can come
     */
    static public boolean stormy;

    /**
     * Stores the number of players.
     * The number of players is asked as input and
     * is given to the game instance in its c'tor
     */
    int playerNum;


    static public boolean dirty = true;

    /**
     * Every function of the game logs its output to this stream
     */
    public static PrintStream log;

    /**
     * @param _isDeterministic
     * @param _playerNum
     * @param mapNum
     * Initializes the game instance and starts the game
     * @author Zsófi
     */
    private Game(int _playerNum, int mapNum) {
        playerNum = _playerNum;
        PlayerContainer.Initialize(playerNum, mapNum);
        RoundController.getInstance(); //letrehoz
        PositionLUT.Initialize(isDeterministic, mapNum);
        log.println("init ready");
    }

    /**
     * Sets the current (but static) states: isDeterministic=true and stormy
     * Then initializes the game instance and sets the output log to the given stream
     * @param out              the log Stream of the Game
     * @param playerNum        number of players in the game
     * @param mapNum           there are two deterministic maps; this is used in a deterministic game
     * @param _stormy          switch: deterministic games can have storms or no storms at all
     * @return                 returns the new game instance
     * @author Zsófi
     */
    public static Game startDeterministicGame(PrintStream out, int playerNum, int mapNum, boolean _stormy) {
        log = out;
        isDeterministic = true;
        stormy = _stormy;
        return new Game(playerNum, mapNum);
    }

    /**
     * Sets the current (but static) states: isDeterministic is false and stormy is true
     * Then initializes the game instance and sets the output log to the given stream
     * @param out              the log Stream of the Game
     * @param playerNum        number of players in the game
     * @return                 returns the new game instance
     * @author Zsófi
     */
    public static Game startRandomGame(PrintStream out, int playerNum) {
        log = out;
        isDeterministic = false;
        stormy = true;
        // 0 is a dummy, initialization is based on isDeterministic and won't use this dummy in the random case
        return new Game(playerNum, 0);
    }

    /**
     * @param dir Direction of step
     * Calls step of current player in the given direction
     */
    void step(Direction dir) {
        PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID()).step(dir);
        dirty = true;
        InGame.gamePanel.repaint();
    }

    /**
     * Writes out a map of items on the table
     * @author Ádám
     */
    public void printItemMap() {
        for (int y = 5; y >= 0; y--) {
            for(int x = 0; x < 6; x++){
                Tile currentTile = PositionLUT.getInstance().getTile(x,y);
                log.print("[ ");
                for(Item item : PositionLUT.getInstance().getItemOnTile(currentTile)){
                    log.print(item.getShortName()+" ");
                }
                log.print("]");
            }
            log.println();
        }
    }

    /**
     * Writes out a map of players on the table
     * A 3x3 example of the representation of this map:
     * [E1, R2][][]
     * [][R3][B]
     * [][][E4]
     * @author Zsófi
     */
    public void printCharacterMap() {
        //hardcodeolt 6x6-os pálya, ha ez változik, akkor itt is ki kell szedni a hardcodeot
        Tile bearTile = PositionLUT.getInstance().getPosition(getInstance().polarbear);
        for (int y = 5; y >= 0; y--) {
            for(int x = 0; x < 6; x++) {
                Tile currentTile = PositionLUT.getInstance().getTile(x, y);
                log.print("[ ");
                for(Player p : PositionLUT.getInstance().getPlayersOnTile(currentTile)) {
                    log.print(p.getShortName()+" ");
                }
                if(currentTile.equals(bearTile)) {
                    log.print(getInstance().polarbear.getShortName()+" ");
                }
                log.print("]");
            }
            log.println();
        }
    }

    /**
     * Writes out the shelter on the table
     * @author Ádám
     */
    public void printShelterMap() {
        for (int y = 5; y >= 0; y--) {
            for(int x = 0; x < 6; x++){
                Tile currentTile = PositionLUT.getInstance().getTile(x,y);
                log.print("[ ");
                if(currentTile.getIglooOn()){
                    log.print("I");
                }
                else if(currentTile.tentOn){
                    log.print("T");
                }
                log.print("]");
            }
            log.println();
        }
    }

    /**
     * Writes out the thickness of snow on the table
     * @author Ádám
     */
    public void printSnowTileMap() {
        for (int y = 5; y >= 0; y--) {
            for(int x = 0; x < 6; x++){
                Tile currentTile = PositionLUT.getInstance().getTile(x,y);
                log.print("[ ");
                log.print(currentTile.getSnow());
                log.print("]");
            }
            log.println();
        }
    }

    /**
     * Prints out a map with the tile's capacities on it
     * @author Zsófi
     */
    public static void printCapacityTileMap() {
        for (int y = 5; y >= 0; y--) {
            for(int x = 0; x < 6; x++){
                Tile currentTile = PositionLUT.getInstance().getTile(x,y);
                log.print("[ ");
                log.print(currentTile.getCapacity());
                log.print("]");
            }
            log.println();
        }
    }

    /**
     * Writes out the Information about tables
     * @author Ádám
     */
    public void printTile(int x, int y) {
        Tile currentTile = PositionLUT.getInstance().getTile(x,y);
        log.format("Tile(%d, %d) > ", x, y);
        log.print("Capacity: ");
        log.print(currentTile.getCapacity());
        log.format(", Snow: %d\n", currentTile.getSnow());
    }

    /**
     * Writes out the Information about items.
     * @author Ádám
     */
    public void printItem() {
        for (int y = 5; y >= 0; y--) {
            for(int x = 0; x < 6; x++) {
                Tile currentTile = PositionLUT.getInstance().getTile(x,y);
                for(Item item : PositionLUT.getInstance().getItemOnTile(currentTile)){
                    log.print("[ ");
                    log.print(item.getShortName()+", ");
                    log.println("]");

                }
            }
        }
    }

    /**
     * Writes out the Information about players.
     * @author Ádám
     */
    public void printPlayer() {
        for(int i=0;i<PlayerContainer.getInstance().getPlayerNum();i++) {
            Player p = PlayerContainer.getInstance().getPlayer(i);
            log.println(p.getInformation());
        }
    }
    /**
     * The player pick item up
     * @author Ádám
     */
    public void pickUp(Item i){
        PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID()).pickUp(i);
        dirty = true;
        InGame.gamePanel.repaint();
    }

    /**
     * The player dig item up
     * @author Ádám
     */
    public void digItemUp(Item i) {
        PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID()).digItemUp(i);
        dirty = true;
        InGame.gamePanel.repaint();
    }

    /**
     * Eskimo build igloo
     * @author Ádám
     */
    public void buildIgloo(){
        Eskimo e= (Eskimo) PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID());
        e.buildIgloo();
        dirty = true;
        InGame.gamePanel.repaint();
    }
    /**
     * Researcher detect the capacity of table
     * @author Ádám
     */
    public void detectCapacity(Direction dir){
        Researcher r = (Researcher) PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID());
        r.detectCapacity(dir);
        dirty = true;
        InGame.gamePanel.repaint();
    }
    /**
     * Player clear snow.
     * @author Ádám
     */
    public void clearSnow(){
        PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID()).clearSnow();
        dirty = true;
        InGame.gamePanel.repaint();
    }
    /**
     * Player saves her pal
     * @author Ádám
     */
    public void savePlayers(Direction dir){
        PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID()).savePlayers(dir);
        dirty = true;
        InGame.gamePanel.repaint();
    }
    /**
     * Player build tent
     * @author Ádám
     */
    public void buildTent(){
        Player p= PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID());
        p.buildTent();
        dirty = true;
        InGame.gamePanel.repaint();
    }
    /**
     * Player put signalflare together
     * @author Ádám
     */
    public void putSignalTogether(){
        Player p= PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID());
        p.putSignalTogether(RoundController.getInstance().sg);
        dirty = true;
        InGame.gamePanel.repaint();
    }
    /**
     * Player pass round
     * @author Ádám
     */
    public void passRound(){
        PlayerContainer.getInstance().getPlayer(RoundController.getInstance().getcurID()).passRound();
        dirty = true;
        InGame.gamePanel.repaint();
    }
}
