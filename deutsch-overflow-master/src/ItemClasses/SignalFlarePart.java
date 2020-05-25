package ItemClasses;

import Control.Game;
import PlayerClasses.Player;

/**
 * Parts of SignalFlare
 */
public class SignalFlarePart extends Item {
    /**
     * @param partID unique identifier
     */
    private int partID;

    public SignalFlarePart(int id){
        partID = id;
    }

    @Override
    public void used(Player p, Activity a){
        Game.log.println("# SignalFlarePart>used");
    }

    @Override
    public String getShortName(){ return "SFP("+state.getShortName(getState())+")";}

    @Override
    public String toString() {return "signalflarePart"+partID+state.getShortName(getState());}
}
