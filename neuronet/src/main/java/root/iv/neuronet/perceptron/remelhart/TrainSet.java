package root.iv.neuronet.perceptron.remelhart;

public class TrainSet {
    private int[] input;
    private double[] goodOutput;

    /**
     *
     * @param input - массив, который будем распознавать
     * @param good  - массив выходов, который должен быть.Например: [1.0, 0.0, 0.0 ... 0.0]
     */
    public TrainSet(int[] input, double[] good) {
        this.input = input;
        this.goodOutput = good;
    }

    public int[] getInput() {
        return input;
    }

    public double[] getGoodOutput() {
        return goodOutput;
    }
}
