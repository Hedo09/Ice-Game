package ItemClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import PlayerClasses.Player;
import TileClasses.Tile;

/**
 * @author adam
 *
 */

public class FragileShovel extends Shovel{
    private int counter;

    public FragileShovel(){
        counter=3;
    }

    /**
     * After step, increments the counter of FragileShovel. If the player can't clear more tile with this fragile shovel: throws down
     * @param p Player
     * @param a Activity
     */
    @Override
    public void used(Player p, Activity a){

        if(a == Activity.clearingSnow && counter>0){
            Tile t = PositionLUT.getInstance().getPosition(p);
            t.changeSnow(-1);
            counter--;
            Game.log.println("$ FragileShovel>used : Transaction 'clearingSnow' was successful");
        } else {
            Game.log.format("! FragileShovel>used : Activity is not 'clearingSnow' or counter is not: %d>0\n", counter);
        }

        if(counter==0){
            p.dropFragileShovel();
            Game.log.println("$ FragileShovel>used : Transaction 'dropFragileShovel' was successful");
        }
    }

    @Override
    public String getShortName(){ return "FS("+state.getShortName(getState())+")";}

    @Override
    public String toString() {return "fragileShovel"+state.getShortName(getState());}
}
