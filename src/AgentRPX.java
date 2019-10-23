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
    public boolean isSafe = true;
    protected Board agentboard = new Board(this.getCoveredMap());



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
                unknown.add(new int[]{i, j});
            }

        }

        //two starting clues of probing in the top left hand corner and the centre
        double mid = (map.length/2);
        int m = (int)mid;
//        coveredMap[0][0] = map[0][0];
//        coveredMap[m][m] = map[m][m];


    }

    /**
     * probe a cell at (x,y)
     * @param x
     * @param y
     */
    public void probe(int x , int y) {
        if (answerMap[y][x] == 't') {
            isSafe = false;
            coveredMap[y][x] =  answerMap[y][x];
        }
        else {
            coveredMap[y][x] =  answerMap[y][x];
        }

        for (int i = 0; i < unknown.size(); i++) {
            if (unknown.get(i)[1] == y && unknown.get(i)[0] == x) {
                unknown.remove(i);
                break;
            }
        }


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
        ArrayList<int[]> front = this.unknown;
        Random rand = new Random();
        int index = rand.nextInt(front.size());
        int loc[] = front.get(index);
        probe(loc[1], loc[0]);
        rpxCount++;
        System.out.println("RPX: probe[" +loc[1] + "," + loc[0] + "]");
        Board agentBoard = new Board(this.getCoveredMap());
        agentBoard.printBoard();
        if (!this.isSafe) {
            System.out.println("A Tornado is in cell [" + loc[1] + "," + loc[0] + "]! Sorry you Lose :(");
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
