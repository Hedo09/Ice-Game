package GlobalControllers;

import Control.Game;
import GUI.GameEndPopup;
import GUI.GamePanel;
import GUI.InGame;
import ItemClasses.SignalFlare;
import ItemClasses.Tent;
import PlayerClasses.PolarBear;
import PlayerClasses.PlayerContainer;
import SnowStorm.SnowStorm;
import TileClasses.Tile;
import javax.swing.JOptionPane;

import javax.swing.*;

public class RoundController {

    private static RoundController rc;
    private int curID; //current player ID, player ID: pc.players arrayban az indexszáma
    public SignalFlare sg;
    public SnowStorm ss;
    public PolarBear polarbear;
    public Tent tent;

    /**
     * Gives back a reference to the RoundController
     * @return RoundController
     */
    public static RoundController getInstance() {
        if (rc == null) {
            rc = new RoundController();
        }
        return rc;
    }

    private RoundController() {
        ss = new SnowStorm();
        sg = new SignalFlare(); //feltölti itemekkel a sajat ArrayList-ét, posLUT initnél mapon is ott lesz.
        polarbear= new PolarBear();
        //tent = new Tent();
    }

    /**
     * Initialises the game
     * Initialises the PlayContainer and curID
     * @param playerNum NumOfPlayer
     */
    public void init(int playerNum) { //a játékot inicializálja
        Game.log.format("# RoundController>init : RoundController initialised by %d players\n", playerNum);
        curID = 0;
    }

    /**
     * When a player passes, this method is called
     * It administrates the changes inbetween players, lets storm try once and then calls the next players round
     * If it is the end of a whole round (the last player had the highest ID):
     * polarbear steps
     */
    public void endLastRound() {
        Game.log.println("# RoundController>endLastRound : Round end started");
        curID = (curID + 1) % PlayerContainer.getInstance().getPlayerNum();
        ss.tryStorm();

        if(curID == 0) { // end of whole round
            Game.log.println("# RoundController>endLastRound : PolarBear goes to a hunt");
            polarbear.hunt();
        }
        checkTent(); // vihar után, hogy védjen a viharban
        Game.log.println("# RoundController>endLastRound : Round end ended");
        PlayerContainer.getInstance().getPlayer(curID).startRound();
    }

    /**
     * Lose of game -> System exit
     * @param cause String of reason
     */
    public void lose(GameEndCause cause) {
        Game.dirty = true;
        InGame.gamePanel.repaint();
        GameEndPopup.showPopup(cause);
        Game.log.format("! RoundController>lose : You lose the game, cause: %s\n", cause);
        Game.log.println("$ RoundController>lose : Game lost, ended");
        System.exit(1);
    }

    /**
     * Win of game -> System exit
     */
    public void win() {
        Game.dirty = true;
        InGame.gamePanel.repaint();
        GameEndPopup.showPopup(GameEndCause.win);
        Game.log.println("$ RoundController>win : Game won, ended");
        System.exit(1);
    }

    /**
     * Gives back the unique identifier ID of CURRENT Player (use to indexing etc.)
     * @return playerID
     */
    public int getcurID() {
        return curID;
    }

    /**
     * @author adam
     * Check the live of Tent. If the counter is 0, then tentOn parameter of Tile will change to false.
     */
    public void checkTent() {
        if(tent==null) return; // a storm elpusztítja rögtön, de utána rc ezt még meghívja -> kell a check
        tent.counter--;
        if (tent.counter == 0) {
            Tile tile = PositionLUT.getInstance().getTile(tent.x, tent.y);
            tile.tentOn = false;
            tent = null;
            Game.log.println("RoundController>checkTent : Tent is destroyed (reason: was too old)");
        }
    }

    public void destroyTent() {
        if(tent==null) return; // a storm elpusztítja rögtön, de utána rc ezt még meghívja -> kell a check
        Tile tile = PositionLUT.getInstance().getTile(tent.x, tent.y);
        tile.tentOn = false;
        tent = null;
        Game.log.println("RoundController>checkTent : Tent is destroyed (reason: storm)");
    }
}
