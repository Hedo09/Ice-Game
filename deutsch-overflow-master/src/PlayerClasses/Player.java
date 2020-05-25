package PlayerClasses;
import Control.Game;
import GlobalControllers.GameEndCause;
import GlobalControllers.PositionLUT;
import GlobalControllers.RoundController;
import ItemClasses.*;
import TileClasses.*;
import TileClasses.Direction;

import java.util.ArrayList;

/**
 * Class of Player
 * Functinalities:
 * -
 */
public abstract class Player {
    protected int BodyHeat;
    protected int ID;
    public int workPoints;
    public boolean inWater;
    protected Item inHand;
    protected Item wearing;
    public Direction saveDirection;

    /**
     * Initialisation for starting a round for Player
     * Sets workingPoint to 4
     */
    public Player(int id){
        inHand=null;
        inWater=false;
        wearing=null;
        workPoints=4;
        ID=id;
    }

    public void startRound() {
        workPoints = 4;
        Game.log.format("# Player>startRound : next Player is %d\n", ID);

        // check, if the player is freezing to death
        if(inWater && wearing == null) { RoundController.getInstance().lose(GameEndCause.water); }
    }

    /**
     * Called by the SnowyHole or UnstableTile
     * Sets inWater parameter of Player to TRUE
     */
    public void fallInWater() {
        inWater = true;
        Game.log.format("# Player>fallInWater : Player (PlayerId:%d) falls in Water \n", ID);
        if(wearing==null) {
            Game.log.format("# Player>fallInWater : Player (PlayerId:%d) has to wait for rescue \n", ID);
            passRound(); // ha beszakadnak, akkor is csak az passzol, aki éppen volt, többen nem
        }
        else {
            inWater = false; // Technically még maradhat a vízben, de nincs veszélyben!
            Game.log.format("# Player>fallInWater : Player (PlayerId:%d) has a diving suit, no worries \n", ID);
        }
    }
    /**
     * Called by Food, if its picked up. Food is automatically eaten if its picked up
     * Removes the Food from Player's hand and increments the bodyHeat of Player
     */
    public void ateFood() {
        inHand = null;
        changeBodyHeat(1);
        Game.log.format("$ Player>ateFood : Player (PlayerId:%d) ate the Food \n", ID);
    }

    /**
     * tent building
     */
    public void buildTent(){
        if(inHand != null)
            inHand.used(this, Activity.putUpTent);
        inHand = null;
    }

    /**
     * Changes the Player bodyHeat with thisMuch
     * -ateFood(1)
     * -snowStorm(-1)
     * @param thisMuch thisMUCH (+/-)
     */
    public void changeBodyHeat(int thisMuch) {
        Game.log.format("# Player>changeBodyHeat : Player (PlayerId:%d) bodyHeat is changed to %d (by %d much)\n", ID, BodyHeat+thisMuch, thisMuch);
        BodyHeat += thisMuch;
        if(BodyHeat==0){
            RoundController.getInstance().lose(GameEndCause.hypothermia);
        }
    }

    /**
     * Called by DivingSuit, if its pickedUp (its automatically worn if its picked up)
     * Sets the diving suit to a specific variable, and takes out from the Player's hand
     */
    public void wear(DivingSuit suit) {
        wearing = suit;
        inHand = null;
        Game.log.format("$ Player>wear : Player (PlayerId:%d) now wears DivingSuit\n", ID);
    }

    /**
     * Player picks up an item
     * It Player's hand already has an item it takes back to actual Tile
     * It also decrements of working points of the player
     * @param i item
     */
    public void pickUp(Item i) {
        Tile position = PositionLUT.getInstance().getPosition(this);
        int snow = position.getSnow();
        if(snow==0) {
            if (inHand != null) {
                if (inHand.getState() == ItemState.thrownDown) {
                    inHand.diggedUp();
                } else {
                    inHand.thrownDown();
                    PositionLUT.throwItemDown(inHand, position);
                    inHand = null;
                }
                Game.log.format("# Player>pickUp : Player (PlayerId:%d) old item from players hand has been removed and thrown down\n", ID);
            }
            inHand = i;
            i.pickedUp(this);
            PositionLUT.pickItemUp(i, position);
            workPoints--;
            Game.log.format("$ Player>pickUp : Player (PlayerId:%d) picked up item\n", ID);
        } else {
            Game.log.format("! Player>pickUp : Player (PlayerId:%d) item cannot picked up, Tile has snow\n", ID);
        }

        if(workPoints==0) {
            Game.log.format("# Player>pickUp : Player (PlayerId:%d) has no more workingPoints\n", ID);
            passRound();
        }
    }

    /**
     * Player clears snow
     * 1) Checks of the Player's hand has an item -> YES: it tries to clear snow with Shovel (1 unit of snow)
     * 2) (Another) unit of snow will be cleared from the Tile
     * It also decrements one working points from the player
     */
    public void clearSnow() {
        if (inHand != null) {
            if(inHand.getState()==ItemState.inHand){
                inHand.used(this,Activity.clearingSnow);
            }
        }

        Tile position= PositionLUT.getInstance().getPosition(this);
        if(position.getSnow()>0) {
            position.changeSnow(-1);
            Game.log.format("$ Player>clearSnow : Player (PlayerId:%d) cleared snow\n", ID);
            workPoints--;
        }

        if(workPoints==0) {
            Game.log.format("# Player>clearSnow : Player (PlayerId:%d) has no more workingPoints\n", ID);
            passRound();
        }

    }

