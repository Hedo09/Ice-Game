package ItemClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import PlayerClasses.Player;
import TileClasses.Tile;

public class Shovel extends Item{
    /**
     * Called by RoundController
     * Player (p) uses Shovel
     * If Activity (a) is 'clearingSnow' decreases the snow by 1 on Tile, where the caller Player is
     * @param p Player
     * @param a Activity
     */
    @Override
    public void used(Player p, Activity a){
        if(a == Activity.clearingSnow){
            Tile t = PositionLUT.getInstance().getPosition(p);
            t.changeSnow(-1);
            Game.log.println("$ Shovel>used : Transaction 'clearingSnow' was successful");
        } else {
            Game.log.println("! Shovel>used : Activity is not 'clearingSnow'");
        }
    }

    @Override
    public String getShortName(){ return "S("+state.getShortName(getState())+")";}

    @Override
    public String toString() {return "shovel"+state.getShortName(getState());}
}
