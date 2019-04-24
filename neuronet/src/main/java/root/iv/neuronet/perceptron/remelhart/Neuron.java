package root.iv.neuronet.perceptron.remelhart;

import java.util.LinkedList;
import java.util.List;

public class Neuron {
    private static final int BIAS = 1;  // Порог
    private static final double LEARNING_RATIO = 0.1;   // Скорость обучения

    private List<Integer> input;
    private List<Double> weights;
    private double biasWeights;
    private double output;

    public Neuron() {
        this.input = new LinkedList<>();
        this.weights = new LinkedList<>();
        this.biasWeights = Math.random();
    }

    /**
     * Задаем входы. Если задаем впервые, то сразу же генерируем веса
     * @param input
     */
    public void setInput(List<Integer> input) {
        if (this.input.size() == 0) {
//            this.input = new LinkedList<>(input);
            generateWeights();
        }
        this.input = new LinkedList<>(input);
    }

    public void generateWeights() {
        for (int i = 0; i <input.size(); i++) {
            weights.add(Math.random());
        }
    }

    public void calculateOutput() {
        double sum = 0;

        for (int i = 0; i < input.size(); i++) {
            sum += input.get(i)*weights.get(i);
        }
        sum += BIAS * biasWeights;

        output = MathUtils.sigmoid(sum);
    }

    /**
     * Корректировка весов
     * @param delta
     */
    public void adjustWeights(double delta) {
        for (int i = 0; i < input.size(); i++) {
            double d = weights.get(i);
            d += LEARNING_RATIO * delta * input.get(i);
            weights.set(i, d);
        }

        biasWeights += LEARNING_RATIO * delta * BIAS;
    }

    public double getOutput() {
        calculateOutput();
        return output;
    }
}
