
/**
 * The game infrastructure class for playing with all the different strategies
 * @author 180026646
 *
 */
public class Game {

    private String map;

    public Game  (String map) {
        this.map = map;


    }

    /**
     * Method for playing the game with the random probing strategy for hexagonal worlds (RPX)
     */
    public void playRPX () {

        //this is the map to play with
        World world = World.valueOf(this.map);
        double cellNumber = world.map.length * world.map.length;

        //instantiate the RPX agent
        RPX agent = new RPX(world.map);
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
        double CR= (1 - agent.unknown.size()/cellNumber) *100;
        int completionRate = (int)CR;
        if (!agent.isSafe) {
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Map: " + this.map);
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        } else {
            System.out.println("Well done you have won!!! :)  ");
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Map: " + this.map);
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        }
    }

    /**
     * Method for playing the game with the single-point strategy (SPX)
     */
    public void playSPX () {

        //this is the map to play with
        World world = World.valueOf(this.map);
        double cellNumber = world.map.length * world.map.length;

        //instantiate the RPX agent
        SPX agent = new SPX(world.map);
        long start = System.currentTimeMillis();
        Board agentBoard= new Board(agent.getCoveredMap());

        //two starting clues of probing in the top left hand corner and the centre
        double mid = (world.map.length/2);
        int m = (int)mid;
        System.out.println("The agent will be playing with a " + world.map.length + "x" +world.map.length + " board:");
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

            // if SPS doesn't work resort to RPX
            if (!agent.spx()) {
                agent.rpx();
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
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Number of flagged tornadoes: " + agent.flagCount);
            System.out.println("Number of times SPX was used: " + agent.spxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        } else {
            //Message for when the agent has won
            System.out.println("Well done you have won!!! :)  ");
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Number of flagged tornadoes: " + agent.flagCount);
            System.out.println("Number of times SPX was used: " + agent.spxCount);
            System.out.println("Completion Rate: " + completionRate + "%");

        }

    }

    /**
     * Method for playing the game with the satisfiability strategy for hexagonal worlds (SATX)
     */
    public void playSATX () {

        //this is the map to play with
        World world = World.valueOf(this.map);
        double cellNumber = world.map.length * world.map.length;

        //instantiate the RPX agent
        SATX agent = new SATX(world.map);
        long start = System.currentTimeMillis();
        Board agentBoard= new Board(agent.getCoveredMap());


        //two starting clues of probing in the top left hand corner and the centre
        double mid = (world.map.length/2);
        int m = (int)mid;
        System.out.println("The agent will be playing with a " + world.map.length + "x" +world.map.length + " board:");
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
            /**
             * if SPX doesn't work resort to SATX
             * if SATX doesn't work resort to RPX
             **/
            if (!agent.spx() && !agent.satx() ) {
                agent.rpx();
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
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Number of marked tornadoes: " + agent.flagCount);
            System.out.println("Number of times SPX was used: " + agent.spxCount);
            System.out.println("Number of times SATX was used: " + agent.satxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        } else {
            //Message for when the agent has won
            System.out.println("Well done you have won!!! :)  ");
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Number of marked tornadoes: " + agent.flagCount);
            System.out.println("Number of times SPX was used: " + agent.spxCount);
            System.out.println("Number of times SATX was used: " + agent.satxCount);
            System.out.println("Completion Rate: " + completionRate + "%");

        }



    }

    /**
     * Method for playing the game with the equation strategy for hexagonal worlds (ESX)
     */
    public void playESX () {

        //this is the map to play with
        World world = World.valueOf(this.map);
        double cellNumber = world.map.length * world.map.length;

        //instantiate the RPX agent
        ESX agent = new ESX(world.map);
        long start = System.currentTimeMillis();
        Board agentBoard= new Board(agent.getCoveredMap());


        //two starting clues of probing in the top left hand corner and the centre
        double mid = (world.map.length/2);
        int m = (int)mid;
        System.out.println("The agent will be playing with a " + world.map.length + "x" +world.map.length + " board:");
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
            /**
             * if SPX doesn't work resort to SATX
             * if SATX doesn't work resort to RPX
             **/
            if (!agent.spx() && !agent.esx() ) {
                agent.rpx();
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
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Number of marked tornadoes: " + agent.flagCount);
            System.out.println("Number of times SPX was used: " + agent.spxCount);
            System.out.println("Number of times ESX was used: " + agent.esxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        } else {
            //Message for when the agent has won
            System.out.println("Well done you have won!!! :)  ");
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.rpxCount);
            System.out.println("Number of marked tornadoes: " + agent.flagCount);
            System.out.println("Number of times SPX was used: " + agent.spxCount);
            System.out.println("Number of times ESX was used: " + agent.esxCount);
            System.out.println("Completion Rate: " + completionRate + "%");

        }

    }

    /**
     * Method for playing the game with the probability random probing strategy for hexagonal worlds (PRPX)
     */
    public void playPRPX(){

        //this is the map to play with
        World world = World.valueOf(this.map);
        double cellNumber = world.map.length * world.map.length;

        //instantiate the RPX agent
        PRPX agent = new PRPX(world.map);
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
            agent.prpx();
            if (!agent.isSafe) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        double CR= (1 - agent.unknown.size()/cellNumber) *100;
        int completionRate = (int)CR;
        if (!agent.isSafe) {
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Map: " + this.map);
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.prpxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        } else {
            System.out.println("Well done you have won!!! :)  ");
            System.out.println();
            System.out.println("***************Performance Report***************");
            System.out.println("Map: " + this.map);
            System.out.println("Time(ms): " + (end - start));
            System.out.println("Number of random guesses: " + agent.prpxCount);
            System.out.println("Completion Rate: " + completionRate + "%");
        }

    }









}
