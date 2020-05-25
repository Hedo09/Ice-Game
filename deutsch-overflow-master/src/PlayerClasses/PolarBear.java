package PlayerClasses;

import Control.Game;
import GlobalControllers.GameEndCause;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import TileClasses.Direction;
import TileClasses.Tile;

import java.util.Random;

/**
 * Auto special Player
 * Functionalities:
 * - auto-step
 * - if it steps on a tile with a player on it, the game ends
 */
public class PolarBear {
    /**
     * PolarBear steps, PositionLUT will be refreshed
     * ERROR HANDLING: Tile hasn't neighbour on dir direction, then prints out "You can't go that way" message
     * @param dir Direction
     */
    private void step(Direction dir) throws IndexOutOfBoundsException {
        Tile position= PositionLUT.getInstance().getPosition(this);
        try {
            Tile next_tile = position.getNeighbour(dir);
            PositionLUT.getInstance().setPosition(this, next_tile);
            Game.log.println("$ PolarBear>step : Transaction 'PolarBearSteps' is completed");
        } catch (IndexOutOfBoundsException e) {
            Game.log.format("! PolarBear>step : PolarBear cannot go that way\n");
            throw e;
        }
    }

    /**
     * When the bear goes to hunt, it tries to step in random directions until the step is successful
     * (Note: the deterministic test game avoids this function and calls step directly instead with non-random values)
     * @author Zsófi
     */
    private void randomStep() {
        Random r = new Random();
        Direction direction = Direction.valueOf(r.nextInt(4));
        boolean stepped = false;
        while(!stepped) {
            try {
                stepped = true;
                step(direction);
            } catch (IndexOutOfBoundsException e) {
                Game.log.format("! PolarBear>randomStep : random step unsuccessful\n");
                stepped = false;
                direction = Direction.valueOf((direction.getValue()+1)%4); // ha nem sikerül lépnie, nem újat generál, csak elfordul
            }
        }
    }


    /**
     * The polarbear hunts for prey; it steps to a new tile and eats the players on it
     * for random stepping see randomStep method
     * about deterministic stepping: it starts in Direction DOWN and if it can't go that way
     * then tries to turn counterclockwise
     * @author Zsófi
     */
    public void hunt() {
        // bear steps
        // this part is almost the same, as randomStep, but without the random part;
        // we won't need the deterministic parts in the game (only for testing)
        if(Game.isDeterministic) {
            Direction direction = Direction.DOWN;
            boolean stepped = false;
            while(!stepped) {
                try {
                    stepped = true;
                    step(direction);
                }
                catch (IndexOutOfBoundsException e) {
                    stepped = false;
                    direction = Direction.valueOf((direction.getValue()+1)%4); // ha nem sikerül lépnie, nem újat generál, csak elfordul
                }
            }
        }
        else {
            randomStep();
        }

        // bear tries to hunt down the players
        int players_num;
        Tile bearTile = PositionLUT.getInstance().getPosition(this);
        players_num = PositionLUT.getInstance().getPlayersOnTile(bearTile).size();
        if (players_num > 0) {
            Game.log.println("! PolarBear>hunt : Bear killed the players on Tile");
            RoundController.getInstance().lose(GameEndCause.polarbear);
        }
        else {
            Game.log.println("! PolarBear>hunt : Bear has found no prey on his new Tile");
        }
        Game.log.println("! PolarBear>hunt : Hunting has ended");
    }

    public String getShortName() {
        return "B";
    }
    public String toString() {return "P";}
}
