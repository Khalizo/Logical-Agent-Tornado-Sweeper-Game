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
    protected int dlsCount = 0;


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
            String p = "N_" + x + "_" + + y;
            //negative DPLLSat
            String prove = KBU + " & ~" + p;
            boolean ans = displayDPLLSatisfiableStatus(prove);
            if (!ans) {
                //if the answer is false, mark this cell.
                System.out.println("DLS: mark[" + x + "," + y + "]");
                dlsCount++;
                mark(x, y);
                successful = true;
            }
            else {
                //positive DPLLSat
                prove = KBU + " & " + p;
                ans = displayDPLLSatisfiableStatus(prove);
                if (!ans) {
                    //if the answer is false, probe the cell
                    System.out.println("DLS: probe[" + x + "," + y + "]");
                    dlsCount++;
                    probe(x, y);
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
        for (int i = 0; i < possibleSet.size(); i++) {
            int sum = 0;
            for (int j: possibleSet.get(i)) {
                if (j == 1) sum++;
            }
            if (sum != count) {
                possibleSet.remove(i);
                i--;
            }
        }
        //generate a proposition for this cell from the binary l ist
        for (int j = 0; j < possibleSet.size(); j++) {
            int[] array = possibleSet.get(j);
            String s = "(";
            for(int i = 0; i < array.length; i++) {
                if (array[i] == 0) {
                    s += "!"; //Tweety syntax for NOT
                }
                s += "T_"; //T for tornado
                s += unknownNeighbors.get(i)[0];
                s += "_";
                s += unknownNeighbors.get(i)[1];
                if (i != array.length - 1) {
                    s += " && ";
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
     * this method is given in the lecture slide, invoking the DPLLSat judgement method from the library.
     * @param query
     * @return
     */
    public static boolean displayDPLLSatisfiableStatus(String query) {

            return false;

    }

    public ArrayList<int[]> getClauses (String kbu) {
        ArrayList<int[]> clauses = new ArrayList<>();
        try {

            //Parse the KBU formula LogicNG
            FormulaFactory f = new FormulaFactory();
            PropositionalParser p = new PropositionalParser(f);
            Formula formula = p.parse(kbu);

            //convert to CNF
            Formula cnf = formula.cnf();

            //Convert to DIMACS
            FormulaDimacsFileWriter.write("dimacs.cnf",cnf,false);

            //Feed the solver
            ISolver solver = SolverFactory.newDefault();
            solver.setTimeout(3600);
            Reader reader = new DimacsReader(solver);
            IProblem problem = reader.parseInstance("dimacs.cnf");


//            //Retrieve the clauses
//            ListIterator<PlFormula> iter =conj.listIterator();
//            System.out.println(kb);
//            System.out.println(conj);
//
//            //Convert from CNF to DIMACS
//
//            //Retrieve the list of propositions
//            PlSignature signature = kb.getSignature();
//            Iterator<Proposition> sigList = signature.iterator();
//            HashMap<String, Integer> propositions = new HashMap <String, Integer> ();
//
//            //Assign each proposition to an integer using a hashmap
//            int propCount = 1;
//            while(sigList.hasNext()) {
//                Proposition literal = sigList.next();
//                propositions.put(literal.toString(),propCount);
//                propCount++;
//            }
//            System.out.println(propositions);
//            System.out.println(propositions.get("A"));
//
//            //remove CNF symbols and replace with DIMACS ones
//            ArrayList<String> stringClauses = new ArrayList<>();
//            while(iter.hasNext()) {
//                PlFormula clause =iter.next();
//                String  clauseStr = clause.getLiterals().toString().replace("!","-").replace("[","").replace("]","");
//                stringClauses.add(clauseStr);
//
//
////                System.out.println(stringClauses);
////                System.out.println(clause.getSignature());
////                System.out.println(stringClauses);
////                System.out.println(clause.getLiterals());
////                for(PlFormula l:clause.getLiterals()){
////                    System.out.println(l);
////
////                }
//            }
//            System.out.println(stringClauses);



//            ArrayList<String> integers = new ArrayList<>();
//            for (String replace1: stringClauses) {
//
//                for (HashMap.Entry<String, Integer> entry: propositions.entrySet()){
//                    String replace2 = replace1.replace(entry.getKey(), entry.getValue().toString());
//                    integers.add(replace2);
//                }
//
//            }
//
//            System.out.println(integers);










        } catch (org.logicng.io.parsers.ParserException e) {

        } catch (IOException e) {

        } catch (org.sat4j.reader.ParseFormatException e) {

        } catch (org.sat4j.specs.ContradictionException e) {

        }



        return clauses;
    }

}
