package PlayerClasses;

import TileClasses.Direction;
import ItemClasses.Item;
import ItemClasses.SignalFlare;

/**
 * Collection of functions
 */
public interface IControllable {

    void step(Direction dir);
    void pickUp(Item i);
    void clearSnow();
    void digItemUp();
    void savePlayers(Direction dir);
    void putSignalTogether(SignalFlare sg);
    void passRound();
}
