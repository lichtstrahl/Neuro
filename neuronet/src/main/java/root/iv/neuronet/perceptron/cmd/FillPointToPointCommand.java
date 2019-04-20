package root.iv.neuronet.perceptron.cmd;

public class FillPointToPointCommand implements Command<int[]> {
    private int[] weights;
    private int index;

    public FillPointToPointCommand(int[] w) {
        this.weights = w;
    }

    @Override
    public void execute() {

    }

    @Override
    public void setArgumet(int[] x) {
        weights = x;
    }



}
