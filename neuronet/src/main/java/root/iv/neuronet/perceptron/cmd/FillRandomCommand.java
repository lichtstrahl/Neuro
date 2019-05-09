package root.iv.neuronet.perceptron.cmd;

public class FillRandomCommand implements Command<double[]> {
    private double[] w;

    @Override
    public void execute() {
        for (int i = 0; i < w.length; i++)
            w[i] = Math.random() - 0.5;
    }

    @Override
    public void setArgumet(double[] x) {
        w = x;
    }
}
