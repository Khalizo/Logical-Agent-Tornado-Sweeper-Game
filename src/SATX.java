import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.PlFormula;

import java.io.IOException;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public class SATX extends Agent {

    /**
     * Constructor for the map
     * @param map
     */
    public SATX (char [][] map) {
        super(map);
    }

    public boolean satx () {

        try {
            PlBeliefSet kb = new PlBeliefSet();
            PlParser parser = new PlParser();
            kb.add((PlFormula)parser.parseFormula("(A || B)&& !(A || !B)"));
        } catch (IOException e) {

        }



        return true;
    }

}
