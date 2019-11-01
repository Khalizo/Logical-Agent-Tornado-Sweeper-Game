import java.util.ArrayList;

/**
 * The agent class for implementing the ESX strategy
 * @author 180026646
 *
 */
public class ESX extends Agent {
    protected int esxCount = 0;
    /**
     * constructor.
     * @param map
     */
    public ESX(char[][] map) {
        super(map);

    }

    /**
     * Implementation of the equation strategy for hexagonal worlds (ESX)
     * @return true if one or more cells are successfully probed or marked
     */
    public boolean esx() {
        updateFrontKnown();
        boolean successful = false;
        for (int i = 0; i < frontKnown.size() - 1; i++) {
            for (int j = i + 1; j < frontKnown.size(); j++) {
                //for each pairs of uncovered cells0
                //if one's covered neighbours are contained by the other's
                //carry out the easy equation analysis
                int[] front1 = frontKnown.get(i);
                int[] front2 = frontKnown.get(j);
                ArrayList<int[]> n1 = getAdjacentUnknown(front1);
                ArrayList<int[]> n2 = getAdjacentUnknown(front2);
                int ic = isContained(n1, n2);
                if (ic != 0) {
                    int diff = Math.abs((Character.getNumericValue(answerMap[front1[1]][front1[0]])
                            - getAdjacentMarked(front1).size()) - (Character.getNumericValue(answerMap[front2[1]][front2[0]]) - getAdjacentMarked(front2).size()));
                    if (diff == 0) {
                        if (ic == 1) probeEsx(n2, n1);
                        else probeEsx(n1, n2);
                        successful = true;
                        Board agentBoard = new Board(this.getCoveredMap());
                        agentBoard.printBoard();
                    }
                    if (diff == Math.abs(n1.size() - n2.size())) {
                        if (ic == 1) flagEsx(n2, n1);
                        else flagEsx(n1, n2);
                        successful = true;
                        Board agentBoard = new Board(this.getCoveredMap());
                        agentBoard.printBoard();
                    }
                }
            }
        }
        return successful;
    }

    /**
     * judge if the one of the array contains the other
     * @param n1
     * @param n2
     * @return 0: not contained; 1:n2 contains n1; 2 n1 contains n2.
     */
    public int isContained(ArrayList<int[]> n1, ArrayList<int[]> n2) {
        int count = 0;
        if (n1.size() == n2.size()) return 0;
        for (int[] i : n1) {
            for (int[] j : n2) {
                if(i[0] == j[0] && i[1] == j[1]) {
                    count++;
                }
            }
        }
        if (count == n1.size()) return 1;
        else {
            if (count == n2.size()) return 2;
            else return 0;
        }
    }

    /**
     * probe all cells that only belong to the bigger collection.
     * @param n1	bigger
     * @param n2	smaller
     */
    public void probeEsx(ArrayList<int[]> n1, ArrayList<int[]> n2) {
        for (int[] i : n1) {
            boolean contained = false;
            for (int[] j : n2) {
                if(i[0] == j[0] && i[1] == j[1]) {
                    contained = true;
                }
            }
            if (!contained) {
                System.out.println("ESX: probe[" + i[0] + "," + i[1] + "]");
                esxCount++;
                probe(i[0], i[1]);
            }
        }
    }

    /**
     * flag cells that could potentially have a danger
     * @param n1	bigger
     * @param n2	smaller
     */
    public void flagEsx(ArrayList<int[]> n1, ArrayList<int[]> n2) {
        for (int[] i : n1) {
            boolean contained = false;
            for (int[] j : n2) {
                if(i[0] == j[0] && i[1] == j[1]) {
                    contained = true;
                }
            }
            if (!contained) {
                System.out.println("ESX: mark[" + i[0] + "," + i[1] + "]");
                esxCount++;
                mark(i[0], i[1]);
            }
        }
    }


    /**
     * Implementation of the equation strategy
     * @return true if one or more cells are successfully probed or marked
     */
    public boolean esxNoPrint() {
        updateFrontKnown();
        boolean successful = false;
        for (int i = 0; i < frontKnown.size() - 1; i++) {
            for (int j = i + 1; j < frontKnown.size(); j++) {
                //for each pairs of uncovered cells0
                //if one's covered neighbours are contained by the other's
                //carry out the easy equation analysis
                int[] front1 = frontKnown.get(i);
                int[] front2 = frontKnown.get(j);
                ArrayList<int[]> n1 = getAdjacentUnknown(front1);
                ArrayList<int[]> n2 = getAdjacentUnknown(front2);
                int ic = isContained(n1, n2);
                if (ic != 0) {
                    int diff = Math.abs((Character.getNumericValue(answerMap[front1[1]][front1[0]])
                            - getAdjacentMarked(front1).size()) - (Character.getNumericValue(answerMap[front2[1]][front2[0]]) - getAdjacentMarked(front2).size()));
                    if (diff == 0) {
                        if (ic == 1) probeEsxNoprint(n2, n1);
                        else probeEsxNoprint(n1, n2);
                        successful = true;
                    }
                    if (diff == Math.abs(n1.size() - n2.size())) {
                        if (ic == 1) flagEsxNoPrint(n2, n1);
                        else flagEsxNoPrint(n1, n2);
                        successful = true;
                    }
                }
            }
        }
        return successful;
    }

    /**
     * probe all cells that only belong to the bigger collection without printing
     * @param n1	bigger
     * @param n2	smaller
     */
    public void probeEsxNoprint(ArrayList<int[]> n1, ArrayList<int[]> n2) {
        for (int[] i : n1) {
            boolean contained = false;
            for (int[] j : n2) {
                if(i[0] == j[0] && i[1] == j[1]) {
                    contained = true;
                }
            }
            if (!contained) {
                esxCount++;
                probe(i[0], i[1]);
            }
        }
    }


    /**
     * Mark cells that could potentially have a danger without printing
     * @param n1	bigger
     * @param n2	smaller
     */
    public void flagEsxNoPrint(ArrayList<int[]> n1, ArrayList<int[]> n2) {
        for (int[] i : n1) {
            boolean contained = false;
            for (int[] j : n2) {
                if(i[0] == j[0] && i[1] == j[1]) {
                    contained = true;
                }
            }
            if (!contained) {
                esxCount++;
                mark(i[0], i[1]);
            }
        }
    }

}
