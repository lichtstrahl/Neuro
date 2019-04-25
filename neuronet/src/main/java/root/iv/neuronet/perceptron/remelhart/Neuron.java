package root.iv.neuronet.perceptron.remelhart;


import root.iv.neuronet.MathUtils;

public class Neuron {
    /** Скорость обучения */
    private static final double LEARNING_RATIO = 0.1;
    /** Порог */
    private static final int BIAS = 1;
    private double biasWeights;
    /** Входы */
    private int[] input;
    /** Веса связей с предыдущим слоем */
    private double[] weights;

    public Neuron(int countPrev) {
        this.weights = new double[countPrev];
        this.biasWeights = Math.random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
        }
    }

    /**
     * Задаем входы. Если задаем впервые, то сразу же генерируем веса
     * @param input
     */
    public void setInput(int[] input) {
        this.input = input;
    }

    public double calculateOutput() {
        double sum = 0;

        for (int i = 0; i < input.length; i++) {
            sum += input[i]*weights[i];
        }
        sum += BIAS * biasWeights;

        return MathUtils.sigmoid(sum);
    }

    /**
     * Корректировка весов
     * @param delta
     */
    public void updateWeights(double delta) {
        for (int i = 0; i < input.length; i++) {
            double d = weights[i];
            d += LEARNING_RATIO * delta * input[i];
            weights[i] = d;
        }

        biasWeights += LEARNING_RATIO * delta * BIAS;
    }
}
