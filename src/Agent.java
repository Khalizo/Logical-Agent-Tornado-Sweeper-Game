import java.util.ArrayList;
import java.util.Random;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public abstract class Agent {
    protected int rpxCount = 0; //counts the number of random guesses
    protected int spxCount = 0; //number of times spx was used
    protected int markCount =0; //number of marked tornadoes
    protected char[][] coveredMap; //parts of the map that is covered
    protected char [][] answerMap; //Original map
    protected int maxX; //Max X coordinate of the map
    protected int maxY; //Max Y coordinate of the map
    protected int tornadoesToMark = 0;
    public ArrayList<int[]> unknown = new ArrayList<int[]>(); //Unknown hexagons
    final char SIGN_UNKNOWN = '?'; //sign for unknown
    final char SIGN_MARK = 'D'; //sign for unknown
    public boolean isSafe = true;

    //the frontiers of uncovered cells
    public ArrayList<int[]> frontKnown = new ArrayList<int[]>();
    //the frontiers of covered cells
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
     * probe the adjacent cells of 0
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
     * finds the adjacent unknown neighbours
     * @param pair
     * @return
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
     * find the adjacent marked neighbours.
     * @param pair
     * @return
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
     * gets all marked and unknown cells
     * @param pair
     * @return
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
     * get adjacent safe cells
     * @param pair
     * @return
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
     * update the covered front array list.
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
          * update the safe front array list.
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



}


