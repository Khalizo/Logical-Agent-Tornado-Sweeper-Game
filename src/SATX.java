import net.sf.tweety.commons.Interpretation;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.*;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.io.writers.FormulaDimacsFileWriter;
import org.sat4j.pb.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public class SATX extends Agent {
    protected int satxCount = 0;


    /**
     * Constructor for the map
     * @param map
     */
    public SATX (char [][] map) {
        super(map);
    }



    /**
     * The satisfiablility Strategy for hexagonal worlds strategy is implemented
     * @return true if one or more cells are successfully probed or marked
     */
    public boolean satx() {
        updateFrontKnown();
        updateFrontUnknown();
        boolean successful = false;
        //generate the KBU with all known frontiers
        String KBU = getKBU();
        //for each unknown frontier, run the positive DPLLSat and negative DPLLSat
        for (int i = 0; i < frontUnknown.size(); i++) {
            int x = frontUnknown.get(i)[0];
            int y = frontUnknown.get(i)[1];
            //generate the logic sentence for this unknown frontier
            String p = "T_" + x + "_" + + y;
            //negative SAT
            String prove = KBU + " & ~" + p;
            boolean ans = SATSatisfiable(prove);
            if (!ans) {
                //if the answer is false, mark this cell.
                mark(x, y);
                System.out.println("SATX: Mark[" + x + "," + y + "]");
                Board agentBoard = new Board(this.getCoveredMap());
                agentBoard.printBoard();
                satxCount++;
                successful = true;
            }
            else {
                //positive SAT
                prove = KBU + " & " + p;
                ans = SATSatisfiable(prove);
                if (!ans) {
                    //if the answer is false, probe the cell
                    probe(x, y);
                    System.out.println("SATX: Probe[" + x + "," + y + "]");
                    Board agentBoard = new Board(this.getCoveredMap());
                    agentBoard.printBoard();
                    satxCount++;
                    successful = true;
                }
            }
        }
        return successful;
    }


    /**
     * create the KBU.
     * @return
     */
    private String getKBU() {
        String KBU = "";
        //generate logic proposition for single known frontier and link them with &
        for (int i = 0; i < frontKnown.size(); i++) {
            int[] cell = frontKnown.get(i);
            String sentence = getSingleKBU(cell);
            KBU += sentence;
            if (i < frontKnown.size() - 1) {
                KBU += " & ";
            }
        }

        return KBU;
    }


    /**
     * create the logic proposition for a single known cell
     * @param cell
     * @return
     */
    private String getSingleKBU(int[] cell) {
        String single = "(";
        int x = cell[0];
        int y = cell[1];
        //count the unmarked nettles around this cell.
        int count = answerMap[x][y] - getAdjacentMarked(cell).size();
        ArrayList<int[]> unknownNeighbors = getAdjacentUnknown(cell);
        //get a collection of list containing all possible combination of 0 and 1.
        //1 is tornado-positive
        //0 is tornado-negative
        //the size of this combination is the number of covered cells around it.
        int[] set = new int[unknownNeighbors.size()];
        for (int i = 0; i < set.length; i++) {
            set[i] = 0;
        }
        ArrayList<int[]> possibleSet = new ArrayList<int[]>();
        possibleSet.add(set.clone());
        for (int i = 0; i < Math.pow(2, set.length) - 1; i++) {
            int index = 0;
            set[index]++;
            while(set[index] == 2) {
                set[index] = 0;
                index++;
                set[index]++;
            }
            possibleSet.add(set.clone());
        }
        //leave combinations with correct number of tornadoes only
//        for (int i = 0; i < possibleSet.size(); i++) {
//            int sum = 0;
//            for (int j: possibleSet.get(i)) {
//                if (j != 1) {
//                    sum++;
//                }
//            }
//            if (sum != count) {
//                possibleSet.remove(i);
//                i--;
//            }
//        }
        //generate a proposition for this cell from the binary l ist
        for (int j = 0; j < possibleSet.size(); j++) {
            int[] array = possibleSet.get(j);
            String s = "(";
            for(int i = 0; i < array.length; i++) {
                if (array[i] == 0) {
                    s += "~"; //LogicNG syntax for NOT
                }
                s += "T_"; //T for tornado
                s += unknownNeighbors.get(i)[0];
                s += "_";
                s += unknownNeighbors.get(i)[1];
                if (i != array.length - 1) {
                    s += " & ";
                }
            }
            s += ")";
            if (j != possibleSet.size() - 1) {
                s += " | ";
            }
            single += s;
        }
        single += ")";
        return single;
    }

    /**
     * this method is given in the lecture slide, invoking the SAT4J judgement method from the library.
     * @param query
     * @return
     */
    public static boolean SATSatisfiable(String query) {
        try {

            //Parse the KBU formula LogicNG
            FormulaFactory f = new FormulaFactory();
            PropositionalParser p = new PropositionalParser(f);
            Formula formula = p.parse(query);

            //convert to CNF
            Formula cnf = formula.cnf();

            //Convert to DIMACS
            FormulaDimacsFileWriter.write("dimacs.cnf",cnf,false);

            //Feed the solver
            ISolver solver = SolverFactory.newDefault();
            solver.setTimeout(3600);
            Reader reader = new DimacsReader(solver);
            IProblem problem = reader.parseInstance("dimacs.cnf");

            //Check satisfiability
            if (problem.isSatisfiable()) {
                System.out.println(query + " is satisfiable");
                return true;

            } else {
                System.out.println(query + " is NOT satisfiable");
                return false;
            }

        } catch (org.logicng.io.parsers.ParserException e) {

        } catch (IOException e) {

        } catch (org.sat4j.reader.ParseFormatException e) {

        } catch (org.sat4j.specs.ContradictionException e) {


        } catch (org.sat4j.specs.TimeoutException e){

        }
        return  true;
    }

//    public ArrayList<int[]> getClauses (String kbu) {
//        ArrayList<int[]> clauses = new ArrayList<>();
//
//
//
//
//        return clauses;
//    }

}
