package root.iv.neuronet.perceptron.remelhart;


import java.util.LinkedList;
import java.util.List;

import root.iv.neuronet.MathUtils;
import root.iv.neuronet.perceptron.cmd.Command;

public class Neuron {
    /** Скорость обучения */
    private static final double LEARNING_RATIO = 0.02;
    private static final double EPS = 1e-1;
    /** Порог */
    private static final int BIAS = 1;
    private double biasWeights;
    /** Веса связей с предыдущим слоем */
    private double[] weights;
    /** Значение нейрона **/
    private double value;
    /** Начальное значение */
    private Double beginValue = null;
    /** Является ли нейрон константным */
    private boolean constant = true;
    /** Ошибка. Отклонение от образцового значения */
    private double error;
    /** Список индексов потенциальных копий */
    private List<Integer> copy;

    Neuron(int countPrev, Command<double[]> cmdFill) {
        this.weights = new double[countPrev];
        this.biasWeights = Math.random();

        cmdFill.setArgumet(weights);
        cmdFill.execute();

        copy = new LinkedList<>();
        for (int i = 0; i < countPrev; i++)
            copy.add(i);
    }

    /**
     * Считаем значение нейрона, в зависимости от предыдущего слоя
     * Применяем активационную функцию, получая значение данного нейрона
     */
    void activate(double[] input) {
        double sum = calculateWeightsSum(input);
        value = MathUtils.sigmoid(sum);

        if (beginValue == null)
            beginValue = value;
        else
            constant = constant && Math.abs(beginValue-value) < EPS;
    }

    /**
     * значально сумма содержит порог
     * Взвешенная сумма
     * @param input Вход с предыдущего слоя
     */
    private double calculateWeightsSum(double[] input) {
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
    void backPropagation(double[] input, double original) {
        double sum = calculateWeightsSum(input);
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
    void backPropagationHidden(double[] input, double er) {
        double sum = calculateWeightsSum(input);
        error = er*MathUtils.sigmoid_(sum);

        for (int i = 0; i < input.length; i++) {
            double deltaW = LEARNING_RATIO * error * input[i];
            weights[i] += deltaW;
        }

        double delta0 = LEARNING_RATIO*error;
        biasWeights += delta0;
    }

    double getValue() {
        return value;
    }

    double getError() {
        return error;
    }

    double getWeights(int indexPrev) {
        return weights[indexPrev];
    }

    int getCountCopy() {
        return copy.size();
    }

    int getIndexCopy(int i) {
        return copy.get(i);
    }

    /**
     * Пометить копию к удалению
     * @param index
     */
    void markToRemoveCopy(int index) {
        copy.set(index, null);
    }

    /**
     * Удаление помеченных копий
     */
    void removeAllCopy() {
        List<Integer> newCopy = new LinkedList<>();

        for (Integer integer : copy) {
            if (integer != null) {
                newCopy.add(integer);
            }
        }

        copy = newCopy;
    }

    /**
     * Сброс начального значения нейрона
     */
    void reset() {
        beginValue = null;
        constant = true;
    }

    boolean isConstant() {
        return constant;
    }

    boolean equals(Neuron o) {
        return Math.abs(value - o.value) < EPS;
    }
}
