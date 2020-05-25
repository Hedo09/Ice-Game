package PlayerClasses;

import Control.Game;
import GlobalControllers.RoundController;
import ItemClasses.*;

import java.util.ArrayList;
import java.util.Random;

public class PlayerContainer {
    private static int playerNum;

    private static PlayerContainer pc;
    private static ArrayList<Player> players;
    /**
     * Gives back a reference to the PlayerContainer
     * @return
     */
    public static PlayerContainer getInstance() {
        if(pc == null) {
            Game.log.format("! PlayerContainer>getInstance : Sorry, instance does not exist THROWN NULLPOINTER EXCEPTION");
            throw new NullPointerException("PlayerContainer should be initialized");
        }
        return pc;
    }

    /**
     * Initialisation of PlayerContainer, creates a new PlayerContainer (if it's not created)
     * @param num
     */
    public static void Initialize(int num, int mapNum) {
        if(pc == null) {
            pc = new PlayerContainer(num);

            if(Game.isDeterministic){
                if(mapNum == 0){
                    detInit();
                    playerNum = 6;
                }
                else {
                    putTogetherInit();
                    playerNum = 3;
                }
            }
            else{
                playerNum = num;
                randInit();
            }

        }
    }

    /**
     * Gives back the numOfPlayer, quantity (count) of all player in game
     * @return numOfPlayer
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Private constructor for initialising
     * @param num quantity (count) of all player
     */
    private PlayerContainer(int num){

    }

    private static void detInit(){
        playerNum = 6;
        players = new ArrayList<Player>();
        players.add(new Eskimo(0));
        players.add(new Eskimo(1));
        players.add(new Researcher(2));
        players.add(new Researcher(3));
        players.add(new Researcher(4));
        players.add(new Researcher(5));

        players.get(2).inHand = new Rope();
        players.get(2).inHand.diggedUp();
        players.get(2).inHand.pickedUp(players.get(2));
        players.get(3).inHand = new Tent();
        players.get(3).inHand.diggedUp();
        players.get(3).inHand.pickedUp(players.get(3));
        players.get(5).inHand = new Shovel();
        players.get(5).inHand.diggedUp();
        players.get(5).inHand.pickedUp(players.get(5));
    }

    private static void putTogetherInit(){
        players = new ArrayList<Player>();
        players.add(new Eskimo(0));
        players.add(new Eskimo(1));
        players.add(new Researcher(2));
        players.get(0).inHand = RoundController.getInstance().sg.signalFlareParts.get(0);
        players.get(0).inHand.diggedUp();
        players.get(0).inHand.pickedUp(players.get(0));
        players.get(1).inHand = RoundController.getInstance().sg.signalFlareParts.get(1);
        players.get(1).inHand.diggedUp();
        players.get(1).inHand.pickedUp(players.get(1));
        players.get(2).inHand = RoundController.getInstance().sg.signalFlareParts.get(2);
        players.get(2).inHand.diggedUp();
        players.get(2).inHand.pickedUp(players.get(2));
    }

    private static void randInit(){
        players = new ArrayList<>();
        Random random = new Random();

        int count=0;
        for(int i = 0; i < playerNum; i++){
            int randNum = random.nextInt(100) + 1;
            if(randNum < 50)
                players.add(new Researcher(count));
            else
                players.add(new Eskimo(count));
            count++;
        }

    }
    /**
     * Gives back a Player based on PlayerId (pid)
     * ERROR HANDLING: throws NullPointerException by outindexing => SHOULD BE CHECKED BY CALLER
     * @param pid id of a Player
     * @return player of pid
     */
    public Player getPlayer(int pid) { // pid = players-ben az adott player indexe
        if(pid>=playerNum) {
            Game.log.format("! PlayerContainer>getPlayer : Sorry, that playerID does not exist THROWN NULLPOINTER EXCEPTION", pid);
            throw new NullPointerException("Player with that pid does not exist");
        }
        else return players.get(pid);
    }
}
