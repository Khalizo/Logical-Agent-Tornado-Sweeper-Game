/**
 * this class is used in PRPX method to rank the cells
 * @author 180026646
 */
public class PCell {
    public int[] location;
    public double pSafe;
    public PCell(int[] location, double pSafe) {
        this.location = location;
        this.pSafe = pSafe;
    }
}