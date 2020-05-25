package TileClasses;

import Control.Game;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import ItemClasses.Tent;
import PlayerClasses.Player;
import PlayerClasses.PlayerContainer;

import java.util.Random;

/**
 * Class of Tile, the "small portions" of the game board
 * A Tile is indexed by x and y
 */
public abstract class Tile {
    /**
     *  @param capacity : max achievable capacity
     *  @param standingHere : the number of players currently standing on the tile (determined by the numOfPlayers on the Tile)
     *  @param snow : amount of snow currently on Tile
     *  @param iglooOn manages Igloo on Tile
     *  @param tentOn manages Tent on Tile
     */
    protected int capacity;
    public int standingHere;

    protected final int x,y;
    protected int snow;
    protected boolean iglooOn;
    public boolean tentOn;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        iglooOn = false;
        if(Game.isDeterministic){
            snow = (x+y)%4;
        }else {
            Random r = new Random();
            snow = r.nextInt(4) + 1;// snow lehet: 1, 2, 3, 4
        }
        capacity = PlayerContainer.getInstance().getPlayerNum(); // ennél nagyobb capacity-nek nincs értelme
        standingHere = 0;
    }

    /**
     * Makes nothing
     * It's relevant for inherited members like unstableTile and SnowyHole redefines this function
     * @param p player
     */
    public void steppedOn(Player p) { // void, cause this method's only job is to set it's object's attributes
        Game.log.format("# Tile>steppedOn : Player stepped on Tile (%d, %d)\n", x, y);
    }

    /**
     * Makes nothing
     * It's relevant for UnstableTile
     * @param dir direction
     */
    public void steppedOff(Direction dir) {
        Game.log.format("# Tile>steppedOff : Player left the Tile (%d, %d)\n", x, y);
    }

    /**
     * Called by the Player clearSnow () or Shovel used() or SnowsStorm tryStorm()
     * Changes the amount of snow based on actual snow
     * DIFFERENCE should be given
     * @param thisMuch DIFFERENCE
     */
    public void changeSnow(int thisMuch) {
        if(thisMuch<0 && snow>0)
            snow += thisMuch;
        else if(snow==0 && thisMuch>0)
            snow += thisMuch;
        Game.log.format("# Tile>ChangeSnow : Tile(%d, %d) snow level is: %d (it has been changed by %d much)\n", x, y, snow, thisMuch);
    }

    public void nullSnow(){
        snow = 0;
    }

    /**
     * Returns the amount of snow
     * @return amount of snow (unit)
     */
    public int getSnow(){
        return snow;
    }

    /**
     * Called by the Researcher
     * Returns the capacity of Tile
     * @return capacity
     */
    public int getCapacity(){
        return capacity;
    }

    /**
     * Tile gets it's neighbour Tile in a specific direction
     * @param dir direction
     * ERROR HANDLING:
     * @throws IndexOutOfBoundsException, when no neighbour in that direction => SHOULD CHECKED BY THE CALLER
     */
    public Tile getNeighbour(Direction dir) throws IndexOutOfBoundsException {
        int nx = this.x, ny = this.y;
        Game.log.format("# Tile>getNeighbour : Neighbour check for Tile (%d, %d) in Direction: %s \n",nx, ny, dir.toString());
        switch (dir.valueOf(dir.getValue())) {
            case DOWN:
                nx = x; ny = y - 1;
                break;
            case LEFT:
                nx = x-1; ny = y;
                break;
            case UP:
                nx = x; ny = y + 1;
                break;
            case RIGHT:
                nx = x + 1; ny = y;
                break;
            case HERE:
                Game.log.println("! Tile>getNeighbour: returned HERE");
                return this;
        }
        // nem itt kell lekezelni, ha nincs dir irányba tile, hanem ott, ahol a player input jön (IControllable)
        Game.log.format("# Tile>getNeighbour : NeighbourTile (%d, %d)\n", nx, ny);
        return PositionLUT.getInstance().getTile(nx, ny); // throws IndexOutOfBoundsException if, and only if there's no neighbour
    }

    /**
     * Called by SnowStorm: tryStorm()
     * Destroys Igloo on Tile (iglooOn = false)
     */
    public boolean destroyIgloo(){
        if(iglooOn){
            iglooOn = false;
            Game.log.format("# Tile>destroyIgloo : Igloo destroyed from Tile (%d, %d)'iglooOn=false'\n", x, y);
            return true; // volt egy iglu, "sikeres destroy"
        }
        else return false; // nem volt iglu rajta
    }

    /**
     * Called by Eskimo
     * Sets Igloo on Tile (iglooOn = true)
     */
    public boolean buildIgloo(){
        boolean successful = !iglooOn;
        iglooOn = true;
        Game.log.format("$ Tile>buildIgloo : Transaction 'buildIgloo' is completed. Igloo built on Tile (%d, %d) 'iglooOn=true'\n", x, y);
        return successful;
    }
    public boolean getIglooOn(){ return iglooOn;}

    /**
     * @author adam
     * Tent set up (tenOn = true). The parameters of the tent is set. (position and counter for count the live of tent.)
     */
    public void putOnTent(){
        tentOn=true;
        RoundController.getInstance().tent = new Tent();
        RoundController.getInstance().tent.counter = PlayerContainer.getInstance().getPlayerNum();
        RoundController.getInstance().tent.x=x;
        RoundController.getInstance().tent.y=y;
        Game.log.format("Tile>putOnTent : Tent built on Tile (%d, %d) 'iglooOn=true'\n", x, y);
    }

    /**
     * For PolarBear moving
     */
    public boolean equals(Tile tile){
        if(x==tile.x && y==tile.y) return true;
        return false;
    }

    public void setStandingHere(int sh){
          standingHere = sh;
    }
    public abstract String toString();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
