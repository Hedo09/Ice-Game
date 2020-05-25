package TileClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * @author adam
 * Directions. We can choose the Direction by value.
 */
public enum Direction {
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3),
    HERE(4);

    private int value;
    private static Map map= new HashMap<>();

    Direction(int value) {
        this.value=value;
    }
    static{
        for(Direction direction : Direction.values()){
            map.put(direction.value,direction);
        }
    }

    /**
     * Converts int to Direction
     * @param direction(int)
     * @return Direction(enum)
     */
    public static Direction valueOf(int direction){
        return (Direction)map.get(direction);
    }

    /**
     * Converts Direction to int
     * @param dir direction (enum)
     * @return int
     */
    public static int valueOf(Direction dir){ return (int)map.get(dir.value);}

    /**
     * Returns the value of Direction enum
     * @return value (int)
     */
    public int getValue(){ return value; }

    public String toString() {
        switch (value)
        {
            case 0:
                return "UP";
            case 1:
                return "DOWN";
            case 2:
                return "LEFT";
            case 3:
                return "RIGHT";
            case 4:
                return "HERE";
            default:
                return null;
        }
    }
}
