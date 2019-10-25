import java.sql.SQLOutput;

/**
 * The agent class for implementing RPX
 * @author 180026646
 */

public class A2main {

    public static void main(String[] args) {

        try {
            String strategy = args[0];
            String map = args[1];


            switch (strategy) {
                case "RPX":

                    //this is the map to play with
                    World world = World.valueOf(map);
                    double cellNumber = world.map.length * world.map.length;

                    //instantiate the RPX agent
                    AgentRPX agent = new AgentRPX(world.map);
                    long start = System.currentTimeMillis();
                    Board agentBoard= new Board(agent.getCoveredMap());



                    //two starting clues of probing in the top left hand corner and the centre
                    double mid = (world.map.length/2);
                    int m = (int)mid;
                    System.out.println("Agent will be playing with a " + world.map.length + "x" +world.map.length + " board:");
                    System.out.println("BEWARE - this map has " + agent.tornadoesToMark + " tornadoes!");
                    agentBoard.printBoard();
                    System.out.println("***************Game Started***************");
                    System.out.println();
                    System.out.println("Probe[0,0], " + "[" + m + "," + m + "]" + "(middle cell) and any zero neighbours");
                    agent.probe(0, 0);
                    agent.probe(m, m);
                    agentBoard.printBoard();


                    //loop until all cells are marked or probed
                    while(!agent.unknown.isEmpty()) {
                        agent.rpx();
                        if (!agent.isSafe) {
                            break;
                        }
                    }
                    long end = System.currentTimeMillis();
                    if (!agent.isSafe) {
                        System.out.println();
                        System.out.println("***************Performance Report***************");
                        System.out.println("Time(ms): " + (end - start));
                        System.out.println("Number of random guesses: " + agent.rpxCount);
                        double CR= (1 - agent.unknown.size()/cellNumber) *100;
                        int completionRate = (int)CR;
                        System.out.println("Completion Rate: " + completionRate + "%");
                    }


                    break;

                case "SPX":
                    System.out.println("Almost ready");
                    break;
                case "SATX":
                    System.out.println("Almost there");
                    break;
                default:
                    System.err.println("Invalid Strategy");


            }
            } catch (IndexOutOfBoundsException e) {
            System.err.println("Usage: java A2main <RPX|SPX|SATX> <ID>");
            }


        }






}
