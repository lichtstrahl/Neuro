package root.iv.neuronet.perceptron.remelhart;

import root.iv.neuronet.perceptron.cmd.Command;

public class Layer {
    private Neuron[] layer;

    public Layer(int size, int sizePrev, Command<double[]> cmdFill) {
        layer = new Neuron[size];

        for (int i = 0; i < size; i++) {
            layer[i] = new Neuron(sizePrev, cmdFill);
        }
    }

    public int size() {
        return layer.length;
    }

    public double[] getValues() {
        double[] out = new double[size()];

        for (int i = 0; i < layer.length; i++)
            out[i] = layer[i].getValue();

        return out;
    }

    public double getValue(int index) {
        return layer[index].getValue();
    }

    /**
     * Слой принимает сигналы с предыдущего
     * Каждый нейрон высчитывает свое значение
     * @param values Значения нейронов на предыдущем слое
     */
    public void receiveSignal(double[] values) {
        for (Neuron n : layer)
            n.activate(values);
    }

    /**
     * Обратное распространение для выходного слоя
     * Слой получает "образцовый" сигнал.
     * То есть набор "правильных" значений для каждого нейрона
     * Каждый нейрон
     * @param original
     */
    public void backPropagation(double[] input, double[] original) {
        for (int n = 0; n < layer.length; n++)
            layer[n].backPropagation(input, original[n]);
    }

    /**
     * @param input Значения нейронов на предыдущем слое
     * @param errors Взвешенные ошибки со следующего слоая
     */
    public void backPropagationHidden(double[] input, double[] errors) {
        for (int n = 0; n < layer.length; n++) {
            layer[n].backPropagationHidden(input, errors[n]);
        }
    }

    /**
     * Подсчет вектора ошибок для отправки на предыдущий слой
     * @param sizePrev Размер предыдущего слоя
     * @return
     */
    public double[] calculateWeightsError(int sizePrev) {
        double[] errors = new double[sizePrev];
        for (int indexPrev = 0; indexPrev < sizePrev; indexPrev++) {
            double sum = 0.0;
            for (int k = 0; k < layer.length; k++) {
                double error = layer[k].getError();
                double w = layer[k].getWeights(indexPrev);
                sum += error * w;
            }
            errors[indexPrev] = sum;
        }

        return errors;
    }

    /**
     * Сброс начальных значений у нейронов
     */
    public void reset() {
        for (Neuron n : layer)
            n.reset();
    }
}
