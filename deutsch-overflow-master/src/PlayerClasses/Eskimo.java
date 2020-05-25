package PlayerClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import TileClasses.Tile;

/**
 * Specification of Player
 * Eskimo can build Igloo (special ability)
 */
public class Eskimo extends Player {
    /**
     * Sets Igloo on Tile, where the Eskimo is actually standing
     */
    public Eskimo(int id){
        super(id);
        BodyHeat=5;
    }
    public void buildIgloo() {
        Game.log.format("# Eskimo>buildIgloo : started by PlayerID: %d\n", ID);
        Tile tile = PositionLUT.getInstance().getPosition(this);
        boolean successful = tile.buildIgloo();
        if(successful) {
            workPoints--;
            if(workPoints==0) {
                Game.log.format("# Player>step : Player (PlayerId:%d) has no more workingPoints\n", ID);
                passRound();
            }
        }
        Game.log.println("# Eskimo>buildIgloo : ended");
    }
    @Override
    public String getShortName() {
        return "E"+ID;
    }

    @Override
    public String toString() {return "E-"+ ID;}

    @Override
    public String getInformation(){
        String ih;
        String wear;
        String inwater;
        if(inHand!=null)
            ih=inHand.getShortName();
        else
            ih="-";
        if(wearing!=null)
            wear="+";
        else
            wear="-";
        if(inWater)
            inwater="+";
        else
            inwater="-";
        return "( "+getShortName()+ ":   InHand "+ih+", Wear: "+wear+", "+"Temp: "+BodyHeat+", inWater: "+inwater+" )";
    }
}
