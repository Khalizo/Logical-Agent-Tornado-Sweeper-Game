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

                    //play the game with the RPX strategy
                    Game rpxGame = new Game(map);
                    rpxGame.playRPX();

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
