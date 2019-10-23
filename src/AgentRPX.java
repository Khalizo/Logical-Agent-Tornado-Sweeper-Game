import java.util.ArrayList;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public class AgentRPX {
    protected int rpxCount = 0; //counts the marked spots on the map
    protected char[][] coveredMap; //parts of the map that is covered
    protected int[][] answerMap; //Original map
    protected int maxX; //Max X coordinate of the map
    protected int maxY; //Max Y coordinate of the map
    public ArrayList<int[]> unknown = new ArrayList<int[]>(); //Unknown hexagons
    final char SIGN_UNKNOWN = '?'; //sign for unknown


    //the frontiers of uncovered cells
    public ArrayList<int[]> frontKnown = new ArrayList<int[]>();
    //the frontiers of covered cells
    public ArrayList<int[]> frontUnknown = new ArrayList<int[]>();


    /**
     * Constructor
     * @param map
     */
    public AgentRPX (int [][] map) {
        this.answerMap = map;
        maxX = map.length;
        maxY = map[0].length;
        coveredMap = new char[maxX][maxY];
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                coveredMap[i][j] = SIGN_UNKNOWN;
            }
        }

    }

    /**
     * probe a cell at (x,y)
     * @param x
     * @param y
     */
    public void probe(int x , int y) {

    }

    /**
     * show the current state of the map
     */

    public void showMap() {

    }

    /**
     * make a random probe for all the covered cells
     */
    public void rpx () {
        
    }




}
