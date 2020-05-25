package TileClasses;

import Control.Game;
import PlayerClasses.Player;

public class SnowyHole extends Tile {

    public SnowyHole(int x, int y) {
        super(x, y);
        capacity = 0;
    }

    /**
     * Sets Igloo parameter of Tile to FALSE (iglooOn = FALSE)
     */
    @Override
    public boolean destroyIgloo(){
        Game.log.format("! SnowyHole>destroyIgloo : SnowyHole (%d, %d) cannot have Igloo! Cannot destroy 'iglooOn=false'\n", x, y);
        return false;
    }

    /**
     * Sets Igloo parameter of Tile to TRUE (iglooOn = TRUE)
     */
    @Override
    public boolean buildIgloo(){
        iglooOn = false;
        Game.log.format("! SnowyHole>buildIgloo : SnowyHole (%d, %d) cannot have Igloo! Cannot built 'iglooOn=false'\n", x, y);
        return false;
    }

    /**
     * If Player steps on a SnowyHole, falls in water
     * @param p player
     */
    @Override
    public void steppedOn(Player p) {
        standingHere++;
        Game.log.format("# SnowyHole>steppedOn : Stepped on SnowyHole(%d, %d)\n", x, y);
        p.fallInWater();
    }

    @Override
    public String toString() {
        if(standingHere > 0 )
            return "tileWater";
        if (snow > 0)
            return "tileSnow";
        return "tileSnow";
    }
}
