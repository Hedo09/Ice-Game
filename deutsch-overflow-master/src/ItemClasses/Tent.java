package ItemClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import PlayerClasses.Player;
import PlayerClasses.PlayerContainer;
import TileClasses.Tile;

/**
 * @author adam
 */

public class Tent extends Item{
    public int counter;
    /**
     * @param x,y Position of Tile, where the Tent located
     */
    public int x,y;


    public Tent(){
        super();
        counter = PlayerContainer.getInstance().getPlayerNum();
    }
    /**
     * Called by RoundController
     * Player(p) puts Tent on Tile
     * If Activity (a) is 'putUpTent' and Tile hasn't Iglu
     * Places a Tent to the caller (Player) position
     * @param p Player
     * @param a Activity
     */
    @Override
    public void used(Player p, Activity a){
        Tile t = PositionLUT.getInstance().getPosition(p);
        if(a == Activity.putUpTent && !t.getIglooOn()&& t.getCapacity()>0){
            t.putOnTent();
            p.workPoints--;
            Game.log.println("$ Tent>used : Transaction 'puttingTent' was successful");
        } else {
            Game.log.format("! Tent>used : Activity is not 'putUpTent' or Tile has Igloo : hasIgloo=%s\n", t.getIglooOn() );
        }
        if(p.workPoints<=0){
            p.passRound();
        }
    }
    @Override
    public String getShortName(){ return "T("+state.getShortName(getState())+")";}

    @Override
    public String toString() { return "tent"+state.getShortName(getState());}

}
