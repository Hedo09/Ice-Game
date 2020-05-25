package ItemClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import PlayerClasses.Player;
import TileClasses.Tile;

import java.util.ArrayList;
import java.util.Random;

public class Food extends Item{

    /**
     * If picked up, gets automatically eaten and respawns on a randomly selected tile,
     * in frozen state.
     * In skeleton random values are omitted, so it respawns on it's initial tile again.
     * @param p Player
     */
    @Override
    public void pickedUp(Player p) {
        ArrayList<Tile> possibleRespawnTiles = new ArrayList<>();
        for(int x = 0; x < PositionLUT.getInstance().getTileRowSize(); x++) {
            for(int y = 0; y < PositionLUT.getInstance().getTileColumnSize(); y++) {
                Tile t = PositionLUT.getTile(x,y);
                if(PositionLUT.getItemOnTile(t).size()==0) { possibleRespawnTiles.add(t); }
            }
        }
        Random r = new Random();
        int randomTileIndex = r.nextInt(possibleRespawnTiles.size());
        PositionLUT.getInstance().throwItemDown(this, possibleRespawnTiles.get(randomTileIndex));
        state = ItemState.frozen;
        p.ateFood();
        Game.log.println("$ Food>pickedUp : Transaction 'pickUp' and 'ateFood' was successful");
    }

    @Override
    public String getShortName(){ return "F("+state.getShortName(getState())+")";}

    @Override
    public String toString() {return "food"+state.getShortName(getState());}
}
