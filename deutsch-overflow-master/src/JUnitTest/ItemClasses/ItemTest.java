package JUnitTest.ItemClasses;

import Control.Game;
import ItemClasses.Item;
import ItemClasses.ItemState;
import ItemClasses.Shovel;
import PlayerClasses.Researcher;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testclass for the methods of Item
 */
public class ItemTest {

    @Test
    /**
     * Tests the methods of Item which change the state of the item
     */
    public void itemStateCheck() {
        Game.log=System.out;
        Item i=new Shovel();
        assertTrue(i.getState()== ItemState.frozen);
        i.diggedUp();
        assertTrue(i.getState()==ItemState.thrownDown);
        i.pickedUp(new Researcher(0));
        assertTrue(i.getState()==ItemState.inHand);
        i.thrownDown();
        assertTrue(i.getState()==ItemState.thrownDown);
    }
}