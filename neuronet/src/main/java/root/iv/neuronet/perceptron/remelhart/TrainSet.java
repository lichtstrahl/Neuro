package root.iv.neuronet.perceptron.remelhart;

import java.util.List;

public class TrainSet {
    private List<Integer> input;
    private List<Double> goodOutput;

    /**
     *
     * @param input - массив, который будем распознавать
     * @param good  - массив выходов, который должен быть.Например: [1.0, 0.0, 0.0 ... 0.0]
     */
    public TrainSet(List<Integer> input, List<Double> good) {
        this.input = input;
        this.goodOutput = good;
    }

    public List<Integer> getInput() {
        return input;
    }

    public List<Double> getGoodOutput() {
        return goodOutput;
    }
}
