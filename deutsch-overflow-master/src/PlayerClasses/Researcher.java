package PlayerClasses;

import Control.Game;
import GUI.GamePanel;
import GUI.InGame;
import GlobalControllers.PositionLUT;
import TileClasses.Direction;
import TileClasses.Tile;

public class Researcher extends Player {

    public Researcher(int id){
        super(id);
        BodyHeat=4;
    }
    /**
     * Special function of the player
     * Researcher is able to detect the capacity of a Tile in a given direction
     * It prints out the capacity of the neighbour Tile
     * ERROR HANDLING: dir should be checked before the call of this method (if tile is OutOfBound)
     * @param dir direction
     */
    public void detectCapacity(Direction dir) {
        Game.log.format("# Researcher>detectCapacity started by Player %d\n", ID);

        try {
            Tile thisTile = PositionLUT.getInstance().getPosition(this);
            Tile checkedTile = thisTile.getNeighbour(dir);
            int capacity = checkedTile.getCapacity();
            GamePanel.capacityEnabled = true;
            GamePanel.capacityOnTile = capacity;
            GamePanel.capacityX = checkedTile.getX();
            GamePanel.capacityY = checkedTile.getY();
            Game.log.println("$ Researcher>detectCapacity : Transaction 'detectCapacity' is completed");
            System.out.println("! Researcher>detectCapacity : Tile capacity: " + capacity);
            this.workPoints--;
            if(this.workPoints<=0){
                InGame.gamePanel.repaint();
                this.passRound();
            }
        }
        catch(IndexOutOfBoundsException e){
            Game.log.format("! Researcher>detectCapacity : No tile in that direction(OutBound)\n");
            return;
        }
    }
    @Override
    public String getShortName() {
        return "R"+ID;
    }

    @Override
    public String toString() {return "R-"+ ID;}

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
