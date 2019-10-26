import java.util.ArrayList;

/**
 * The agent class for implementing SPX
 * @author 180026646
 */

public class SPX extends Agent {

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
    public boolean spx () {
        updateFrontUnknown();
        boolean successful = false;
        for (int i = 0; i < frontUnknown.size(); i++) {
            int x = frontUnknown.get(i)[0];
            int y = frontUnknown.get(i)[1];
            ArrayList<int[]> knownNeighbours = getAdjacentSafe(frontUnknown.get(i));
            for (int[] j: knownNeighbours) {
                //all clear neighbours
                if (Character.getNumericValue(answerMap[j[1]][j[0]]) == getAdjacentMarked(j).size()) {
                    probe(x, y);
                    successful = true;
                    System.out.println("SPX: probe[" + x + "," + y + "]");
                    Board agentBoard = new Board(this.getCoveredMap());
                    agentBoard.printBoard();
                    spxCount++;
                    showMap();
                    i--;
                    updateFrontUnknown();
                    break;
                }
                else {
                    //all marked neighbours
                    if (Character.getNumericValue( answerMap[j[1]][j[0]]) == getAdjacentRisk(j).size()) {
                        mark(x, y);
                        successful = true;
                        System.out.println("SPX: mark[" + x + "," + y + "]");
                        Board agentBoard = new Board(this.getCoveredMap());
                        agentBoard.printBoard();
                        markCount++;
                        showMap();
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