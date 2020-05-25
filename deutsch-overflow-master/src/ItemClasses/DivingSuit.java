package ItemClasses;

import Control.Game;
import PlayerClasses.Player;

public class DivingSuit extends Item{

    /**
     * Player picks up the DivingSuit
     * Automatically worn, if picked up
     */
    @Override
    public void pickedUp(Player p){
        p.wear(this);
        Game.log.println("$ DivingSuit>pickedUp : Transaction 'pickingUpDivingSuit' was successful");
    }

    @Override
    public String getShortName(){ return "D("+state.getShortName(this.getState())+")";}

    @Override
    public String toString() {return "divingSuit"+state.getShortName(getState());}
}
