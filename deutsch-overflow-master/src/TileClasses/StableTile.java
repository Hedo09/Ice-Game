package TileClasses;

public class StableTile extends Tile {
    public StableTile(int x, int y) {
        super(x, y);
    }
    @Override
    public String toString() {
        if (snow > 0) return "tileSnow";
        return "tileNoSnow";
    }
}
