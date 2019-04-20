package root.iv.neuronet.perceptron.cmd;



import java.util.Random;

public class FillRandomCommand implements Command<int[]> {
    private int[] weights;

    public FillRandomCommand() {
        weights = null;
    }

    @Override
    public void execute() {
        Random random = new Random();
        for (int i = 0; i < weights.length; i++) {
            int r = random.nextInt(3);
            weights[i] = 1-r; // {-1,0,1}
        }
    }

    @Override
    public void setArgumet(int[] x) {
        this.weights = x;
    }
}
