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
    protected int tornadoesToMark = 0;
    public ArrayList<int[]> unknown = new ArrayList<int[]>(); //Unknown hexagons
    final char SIGN_UNKNOWN = '?'; //sign for unknown
    final char SIGN_MARK = 'D'; //sign for unknown
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
                if (map[i][j] == 't'){
                    tornadoesToMark++;
                }
                unknown.add(new int[]{i, j});
            }

        }


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
            if (unknown.get(i)[0] == y && unknown.get(i)[1] == x) {
                unknown.remove(i);
                break;
            }
        }

        if (answerMap[y][x] == '0') {
            uncoverZeroNeighbours(x, y);
        }


    }

    /**
     * probe the adjacent cells of 0
     * @param x
     * @param y
     */


    public void uncoverZeroNeighbours (int x, int y  ) {

        if (x - 1 >= 0  && y -1 >= 0  && coveredMap[y-1][x-1] == SIGN_UNKNOWN){
            probe(x -1, y-1);
        }

        if (x - 1 >= 0 && coveredMap[y][x-1] == SIGN_UNKNOWN){
            probe(x-1, y);
        }

        if (y + 1 < maxY && coveredMap[y+1][x] == SIGN_UNKNOWN){
            probe(x, y +1 );
        }

        if (x + 1 < maxX && y + 1< maxY && coveredMap[y+1][x +1] == SIGN_UNKNOWN){
            probe(x + 1, y +1 );
        }

        if (x + 1 < maxX  && coveredMap[y][x +1] == SIGN_UNKNOWN){
            probe(x + 1, y);
        }

        if (y - 1  >=0 && coveredMap[y - 1][x] == SIGN_UNKNOWN){
            probe(x, y -1);
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

    /**
     * mark a cell at (x,y)
     * @param x
     * @param y
     */
    public void mark(int x, int y) {
        coveredMap[x][y] = SIGN_MARK;
        tornadoesToMark--;
        for (int i = 0; i < unknown.size(); i++) {
            if (unknown.get(i)[0] == x && unknown.get(i)[1] == y) {
                unknown.remove(i);
                break;
            }
        }
    }


//    /**
//     * single point strategy is implemented
//     * @return
//     */
//    public boolean sps() {
//        updateFrontUnknown();
//        boolean successful = false;
//        for (int i = 0; i < frontUnknown.size(); i++) {
//            int x = frontUnknown.get(i)[0];
//            int y = frontUnknown.get(i)[1];
//            ArrayList<int[]> knownNeighbors = findAdjacentSafe(frontUnknown.get(i));
//            for (int[] j: knownNeighbors) {
//                //all clear neighbours
//                if (answerMap[j[0]][j[1]] == findAdjacentMark(j).size()) {
//                    probe(x, y);
//                    successful = true;
//                    System.out.println("SPS: probe[" + x + "," + y + "]");
//                    spsCount++;
//                    showMap();
//                    i--;
//                    updateFrontUnknown();
//                    break;
//                }
//                else {
//                    //all marked neighbours
//                    if (answerMap[j[0]][j[1]] == findAdjacentRisk(j).size()) {
//                        mark(x, y);
//                        successful = true;
//                        System.out.println("SPS: probe[" + x + "," + y + "]");
//                        spsCount++;
//                        showMap();
//                        i--;
//                        updateFrontUnknown();
//                        break;
//                    }
//                }
//            }
//        }
//        return successful;
//    }
//
//    /**
//     * get a array list containing locations of adjacent safe cells
//     * @param pair
//     * @return
//     */
//    protected ArrayList<int[]> findAdjacentSafe(int[] pair) {
//        int x = pair[0];
//        int y = pair[1];
//        ArrayList<int[]> neighbors  = new ArrayList<int[]>();
//        for (int i = x - 1; i <= x + 1; i++) {
//            for (int j = y - 1; j <= y + 1; j++) {
//                if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] != SIGN_UNKNOWN && coveredMap[i][j] != SIGN_MARK) {
//                    neighbors.add(new int[]{i, j});
//                }
//            }
//        }
//        return neighbors;
//    }
//
//    /**
//     * get a array list containing locations of adjacent marked cells
//     * @param pair
//     * @return
//     */
//    protected ArrayList<int[]> findAdjacentMark(int[] pair) {
//        int x = pair[0];
//        int y = pair[1];
//        ArrayList<int[]> neighbors  = new ArrayList<int[]>();
//        for (int i = x - 1; i <= x + 1; i++) {
//            for (int j = y - 1; j <= y + 1; j++) {
//                if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] == SIGN_MARK) {
//                    neighbors.add(new int[]{i, j});
//                }
//            }
//        }
//        return neighbors;
//    }
//
//    /**
//     * get a array list containing locations of adjacent covered cells
//     * @param pair
//     * @return
//     */
//    protected ArrayList<int[]> findAdjacentUnknown(int[] pair) {
//        int x = pair[0];
//        int y = pair[1];
//        ArrayList<int[]> neighbors  = new ArrayList<int[]>();
//        for (int i = x - 1; i <= x + 1; i++) {
//            for (int j = y - 1; j <= y + 1; j++) {
//                if (i >= 0 && i < maxX && j >= 0 && j < maxY && coveredMap[i][j] == SIGN_UNKNOWN) {
//                    neighbors.add(new int[]{i, j});
//                }
//            }
//        }
//        return neighbors;
//    }
//
//    /**
//     * get a array list containing locations of adjacent covered cells and marked cells
//     * @param pair
//     * @return
//     */
//    protected ArrayList<int[]> findAdjacentRisk(int[] pair) {
//        int x = pair[0];
//        int y = pair[1];
//        ArrayList<int[]> neighbors  = new ArrayList<int[]>();
//        for (int i = x - 1; i <= x + 1; i++) {
//            for (int j = y - 1; j <= y + 1; j++) {
//                if (i >= 0 && i < maxX && j >= 0 && j < maxY ) {
//                    if (coveredMap[i][j] == SIGN_UNKNOWN || coveredMap[i][j] == SIGN_MARK) {
//                        neighbors.add(new int[]{i, j});
//                    }
//                }
//            }
//        }
//        return neighbors;
//    }
//
//    /**
//     * update the covered front array list.
//     */
//    public void updateFrontUnknown() {
//        frontUnknown = new ArrayList<int[]>();
//        for (int i = 0; i < unknown.size(); i++) {
//            int[] pair = unknown.get(i).clone();
//            if (findAdjacentSafe(pair).size() != 0) {
//                frontUnknown.add(pair);
//            }
//        }
//    }
//
//    /**
//     * update the safe front array list.
//     */
//    public void updateFrontKnown() {
//        frontKnown = new ArrayList<int[]>();
//        for (int i = 0; i < maxX; i++) {
//            for (int j = 0; j < maxY; j++) {
//                if (coveredMap[i][j] != SIGN_UNKNOWN && coveredMap[i][j] != SIGN_MARK && findAdjacentUnknown(new int[]{i, j}).size() > 0) {
//                    frontKnown.add(new int[]{i,j});
//                }
//            }
//        }
//    }


}
