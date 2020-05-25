package GlobalControllers;

import ItemClasses.*;
import PlayerClasses.Player;
import PlayerClasses.PlayerContainer;
import PlayerClasses.PolarBear;
import TileClasses.SnowyHole;
import TileClasses.StableTile;
import TileClasses.Tile;
import TileClasses.UnstableTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A singleton Look-Up-Table
 * It includes HashMaps between Item - Tiles , Tiles - PLayers ..
 */
public class PositionLUT {
    protected static PositionLUT pLUT;
    /**
     * Gives back the reference of PositionLUT
     */
    public static PositionLUT getInstance() {
        if(pLUT == null) {
            pLUT = new PositionLUT();
        }
        return pLUT;
    }

    private static HashMap<Item, Tile> itemTileMap;
    private static HashMap<Tile, ArrayList<Item>> tileItemMap;
    private static HashMap<Player, Tile> playerTileMap;
    private static HashMap<PolarBear,Tile> polarbearTileMap;
    private static HashMap<Tile,ArrayList<PolarBear>> tilePolarBearMap;
    private static HashMap<Tile, ArrayList<Player>> tilePlayerMap;
    private static ArrayList<ArrayList<Tile>> tileList;//y: array index, x: Tile index

    /**
     * Randomly generated fields are omitted in skeleton, we have a hardcoded map
     * at this phase of testing for the scenarios
     * */
    private PositionLUT(){}

    /**
     * @param isDeterministic
     * @param mapNum
     */
    public static void Initialize(boolean isDeterministic, int mapNum){
        if(isDeterministic && mapNum ==0){
            initDet1();
        }
        else if(isDeterministic && mapNum ==1){
            putTogetherInit();
        }
        else{
            randInit();
        }
    }

