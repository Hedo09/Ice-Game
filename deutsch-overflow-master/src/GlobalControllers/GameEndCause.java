package GlobalControllers;

public enum GameEndCause {
    polarbear ("The polarbear's hunt was successful", "src/GUI/Pack/meat.png"),
    hypothermia ("You lost because of hypothermia", "src/GUI/Pack/hypothermia.png"),
    water ("A player froze in the water", "src/GUI/Pack/froze_in_water.png"),
    win ("You are being rescued! You won the game!", "src/GUI/Pack/win.png");

    public String output, iconName;

    GameEndCause(String _output, String _iconName) { output = _output; iconName = _iconName; }
}
