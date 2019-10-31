import java.util.ArrayList;
import java.util.Random;

/**
 * The general abstract agent class for all strategies
 * @author 180026646
 */

public abstract class Agent {
    protected int rpxCount = 0; //Counts the number of random guesses
    protected int spxCount = 0; //Number of times spx was used
    protected int markCount = 0; //Number of marked tornadoes
    protected char [][] coveredMap; //Parts of the map that is covered
    protected char [][] answerMap; //Original map
    protected int maxX; //Max X coordinate of the map
    protected int maxY; //Max Y coordinate of the map
    protected int tornadoesToMark = 0;
    public ArrayList<int[]> unknown = new ArrayList<int[]>(); //Unknown cells
    final char SIGN_UNKNOWN = '?'; //Sign for unknown
    final char SIGN_MARK = 'D'; //Sign for unknown
    public boolean isSafe = true; //Checks for whether the agent has hit a tornado or not.

    //The frontiers of uncovered cells
    public ArrayList<int[]> frontKnown = new ArrayList<int[]>();
    //The frontiers of covered cells
    public ArrayList<int[]> frontUnknown = new ArrayList<int[]>();


    /**
     * Constructor
     * @param map
     */
    public Agent (char [][] map) {
        this.answerMap = map;
        maxX = map.length;
        maxY = map[0].length;
        coveredMap = new char[maxX][maxY];

        //Dynamically create a map based on the selected map
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
     * Probe a cell at (x,y)
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

        if (answerMap[y][x] == '0') {
            uncoverZeroNeighbours(x, y);
        }


    }

    /**
     * Probe the adjacent cells of 0
     * @param x
     * @param y
     */

