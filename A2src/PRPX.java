import java.util.ArrayList;
import java.util.Random;
/**
 * The agent class for implementing the PRPX strategy
 * @author 180026646
 *
 */
public class PRPX extends Agent {

    protected int prpxCount = 0;



    public PRPX (char [][] map) {
        super(map);
    }


    /**
     * Probability random probing strategy for hexagonal worlds (PRPX)
     * make a random guess from the possibly safe unknown frontiers.
     */
    public void prpx() {
        updateFrontUnknown();
        //initialize the list
        ArrayList<PCell> pList = new ArrayList<PCell>();
        for (int i = 0; i < frontUnknown.size(); i++) {
            int[] cell = frontUnknown.get(i);
            ArrayList<int[]> safeNeighbours= getAdjacentSafe(cell);
            double pSafe = 0;
            for (int j = 0; j < safeNeighbours.size(); j++) {
                int[] safeNeighbour = safeNeighbours.get(j);
                double marked = getAdjacentMarked(safeNeighbour).size();
                double total = Character.getNumericValue(answerMap[safeNeighbour[0]][safeNeighbour[1]]);
                double p = 1 - (total - marked) / getAdjacentUnknown(safeNeighbour).size();
                if (pSafe < p) pSafe = p;
            }
            boolean flag = true;
            PCell pc = new PCell(cell.clone(), pSafe);
            //put the unknown frontiers in the right order
            for (int j = 0; j < pList.size(); j++) {
                if (pSafe > pList.get(j).pSafe) {
                    pList.add(j, pc);
                    flag = false;
                    break;
                }
            }
            if (flag) pList.add(pc);
        }
        //make random guess in the first 1/3 of the list.
        Random rand = new Random();
        int index = rand.nextInt(pList.size() / 3);
        int[] cellToProbe = pList.get(index).location;
        probe(cellToProbe[0], cellToProbe[1]);
        prpxCount++;
        System.out.println("PRPX: Probe[" + cellToProbe[0] + "," + cellToProbe[1] + "]");
        Board agentBoard = new Board(this.getCoveredMap());
        agentBoard.printBoard();
        if (!this.isSafe) {
            System.out.println("A Tornado is in cell [" + cellToProbe[0] + "," + cellToProbe[1] + "]! Sorry you Lose :(");
        }
    }

    /**
     * make a random guess from the possibly safe unknown frontiers
     */
    public void prpxNoPrint() {
        updateFrontUnknown();
        //initialize the list
        ArrayList<PCell> pList = new ArrayList<PCell>();
        for (int i = 0; i < frontUnknown.size(); i++) {
            int[] cell = frontUnknown.get(i);
            ArrayList<int[]> safeNeighbours= getAdjacentSafe(cell);
            double pSafe = 0;
            for (int j = 0; j < safeNeighbours.size(); j++) {
                int[] safeNeighbour = safeNeighbours.get(j);
                double marked = getAdjacentMarked(safeNeighbour).size();
                double total = Character.getNumericValue(answerMap[safeNeighbour[0]][safeNeighbour[1]]);
                double p = 1 - (total - marked) / getAdjacentUnknown(safeNeighbour).size();
                if (pSafe < p) pSafe = p;
            }
            boolean flag = true;
            PCell pc = new PCell(cell.clone(), pSafe);
            //put the unknown frontiers in the right order
            for (int j = 0; j < pList.size(); j++) {
                if (pSafe > pList.get(j).pSafe) {
                    pList.add(j, pc);
                    flag = false;
                    break;
                }
            }
            if (flag) pList.add(pc);
        }
        //make random guess in the first 1/3 of the list.
        Random rand = new Random();
        int index = rand.nextInt(pList.size() / 3);
        int[] cellToProbe = pList.get(index).location;
        probe(cellToProbe[0], cellToProbe[1]);
        prpxCount++;
//        System.out.println("PRPX: Probe[" + cellToProbe[0] + "," + cellToProbe[1] + "]");
//        Board agentBoard = new Board(this.getCoveredMap());
//        agentBoard.printBoard();
//        if (!this.isSafe) {
//            System.out.println("A Tornado is in cell [" + cellToProbe[0] + "," + cellToProbe[1] + "]! Sorry you Lose :(");
//        }
    }


}



