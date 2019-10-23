public class A2main {

    public static void main(String[] args) {

        String strategy = args[0];
        String map = args[1];
            World world = World.valueOf(map);
            Board answerBoard = new Board(world.map);
            answerBoard.printBoard();


            AgentRPX agent = new AgentRPX(world.map);
            agent.rpx();
            Board agentBoard = new Board(agent.getCoveredMap());
            agentBoard.printBoard();




    }
}
