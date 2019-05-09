package root.iv.neuronet.perceptron.remelhart;


import root.iv.neuronet.MathUtils;
import root.iv.neuronet.perceptron.cmd.Command;

public class Neuron {
    /** Скорость обучения */
    private static final double LEARNING_RATIO = 0.02;
    /** Порог */
    private static final int BIAS = 1;
    private double biasWeights;
    /** Веса связей с предыдущим слоем */
    private double[] weights;
    /** Активировался нейрон или нет */
    private boolean changeState;
    /** Значение нейрона **/
    private double value;
    /** Ошибка. Отклонение от образцового значения */
    private double error;


    public Neuron(int countPrev, Command<double[]> cmdFill) {
        this.weights = new double[countPrev];
        this.biasWeights = Math.random();

        cmdFill.setArgumet(weights);
        cmdFill.execute();
    }

    public double calculateOutput(double[] input) {
        double sum = 0;

        for (int i = 0; i < input.length; i++) {
            sum += input[i]*weights[i];
        }
        sum += BIAS * biasWeights;

        value = MathUtils.sigmoid(sum);
        return value;
    }

    /**
     * Считаем значение нейрона, в зависимости от предыдущего слоя
     * Применяем активационную функцию, получая значение данного нейрона
     */

    public void activate(double[] input) {
        double sum = calulateWeightSum(input);
        value = MathUtils.sigmoid(sum);
    }

    // Подсчет взвешенной суммы

    /**
     * значально сумма содержит порог
     * Взвешенная сумма
     * @param input
     */
    private double calulateWeightSum(double[] input) {
        if (input.length != weights.length) throw new IllegalStateException("Не совпадают длины input и weights");
        double sum = BIAS*biasWeights;
        for (int i = 0; i < input.length; i++) {
            sum += input[i]*weights[i];
        }
        return sum;
    }

    /**
     * Снова находим сумму, соответствующую данным входам
     * Умножаем ошибку на производную функции активации
     * Перебирам значения каждого нейрона с пред. слоя
     * Считаем deltaW для каждой связи
     * Считаем delta0 для порога
     * @param original Образцовое значение, которое он должен был принять
     * @param input Входные значения (которые были до этого)
     */
    public void backPropagation(double[] input, double original) {
        double sum = calculateOutput(input);
        error = (original - value)*MathUtils.sigmoid_(sum);

        for (int i = 0; i < input.length; i++) {
            double deltaW = LEARNING_RATIO * error * input[i];
            weights[i] += deltaW;
        }

        double delta0 = LEARNING_RATIO*error;
        biasWeights += delta0;
    }

    /**
     * Обратное распространение для скрытых слоев
     * Вычисляем ошибку
     * @param input
     * @param er Значение ошибки, распространенной со следующего слоя
     */
    public void backPropagationHidden(double[] input, double er) {
        double sum = calulateWeightSum(input);
        error = er*MathUtils.sigmoid_(sum);

        for (int i = 0; i < input.length; i++) {
            double deltaW = LEARNING_RATIO * error * input[i];
            weights[i] += deltaW;
        }

        double delta0 = LEARNING_RATIO*error;
        biasWeights += delta0;
    }

    public double getValue() {
        return value;
    }

    public double getError() {
        return error;
    }

    public double getWeights(int indexPrev) {
        return weights[indexPrev];
    }
}
