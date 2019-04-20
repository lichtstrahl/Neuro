package root.iv.neuronet.perceptron.cmd;

public class FillConstantCommand implements Command<int[]> {
    private int[] weights;
    private int c;

    public FillConstantCommand(int c) {
        this.weights = null;
        this.c = c;
    }

    @Override
    public void execute() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = c;
        }
    }

    @Override
    public void setArgumet(int[] x) {
        weights = x;
    }
}