    /**
     * deterministic map initialisation for test cases
     */
    private static void initDet1(){
        tileList = new ArrayList<>();

        for(int y = 0; y <6; y++){
            ArrayList<Tile> row= new ArrayList<>();
            for(int x = 0; x < 6; x++){
                row.add(new StableTile(x,y));
            }
            tileList.add(row);
        }

        tileList.get(4).set(2, new SnowyHole(2,4));
        //hó nullázás a könnyebb ásás érdekében
        tileList.get(5).get(0).nullSnow();
        tileList.get(4).get(3).nullSnow();

        tileItemMap = new HashMap<>();
        tilePlayerMap = new HashMap<>();
        tilePolarBearMap=new HashMap<>();
        for(int y = 0; y<6; y++){//init, h mindenhol legyen
            for(int x = 0; x< 6; x++){
                tilePlayerMap.put(getTile(x,y),new ArrayList<>() );
                tileItemMap.put(getTile(x,y),new ArrayList<>() );
                tilePolarBearMap.put(getTile(x,y),new ArrayList<>());
            }
        }

        ArrayList<Item> items1 = new ArrayList<>();
        items1.add(new Food());
        tileItemMap.put(getTile(0,5),items1 );
        ArrayList<Item> items2 = new ArrayList<>();
        items2.add(new DivingSuit());
        tileItemMap.put(getTile(3,4),items2 );

        itemTileMap = new HashMap<>();
        itemTileMap.put(items1.get(0), getTile(0, 5));
        itemTileMap.put(items2.get(0), getTile(3, 4));


        ArrayList<Item> sf1 = new ArrayList<>();
        sf1.add(RoundController.getInstance().sg.signalFlareParts.get(0));
        tileItemMap.put(getTile(1,2),sf1 );//signalflarepart 0ID
        ArrayList<Item> sf2 = new ArrayList<>();
        sf2.add(RoundController.getInstance().sg.signalFlareParts.get(1));
        tileItemMap.put(getTile(2,2),sf2 );//signalflarepart 1ID
        ArrayList<Item> sf3 = new ArrayList<>();
        sf3.add(RoundController.getInstance().sg.signalFlareParts.get(2));
        tileItemMap.put(getTile(2,1),sf3 );//signalflarepart 2ID

        itemTileMap.put(RoundController.getInstance().sg.signalFlareParts.get(0), getTile(1,2));
        itemTileMap.put(RoundController.getInstance().sg.signalFlareParts.get(1), getTile(2,2));
        itemTileMap.put(RoundController.getInstance().sg.signalFlareParts.get(2), getTile(2,1));


        playerTileMap = new HashMap<>();
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(0),  getTile(0,5));//eskimo1
        getTile(0,5).setStandingHere(1);
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(1),  getTile(1,4));//eskimo2
        getTile(1,4).setStandingHere(1);
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(2),  getTile(2,5));//researcher1
        getTile(2,5).setStandingHere(1);
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(3),  getTile(2,3));//researcher2
        getTile(2,3).setStandingHere(1);
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(4),  getTile(3,4));//researcher3
        getTile(3,4).setStandingHere(1);
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(5),  getTile(4,3));//researcher4
        getTile(4,3).setStandingHere(1);

        ArrayList player1 = new ArrayList();
        player1.add(PlayerContainer.getInstance().getPlayer(0));
        tilePlayerMap.put(getTile(0,5), player1);
        ArrayList player2 = new ArrayList();
        player2.add(PlayerContainer.getInstance().getPlayer(1));
        tilePlayerMap.put(getTile(1,4), player2);
        ArrayList player3 = new ArrayList();
        player3.add(PlayerContainer.getInstance().getPlayer(2));
        tilePlayerMap.put(getTile(2,5), player3);
        ArrayList player4 = new ArrayList();
        player4.add(PlayerContainer.getInstance().getPlayer(3));
        tilePlayerMap.put(getTile(2,3), player4);
        ArrayList player5 = new ArrayList();
        player5.add(PlayerContainer.getInstance().getPlayer(4));
        tilePlayerMap.put(getTile(3,4), player5);
        ArrayList player6 = new ArrayList();
        player6.add(PlayerContainer.getInstance().getPlayer(5));
        tilePlayerMap.put(getTile(4,3), player6);


        polarbearTileMap = new HashMap<>();
        polarbearTileMap.put(RoundController.getInstance().polarbear,getTile(0,4)); //polarbear

        ArrayList polarbear= new ArrayList();
        polarbear.add(RoundController.getInstance().polarbear);
        tilePolarBearMap.put(getTile(0,4),polarbear);
    }

    /**
     * deterministic map for putTogether test
     */
    private static void putTogetherInit() {
        tileList = new ArrayList<>();
        tilePlayerMap = new HashMap<>();
        polarbearTileMap = new HashMap<>();
        tilePolarBearMap = new HashMap<>();
        tileItemMap = new HashMap<>();
        playerTileMap = new HashMap<>();

        for(int y = 0; y <6; y++){
            ArrayList<Tile> row= new ArrayList<>();
            for(int x = 0; x < 6; x++){
                row.add(new StableTile(x,y));
            }
            tileList.add(row);
        }

        for(int y = 0; y<6; y++){//init, h mindenhol legyen
            for(int x = 0; x< 6; x++){
                tilePlayerMap.put(getTile(x,y),new ArrayList<>() );
                tileItemMap.put(getTile(x,y),new ArrayList<>() );
                tilePolarBearMap.put(getTile(x,y),new ArrayList<>());
            }
        }


        ArrayList player1 = new ArrayList();
        player1.add(PlayerContainer.getInstance().getPlayer(0));
        tilePlayerMap.put(getTile(0,0), player1);
        getTile(0,0).setStandingHere(1);
        ArrayList players23 = new ArrayList();
        players23.add(PlayerContainer.getInstance().getPlayer(1));
        players23.add(PlayerContainer.getInstance().getPlayer(2));
        tilePlayerMap.put(getTile(1,0), players23);
        getTile(1,0).setStandingHere(2);

        playerTileMap.put(PlayerContainer.getInstance().getPlayer(0), getTile(0,0));
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(1), getTile(1,0));
        playerTileMap.put(PlayerContainer.getInstance().getPlayer(2), getTile(1,0));

        polarbearTileMap = new HashMap<>();
        polarbearTileMap.put(RoundController.getInstance().polarbear,getTile(0,4)); //polarbear
        ArrayList polarbear= new ArrayList();
        polarbear.add(RoundController.getInstance().polarbear);
        tilePolarBearMap.put(getTile(0,4),polarbear);
    }

    /**
     * random initialisation, based on probability.
     * players and items spawn on different Tiles
     * Snowyhole is not allowed as spawnplace for players and items
     * Polarbear can spawn anywhere
     * at the beginning polarbear and player can't be on the same tile
     */
    public static void randInit(){
        tileList = new ArrayList<>();
        itemTileMap = new HashMap<>();
        tileItemMap = new HashMap<>();
        playerTileMap = new HashMap<>();
        tilePlayerMap = new HashMap<>();
        polarbearTileMap = new HashMap<>();
        tilePolarBearMap = new HashMap<>();
        int [][] spawnMatrix = new int[6][6]; //segedmatrix, h ne rakjunk lyukra embert meg itemet


        Random random = new Random();
        for(int y = 0; y<6; y++){   //filling up tileList based on probablity
            tileList.add(new ArrayList<>());
            for(int x = 0; x<6; x++ ){
                int randNum = random.nextInt(100) + 1;//randNum :1-100
                if( randNum <= 60 ) {
                    tileList.get(y).add(new StableTile(x, y));//60%
                    spawnMatrix[y][x] = 1;
                }else if(randNum<=90) {
                    tileList.get(y).add(new UnstableTile(x, y));//30%
                    spawnMatrix[y][x] = 1;
                }else {
                    tileList.get(y).add(new SnowyHole(x, y));//10%
                    spawnMatrix[y][x] = 0;
                }
            }
        }
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Shovel());
        items.add(new FragileShovel());
        items.add(new DivingSuit());
        items.add(new Food());
        items.add(new Rope());
        items.add(new Tent());
        items.add(RoundController.getInstance().sg.signalFlareParts.get(0));
        items.add(RoundController.getInstance().sg.signalFlareParts.get(1));
        items.add(RoundController.getInstance().sg.signalFlareParts.get(2));


        int [][] itemSpawnMatrix = spawnMatrix;
        for(int i = 0 ;i < items.size(); i++) {
            int x = random.nextInt(6);
            int y = random.nextInt(6);
            do {
                x = random.nextInt(6);
                y = random.nextInt(6);
                itemTileMap.put(items.get(i), getTile(x, y)); //random hely, ami nem luk és csak 1 Item spawnolhat 1 helyre
            } while (itemSpawnMatrix[y][x] == 0);
            itemSpawnMatrix[y][x] = 0;
        }


        for(int i = 0; i < 36; i++){
            ArrayList<Item> item = new ArrayList<>();
            tileItemMap.put(tileList.get(i/6).get(i%6), item);//filling up with empty lists
        }

        //filling up tileItemMap
        for(int i = 0 ; i < items.size(); i++) {
            ArrayList<Item> item = new ArrayList<>();
            item.add(items.get(i));
            tileItemMap.put(itemTileMap.get(items.get(i)), item);
        }


        //polarBear position init, most csak 1 db van
        int x = random.nextInt(6);
        int y = random.nextInt(6);
        polarbearTileMap.put(RoundController.getInstance().polarbear, getTile(x, y));//akarhol lehet, vízben is
        spawnMatrix[y][x] = 0;

        for(int yy =0 ; yy < 6; yy++){
            for(int xx = 0; xx <6; xx++){
                tilePolarBearMap.put(getTile(xx, yy), new ArrayList<>());
            }
        }
        ArrayList<PolarBear> bearList = new ArrayList<>();
        bearList.add(RoundController.getInstance().polarbear);
        tilePolarBearMap.put(getTile(x, y),bearList);


        for(int i = 0; i < PlayerContainer.getInstance().getPlayerNum(); i++){
            x = random.nextInt(6);
            y = random.nextInt(6);
            do {
                x = random.nextInt(6);
                y = random.nextInt(6);
                playerTileMap.put(PlayerContainer.getInstance().getPlayer(i),getTile(x, y));

            } while (spawnMatrix[y][x] == 0);
            getTile(x,y).setStandingHere(1);
            spawnMatrix[y][x] = 0;
        }

        for(int i = 0; i < 36; i++){
            ArrayList<Player> player = new ArrayList<>();
            tilePlayerMap.put(tileList.get(i/6).get(i%6), player);//filling up with empty lists
        }

        for(int i = 0 ; i <  PlayerContainer.getInstance().getPlayerNum(); i++) {
            ArrayList<Player> player = new ArrayList<>();
            player.add(PlayerContainer.getInstance().getPlayer(i));
            tilePlayerMap.put(playerTileMap.get(player.get(0)), player);
        }


    }

        /**
         * Gives back position (Tile) of a Player (p)
         * @param p Player
         * @return position(Tile)
         */
    public static Tile getPosition(Player p){
        return playerTileMap.get(p);
    }

    /**
     * Gives back position (Tile) of an Item (i)
     * @param i Item
     * @return position(Tile)
     */
    public static Tile getPosition(Item i){
        return itemTileMap.get(i);
    }

    /**
     * Gives back all player on a specific Tile
     * @param t position (Tile)
     * @return ArrayList<Item> players of the Tile
     */
    public static ArrayList<Player> getPlayersOnTile(Tile t){
        return tilePlayerMap.get(t);
    }

    /**
     * Gives back all item on a specific Tile
     * @param t position (Tile)
     * @return ArrayList<Item> items of the Tile
     */
    public static ArrayList<Item> getItemOnTile(Tile t){
        return tileItemMap.get(t);
    }

    /**
     * Gets two integers
     * @param x Descartes coord. x (Row)
     * @param y Descartes coord. y (Column)
     * @return position (Tile)
     */
    public static Tile getTile(int x, int y) {
        return tileList.get(y).get(x); //indexing convention
    }

    /**
     * Sets position (Tile - t) of the Player (p)
     * @param p Player
     * @param t Tile
     */
    public static void setPosition(Player p, Tile t){
        tilePlayerMap.get(playerTileMap.get(p)).remove(p);
        tilePlayerMap.get(t).add(p);//uj hely add
        playerTileMap.put(p, t);//put folulirja az elozot
    }//kell frissiteni: tilePlayerMap, playerTileMap

    /**
     * Sets the position (Tile - t) of an Item (i)
     * @param i Item
     * @param t position (Tile)
     */
    public void setPosition(Item i, Tile t){
        tileItemMap.get(itemTileMap.get(i)).remove(i);
        tileItemMap.get(t).add(i);//uj hely add
        itemTileMap.put(i, t);//put folulirja az elozot
    }

    /**
     * Gives back the position (Tile) of PolarBear
     * @param pb PolarBear
     * @return position(Tile)
     */
    public Tile getPosition(PolarBear pb){
        return polarbearTileMap.get(pb);
    }


    /**
     * Sets the position (Tile - t) of the PolarBear(pb)
     * @param pb PolarBear
     * @param t position(Tile)
     */
    public void setPosition(PolarBear pb, Tile t){
        tilePolarBearMap.get(polarbearTileMap.get(pb)).remove(pb);
        tilePolarBearMap.get(t).add(pb);//uj hely add
        polarbearTileMap.put(pb, t);//put folulirja az elozot
    }

    /**
     * @return the number of tiles in one row (max of x Coord + 1)
     */
    public int getTileRowSize() {
        return tileList.size();
    }

    /**
     * @return the number of tiles in one column (max of y Coord + 1)
     */
    public int getTileColumnSize() {
        return tileList.get(0).size();
    }

    /**
     * removes Item from tileItemMap & itemTileMap
     * called from Player.pickUp
     */
    public static void pickItemUp(Item item, Tile t){
        tileItemMap.get(t).remove(item);
        itemTileMap.remove(item);
    }

    /**
     * adds Item to tileItemMap & itemTileMap
     * called from Player.pickUp
     */
    public static void throwItemDown(Item item, Tile t){
        tileItemMap.get(t).add(item);
        itemTileMap.put(item, t);
    }
}
