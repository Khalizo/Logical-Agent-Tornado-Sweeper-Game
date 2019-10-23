import java.util.ArrayList;
import java.util.Random;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public class AgentRPX {
    protected int rpxCount = 0; //counts the marked spots on the map
    protected char[][] coveredMap; //parts of the map that is covered
    protected char [][] answerMap; //Original map
    protected int maxX; //Max X coordinate of the map
    protected int maxY; //Max Y coordinate of the map
    public ArrayList<int[]> unknown = new ArrayList<int[]>(); //Unknown hexagons
    final char SIGN_UNKNOWN = '?'; //sign for unknown



    //the frontiers of uncovered cells
    public ArrayList<char[]> frontKnown = new ArrayList<char[]>();
    //the frontiers of covered cells
    public ArrayList<char[]> frontUnknown = new ArrayList<char[]>();


    /**
     * Constructor
     * @param map
     */
    public AgentRPX (char [][] map) {
        this.answerMap = map;
        maxX = map.length;
        maxY = map[0].length;
        coveredMap = new char[maxX][maxY];
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                coveredMap[i][j] = SIGN_UNKNOWN;
            }
        }

        //two starting clues of probing in the top left hand corner and the centre
        double mid = (map.length/2);
        int m = (int)mid;
        coveredMap[0][0] = map[0][0];
        coveredMap[m][m] = map[m][m];

    }

    /**
     * probe a cell at (x,y)
     * @param x
     * @param y
     */
    public void probe(int x , int y) {

    }

    public void uncoverCell () {
        Random rand = new Random();
        int randX = rand.nextInt(answerMap.length -1);
        int randY = rand.nextInt(answerMap.length -1);
        coveredMap[randY][randX] = answerMap[randY][randX];
        System.out.println("The agent just uncovered cell: " + "[" + randY + "," + randX + "]" );
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

        for (int i = 0; i <25; i++) {
            this.uncoverCell();
        }
        
    }

    /**
     * Getter for returning the covered map
     * @return
     */
    public char[][] getCoveredMap (){
        return this.coveredMap;
    }


}
