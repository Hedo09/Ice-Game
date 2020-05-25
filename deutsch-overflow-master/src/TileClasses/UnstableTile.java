package TileClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import PlayerClasses.Player;

import java.util.ArrayList;
import java.util.Random;

public class UnstableTile extends Tile {

    public UnstableTile(int x, int y) {
        super(x, y);

        Random rand= new Random();
        capacity= rand.nextInt(3) + 1;
    }

    /**
     * Called by Character
     * It increases the capacity of Tile => standinghere-- (somebody has left the Tile)
     * @param dir direction
     */
    @Override
    public void steppedOff(Direction dir) {
        standingHere--;
        Game.log.format("# Tile>steppedOff : Player left the Tile (%d, %d) and standingHere changed to %d\n", x, y, standingHere);
    }


    /**
     * Called by the Character
     * It decreases the capacity of Tile => standinghere++ (somebody entered on Tile)
     * It also check if the OVERWEIGHT OF TILE, in case: all Player fall in water
     * @param p player
     */
    @Override
    public void steppedOn(Player p) {
        standingHere++;
        if(capacity-standingHere<0){
            Game.log.format("# Tile>steppedOn : Player stepped on Tile (%d, %d) capacity %d is not enough for %d players\n", x, y, capacity, standingHere);
            Tile t = PositionLUT.getInstance().getPosition(p);
            ArrayList<Player> players = PositionLUT.getInstance().getPlayersOnTile(t);
            for (Player player : players) {
                // az utoljára rálépő a current player, aki passzolni is fog, ezért o esik bele utoljára, hogy kor vege elott lemenjen a beeses
                if(player!=p)
                    player.fallInWater();
            }
            p.fallInWater();
        } else {
            Game.log.format("# Tile>steppedOn : Player stepped on Tile (%d, %d) and standingHere changed to %d\n", x, y, standingHere);
        }
    }


    @Override
    public String toString() {
        if (capacity < standingHere)
            return "tileWater";
        if (snow > 0)
            return "tileSnow";
        return "tileNoSnow";
    }
}