    /**
     * Player digs to Item from Tile (calls the Tile to change the state of the item)
     * It also decrements of working points of the player
     * @param i item
     */
    public void digItemUp(Item i) {
        Tile t=PositionLUT.getInstance().getPosition(this);
        if(t.getSnow()==0){
            if(i.getState()==ItemState.frozen) {
                i.diggedUp();
                Game.log.format("$ Player>digItemUp : Player (PlayerId:%d) 'digItemUp' is completed\n", ID);
                workPoints--;
            }
            if(workPoints==0) {
                Game.log.format("# Player>digItemUp : Player (PlayerId:%d) has no more workingPoints\n", ID);
                passRound();
            }
        }
    }

    /**
     * Player saves another Players on neighbour Tile (defined by the direction)
     * It also decrements of working points of the player, if action is successful
     * Error handling: if direction is HERE, it makes no sense -> Reply message: "You can't save yourself"
     * @param dir direction
     */
    public void savePlayers(Direction dir) {
        Game.log.format("$ Player>savePlayers : Player (PlayerId:%d) save started in direction:%s\n", ID, dir.toString());
        if(dir==Direction.valueOf(4)) {
            Game.log.format("! Player>savePlayers : Player (PlayerId:%d) cannot rescue herself\n", ID);
            return;
        }
        saveDirection = dir;
        if(inHand != null) {
            inHand.used(this, Activity.savingPeople);
            workPoints--;
        }
        else {
            Game.log.format("! Player>savePlayers : Player (PlayerId:%d) has no rope\n", ID);
        }
        if(workPoints==0) {
            Game.log.format("! Player>savePlayers : Player (PlayerId:%d) has no more working points\n", ID);
            passRound();
        }
        Game.log.format("$ Player>savePlayers : Player (PlayerId:%d)'s save ended\n", ID);
    }

    /**
     * Player puts the signal flare together
     * Calls the putTogether() function of the SignalFlare-> calls the win() function of RoundController
     * if all Player and SignalFlarePart are on the same tile
     * @param sg signal flare
     */
    public void putSignalTogether(SignalFlare sg) {
        Game.log.format("$ Player>putSignalTogether : Player (PlayerId:%d) started putting together\n", ID);
        sg.putTogether(RoundController.getInstance());
    }

    /**
     * Player passes the round
     * Calls the endLastRound() function of RoundController
     */
    public void passRound() {
        if(ID==RoundController.getInstance().getcurID()) {
            Game.log.format("# Player>passRound : Player (PlayerId:%d) passed round\n", ID);
            RoundController.getInstance().endLastRound();
        }
    }

    /**
     * Function to set the Player's hand to null (FragileShovel can be used just 3 times,
     * after it disappears from Player's hand)
     */
    public void dropFragileShovel(){
        inHand = null;
    }


    /**
     * Realises stepping of a Player, changes the position of Player in PositionLUT
     * Error handling: if caller(Player) would like to step to a Tile where is a PolarBear: "Dangerous Direction" error message
     * Error handling: if direction given is HERE: "You stay where you were" error message
     * Error handling: if there isn't Tile in given direction: "You can't go that way" error message
     * Error message != Exception!
     * @param dir Direction where to step
     */
    public void step(Direction dir) {

        Game.log.format("$ Player>step : Player (PlayerId:%d) Transaction 'stepping' began\n", ID);
        if(dir.getValue() == 4) {
                Game.log.format("! Player>step : Player (PlayerId:%d) player has chosen HERE for step\n", ID);
                return;
            }

            Tile position= PositionLUT.getInstance().getPosition(this);
            try {
                Tile next_tile = position.getNeighbour(dir);
                Tile bear_position= PositionLUT.getInstance().getPosition(RoundController.getInstance().polarbear);
                if(next_tile.equals(bear_position)){
                    Game.log.format("! Player>step : Player (PlayerId:%d) cannot step in that direction(PolarBear)\n", ID);
                    return;
                }
                position.steppedOff(dir);
                PositionLUT.getInstance().setPosition(this, next_tile);

                next_tile.steppedOn(this);
            } catch (IndexOutOfBoundsException e) {
                Game.log.format("! Player>step : Player (PlayerId:%d) cannot step in that direction(OutBound)\n", ID);
                return;
            }
            workPoints--;
            if(workPoints==0) {
                Game.log.format("# Player>step : Player (PlayerId:%d) has no more workingPoints\n", ID);
                passRound();
            }
    }

    public void pullOut(Direction dir) {
        Game.log.format("$ Player>pullOut : Player (PlayerId:%d) Transaction 'pulling out' began\n", ID);
        if(dir.getValue() == 4) {
            Game.log.format("! Player>pullOut : Error state, Player (PlayerId:%d) player has chosen HERE for pullOut\n", ID);
            return;
        }

        Tile position= PositionLUT.getInstance().getPosition(this);
        try {
            Tile next_tile = position.getNeighbour(dir);
            Tile bear_position= PositionLUT.getInstance().getPosition(RoundController.getInstance().polarbear);
            if(next_tile.equals(bear_position)){
                Game.log.format("! Player>pullOut : Error state, Player (PlayerId:%d) cannot pullOut in that direction(PolarBear)\n", ID);
                return;
            }
            position.steppedOff(dir);
            PositionLUT.getInstance().setPosition(this, next_tile);
            next_tile.steppedOn(this);
            inWater = false;
        } catch (IndexOutOfBoundsException e) {
            Game.log.format("! Player>pullOut : Error state, Player (PlayerId:%d) cannot pullOut in that direction(OutBound)\n", ID);
            e.printStackTrace();
            return;
        }
    }

    public ArrayList<Item> getItemsOnHand() {
        ArrayList<Item> items = new ArrayList<>();
        if (inHand!= null) items.add(inHand);
        if (wearing!= null) items.add(wearing);
        return items;
    }
    public int getBodyHeat() {return BodyHeat;}
    public abstract String getInformation();
    public abstract String getShortName();
    public abstract String toString();

}
