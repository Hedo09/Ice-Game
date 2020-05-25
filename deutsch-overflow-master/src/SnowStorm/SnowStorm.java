package SnowStorm;

import Control.Game;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import PlayerClasses.Player;
import TileClasses.Tile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SnowStorm {
    /**
     * Called by the RoundController after a player round
     * Based randomly => befall:
     * On some Tile selected randomly
     * 1) destroys Igloo on them
     * 2) hurts player on them (BodyHeat of players will decreased)
     * 3) puts snow on them
     * IF the game is deterministic:
     * 1) comes after
     */
    public void tryStorm() {
        ArrayList<Tile> values = new ArrayList<>(); // coordinates of tiles

        // determinisztikus vihar mindig az utolsó player köre után jön
        if (Game.isDeterministic && Game.stormy) {
            if (RoundController.getInstance().getcurID() == 0) { // 0, mert storm előtt már változik a curID
                Game.log.println("# SnowStorm>tryStorm : SnowStorm is coming");
                // tent és igloo rombolás, tent(2,5), igloo(0,0)
                // játékos sebzés -> ha már elfogyott a Heim a fejük felől
                values.add(PositionLUT.getInstance().getTile(2, 3));
                values.add(PositionLUT.getInstance().getTile(0, 5));
                stormTiles(values);
                Game.log.println("# SnowStorm>tryStorm : The storm is done");
            }
        }
        else if(!Game.isDeterministic) {
            Random rand = new Random();
            int number= rand.nextInt(100);

            if(number>30) {// ~70%
                Game.log.println("# SnowStorm>tryStorm : SnowStorm is NOT coming");
                return;
            }

            Game.log.println("# SnowStorm>tryStorm : SnowStorm is coming");
            JOptionPane.showMessageDialog(null, "A storm is coming!", "Storm", JOptionPane.WARNING_MESSAGE);
            int x,y;
            do {
                x = rand.nextInt(PositionLUT.getInstance().getTileRowSize());
                y = rand.nextInt(PositionLUT.getInstance().getTileColumnSize());
                Tile t = PositionLUT.getInstance().getTile(x,y);
                if(!values.contains(t))
                    values.add(t);
            } while(values.size()<4);
            stormTiles(values);
            Game.log.println("# SnowStorm>tryStorm : The storm is done");
        }
    }

    private void stormTiles(ArrayList<Tile> tiles) {
        for(Tile tile : tiles){
            if(!tile.destroyIgloo() && !tile.tentOn) { // destroy true, ha volt ott igloo és elpusztítottuk - ha nem volt=>sebzünk
                for (Player p : PositionLUT.getInstance().getPlayersOnTile(tile)) {
                    Game.log.println("# SnowStorm>tryStorm : player");
                    p.changeBodyHeat(-1); //change bodyheat of players, who stand on this tile
                }
            }
            RoundController.getInstance().destroyTent();
            tile.changeSnow(1);
        }
    }
}
