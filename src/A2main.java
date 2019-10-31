import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main method for playing the game with all strategies
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
                    //play the game with the SPX strategy
                    Game spxGame = new Game(map);
                    spxGame.playSPX();
                    break;
                case "SATX":
                    //play the game with the SATX strategy
                    Game satxGame = new Game(map);
                    satxGame.playSATX();
                    break;
                case "ESX":
                    //play the game with the ESX strategy
                    Evaluation esxGame = new Evaluation();
                    esxGame.getESXResults();
//                    Game esxGame = new Game(map);
//                    esxGame.playESX();
                    break;
                case "PRPX":
                    //play the game with the PRPX strategy

                    Evaluation prpxGame = new Evaluation();
                    prpxGame.getPRPXResults();


//                    Game prpxGame = new Game(map);
//                    prpxGame.playPRPX();
                    break;
                default:
                    System.err.println("Invalid Strategy");
            }
            } catch (IndexOutOfBoundsException e) {
            System.err.println("Usage: java A2main <RPX|SPX|SATX> <MAP ID>");
            }


        }
}
