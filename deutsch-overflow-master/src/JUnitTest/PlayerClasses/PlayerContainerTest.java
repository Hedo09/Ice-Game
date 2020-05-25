package JUnitTest.PlayerClasses;

import Control.Game;
import PlayerClasses.PlayerContainer;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Testclass for the methods of PlayerContainer
 */
public class PlayerContainerTest {
    static PlayerContainer pc;

    @BeforeClass
    /**
     * Initialises PlayerContainer and Game before the tests with deterministic init
     */
    public static void init(){
        Game.log=System.out;
        Game.isDeterministic=true;
        PlayerContainer.Initialize(6, 0);
        pc = PlayerContainer.getInstance();
    }

    @Test
    /**
     * Tests the getPlayer method of PlayerContainer
     */
    public void getPlayerTest() {
        assertEquals(6, pc.getPlayerNum());
        for(int i=0;i<pc.getPlayerNum();i++){
            assertNotNull(pc.getPlayer(i));
        }
    }

    /**
     *Tests, that the getPlayer method throws an exception for invalid pID
     */
    @Test(expected=NullPointerException.class)
    public void getPlayerExceptionTest(){
        pc.getPlayer(pc.getPlayerNum());
    }
}