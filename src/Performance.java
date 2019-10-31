import java.util.Arrays;
import java.util.List;

public class Performance {

    public List<String> maps = Arrays.
            asList("S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S10",
                    "L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10", "TEST0", "TEST1",
                    "TEST2");

    public Performance () {

    }


    public void playSPX() {

        for (String map: maps ) {


        //this is the map to play with
        World world = World.valueOf(map);
        double cellNumber = world.map.length * world.map.length;

        //instantiate the RPX agent
        SPX agent = new SPX(world.map);
        long start = System.currentTimeMillis();
        Board agentBoard= new Board(agent.getCoveredMap());

        //two starting clues of probing in the top left hand corner and the centre
        double mid = (world.map.length/2);
        int m = (int)mid;
        agent.probe(0, 0);
        agent.probe(m, m);


        //loop until all cells are marked or probed
        while(!agent.unknown.isEmpty()) {

            // if SPS doesn't work resort to RPX
            if (!agent.spxNoPrint()) {
                agent.rpxNoPrint();
            }

            if (!agent.isSafe) {
                break;
            }
        }

        long end = System.currentTimeMillis();
        double CR= (1 - agent.unknown.size()/cellNumber) *100;
        int completionRate = (int)CR;

        if (!agent.isSafe) { //Checks if the agent has probed a tornado or not
            //Message for when the agent loses

            String summary = "";
            String delimiter = ",";
            summary += map + delimiter
                    + (end - start) + delimiter
                    + agent.rpxCount + delimiter
                    + agent.markCount + delimiter
                    + agent.spxCount + delimiter
                    + completionRate + delimiter
                    + "\n";
            System.out.println(summary);
        } else {
            //Message for when the agent has won

            String summary = "";
            String delimiter = ",";
            summary += map + delimiter
                    + (end - start) + delimiter
                    + agent.rpxCount + delimiter
                    + agent.markCount + delimiter
                    + agent.spxCount + delimiter
                    + completionRate + delimiter
                    + "\n";
            System.out.println(summary);

        }

    }
    }
}
