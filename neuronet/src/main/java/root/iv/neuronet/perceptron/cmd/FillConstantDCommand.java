package root.iv.neuronet.perceptron.cmd;

public class FillConstantDCommand implements Command<double[]> {
    private double[] weights;
    private double c;

    public FillConstantDCommand(int c) {
        this.c = c;
    }

    @Override
    public void execute() {
        for (int i = 0; i < weights.length; i++)
            weights[i] = c;
    }

    @Override
    public void setArgumet(double[] x) {
        weights = x;
    }
}
