import java.util.ArrayList;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public class SPX extends AgentRPX {

    //the frontiers of uncovered cells
    public ArrayList<int[]> frontKnown = new ArrayList<int[]>();
    //the frontiers of covered cells
    public ArrayList<int[]> frontUnknown = new ArrayList<int[]>();


    /**
     * Constructor for the map
     * @param map
     */
    public SPX (char [][] map) {
        super(map);
    }

    /**
     * Implementation of the sps strategy
     * @return
     */
    public boolean sps () {

        return false;
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
     //     * update the safe front array list.
     //     */
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