    /**
     * Method for uncovering the neighbours of a zero cell
     * @param x
     * @param y
     */
    public void uncoverZeroNeighbours (int x, int y  ) {


        //operations for finding the six neighbours
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
     * Make a random probe for all the covered cells
     */
    public void rpx () {
        ArrayList<int[]> front = this.unknown;
        Random rand = new Random();
        int index = rand.nextInt(front.size());
        int loc[] = front.get(index);
        int x = loc[0];
        int y = loc[1];
        probe(x, y);
        rpxCount++;
        System.out.println("RPX: probe[" + x + "," + y + "]");
        Board agentBoard = new Board(this.getCoveredMap());
        agentBoard.printBoard();
        if (!this.isSafe) {
            System.out.println("A Tornado is in cell [" + x + "," + y + "]! Sorry you Lose :(");
        }


    }

    /**
     * Getter for returning the covered map
     * @return coveredMap
     */
    public char[][] getCoveredMap (){
        return this.coveredMap;
    }

    /**
     * Mark a cell at (x,y)
     * @param x
     * @param y
     */
    public void mark(int x, int y) {
        coveredMap[y][x] = SIGN_MARK;
        tornadoesToMark--;
        for (int i = 0; i < unknown.size(); i++) {
            if (unknown.get(i)[0] == x && unknown.get(i)[1] == y) {
                unknown.remove(i);
                break;
            }
        }
    }

    /**
     * Implementation of the SPX strategy
     * @return
     */
    public boolean spx () {
        updateFrontUnknown();
        boolean successful = false;
        for (int i = 0; i < frontUnknown.size(); i++) {
            int x = frontUnknown.get(i)[0];
            int y = frontUnknown.get(i)[1];
            ArrayList<int[]> knownNeighbours = getAdjacentSafe(frontUnknown.get(i));
            for (int[] j: knownNeighbours) {
                //all free neighbours
                if (Character.getNumericValue(answerMap[j[1]][j[0]]) == getAdjacentMarked(j).size()) {
                    probe(x, y);
                    successful = true;
                    System.out.println("SPX: Probe[" + x + "," + y + "]");
                    Board agentBoard = new Board(this.getCoveredMap());
                    agentBoard.printBoard();
                    spxCount++;
                    i--;
                    updateFrontUnknown();
                    break;
                }
                else {
                    //all marked neighbours
                    if (Character.getNumericValue( answerMap[j[1]][j[0]]) == getAdjacentRisk(j).size()) {
                        mark(x, y);
                        successful = true;
                        System.out.println("SPX: Mark[" + x + "," + y + "]");
                        Board agentBoard = new Board(this.getCoveredMap());
                        agentBoard.printBoard();
                        markCount++;
                        i--;
                        updateFrontUnknown();
                        break;
                    }
                }
            }
        }
        return successful;

    }



    /**
     * Finds the adjacent unknown neighbours
     * @param pair
     * @return neighbors
     */
    protected ArrayList<int[]> getAdjacentUnknown(int[] pair) {
        int x = pair[0];
        int y = pair[1];
        ArrayList<int[]> neighbors  = new ArrayList<int[]>();
        if (x - 1 >= 0  && y -1 >= 0  && coveredMap[y-1][x-1] == SIGN_UNKNOWN){
            neighbors.add(new int[]{x -1, y-1});
        }

        if (x - 1 >= 0 && coveredMap[y][x-1] == SIGN_UNKNOWN){
            neighbors.add(new int[]{x-1, y});
        }

        if (y + 1 < maxY && coveredMap[y+1][x] == SIGN_UNKNOWN){
            neighbors.add(new int[]{x, y +1 });
        }

        if (x + 1 < maxX && y + 1< maxY && coveredMap[y+1][x +1] == SIGN_UNKNOWN){
            neighbors.add(new int[]{x + 1, y +1 });
        }

        if (x + 1 < maxX  && coveredMap[y][x +1] == SIGN_UNKNOWN){
            neighbors.add(new int[]{x + 1, y});
        }

        if (y - 1  >=0 && coveredMap[y - 1][x] == SIGN_UNKNOWN){
            neighbors.add(new int[]{x, y -1});
        }

        return neighbors;
    }


    /**
     * Find the adjacent marked neighbours.
     * @param pair
     * @return neighbors
     */
    protected ArrayList<int[]> getAdjacentMarked(int[] pair) {
        int x = pair[0];
        int y = pair[1];
        ArrayList<int[]> neighbors  = new ArrayList<int[]>();

        //operations for finding the six neighbours
        if (x - 1 >= 0  && y -1 >= 0  && coveredMap[y-1][x-1] == SIGN_MARK){
            neighbors.add(new int[]{x -1, y-1});
        }

        if (x - 1 >= 0 && coveredMap[y][x-1] == SIGN_MARK){
            neighbors.add(new int[]{x-1, y});
        }

        if (y + 1 < maxY && coveredMap[y+1][x] == SIGN_MARK){
            neighbors.add(new int[]{x, y +1 });
        }

        if (x + 1 < maxX && y + 1< maxY && coveredMap[y+1][x +1] == SIGN_MARK){
            neighbors.add(new int[]{x + 1, y +1 });
        }

        if (x + 1 < maxX  && coveredMap[y][x +1] == SIGN_MARK){
            neighbors.add(new int[]{x + 1, y});
        }

        if (y - 1  >=0 && coveredMap[y - 1][x] == SIGN_MARK){
            neighbors.add(new int[]{x, y -1});
        }

        return neighbors;
    }

    /**
     * Gets all marked and unknown cells
     * @param pair
     * @return neighbors
     */
    protected ArrayList<int[]> getAdjacentRisk(int[] pair) {
        int x = pair[0];
        int y = pair[1];
        ArrayList<int[]> neighbors  = new ArrayList<int[]>();

        //operations for finding the six neighbours
        if (x - 1 >= 0  && y -1 >= 0  && (coveredMap[y-1][x-1] == SIGN_MARK || coveredMap[y-1][x-1] == SIGN_UNKNOWN)){
            neighbors.add(new int[]{x -1, y-1});
        }

        if (x - 1 >= 0 && (coveredMap[y][x-1] == SIGN_MARK || coveredMap[y][x-1] == SIGN_UNKNOWN)){
            neighbors.add(new int[]{x-1, y});
        }

        if (y + 1 < maxY && (coveredMap[y+1][x] == SIGN_MARK || coveredMap[y+1][x] == SIGN_UNKNOWN)){
            neighbors.add(new int[]{x, y +1 });
        }

        if (x + 1 < maxX && y + 1< maxY && (coveredMap[y+1][x +1] == SIGN_MARK || coveredMap[y+1][x +1] == SIGN_UNKNOWN)){
            neighbors.add(new int[]{x + 1, y +1 });
        }

        if (x + 1 < maxX  && (coveredMap[y][x +1] == SIGN_MARK || coveredMap[y][x +1] == SIGN_UNKNOWN)){
            neighbors.add(new int[]{x + 1, y});
        }

        if (y - 1  >=0 && (coveredMap[y - 1][x] == SIGN_MARK || coveredMap[y - 1][x] == SIGN_UNKNOWN) ){
            neighbors.add(new int[]{x, y -1});
        }

        return neighbors;
    }


    /**
     * Get adjacent safe cells
     * @param pair
     * @return neighbors
     */
    protected ArrayList<int[]> getAdjacentSafe(int[] pair) {
        int x = pair[0];
        int y = pair[1];
        ArrayList<int[]> neighbors  = new ArrayList<int[]>();
        if (x - 1 >= 0  && y -1 >= 0  && coveredMap[y-1][x-1] != SIGN_UNKNOWN && coveredMap[y-1][x-1] != SIGN_MARK){
            neighbors.add(new int[]{x -1, y-1});
        }

        if (x - 1 >= 0 && coveredMap[y][x-1] != SIGN_UNKNOWN && coveredMap[y][x-1] != SIGN_MARK){
            neighbors.add(new int[]{x-1, y});
        }

        if (y + 1 < maxY && coveredMap[y+1][x] != SIGN_UNKNOWN && coveredMap[y+1][x] != SIGN_MARK){
            neighbors.add(new int[]{x, y +1 });
        }

        if (x + 1 < maxX && y + 1< maxY && coveredMap[y+1][x +1] != SIGN_UNKNOWN && coveredMap[y+1][x +1] != SIGN_MARK){
            neighbors.add(new int[]{x + 1, y +1 });
        }

        if (x + 1 < maxX  && coveredMap[y][x +1] != SIGN_UNKNOWN && coveredMap[y][x +1] != SIGN_MARK){
            neighbors.add(new int[]{x + 1, y});
        }

        if (y - 1  >=0 && coveredMap[y - 1][x] != SIGN_UNKNOWN && coveredMap[y - 1][x] != SIGN_MARK){
            neighbors.add(new int[]{x, y -1});
        }



        return neighbors;
    }




    /**
     *  Update the front array list of covered cells
     * */
    public void updateFrontUnknown() {
        frontUnknown = new ArrayList<int[]>();
        for (int i = 0; i < unknown.size(); i++) {
            int[] pair = unknown.get(i).clone();
            if (getAdjacentSafe(pair).size() != 0) {
                frontUnknown.add(pair);
            }


        }
    }

    /**
     *  Update the safe front array list.
     */
    public void updateFrontKnown() {
        frontKnown = new ArrayList<int[]>();
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                if (coveredMap[i][j] != SIGN_UNKNOWN && coveredMap[i][j] != SIGN_MARK && getAdjacentUnknown(new int[]{i, j}).size() > 0) {
                    frontKnown.add(new int[]{i,j});
                }
            }
        }
    }

    /**
     * Special RPX function that doesn't print out information. Used running the algorithm in a format suitable for excel
     */
    public void rpxNoPrint () {
        ArrayList<int[]> front = this.unknown;
        Random rand = new Random();
        int index = rand.nextInt(front.size());
        int loc[] = front.get(index);
        int x = loc[0];
        int y = loc[1];
        probe(x, y);
        rpxCount++;
        if (!this.isSafe) {
        }


    }

    /**
     * Implementation of the SPX strategy
     * @return
     */
    public boolean spxNoPrint () {
        updateFrontUnknown();
        boolean successful = false;
        for (int i = 0; i < frontUnknown.size(); i++) {
            int x = frontUnknown.get(i)[0];
            int y = frontUnknown.get(i)[1];
            ArrayList<int[]> knownNeighbours = getAdjacentSafe(frontUnknown.get(i));
            for (int[] j: knownNeighbours) {
                //all free neighbours
                if (Character.getNumericValue(answerMap[j[1]][j[0]]) == getAdjacentMarked(j).size()) {
                    probe(x, y);
                    successful = true;
//                    System.out.println("SPX: Probe[" + x + "," + y + "]");
                    Board agentBoard = new Board(this.getCoveredMap());
//                    agentBoard.printBoard();
                    spxCount++;
                    i--;
                    updateFrontUnknown();
                    break;
                }
                else {
                    //all marked neighbours
                    if (Character.getNumericValue( answerMap[j[1]][j[0]]) == getAdjacentRisk(j).size()) {
                        mark(x, y);
                        successful = true;
//                        System.out.println("SPX: Mark[" + x + "," + y + "]");
                        Board agentBoard = new Board(this.getCoveredMap());
//                        agentBoard.printBoard();
                        markCount++;
                        i--;
                        updateFrontUnknown();
                        break;
                    }
                }
            }
        }
        return successful;

    }




}


