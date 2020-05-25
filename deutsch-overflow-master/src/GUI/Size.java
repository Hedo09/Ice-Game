package GUI;

public enum Size {
    verySmall(10), //3x3 grid Tile
    small(15), //2x2 grid Tile
    medium(20), //PlayerBar (right-upper corner)
    big(30);   //StatusBar (right-down corner)

    public final int i;
    Size(int i) {
        this.i = i;
    }
}