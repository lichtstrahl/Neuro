package root.iv.neuronet.perceptron.remelhart;

import java.util.Locale;

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
            if (layer[i] != null) {
                out[i] = layer[i].getValue();
            } else {
                out[i] = 0;
            }
        return out;
    }

    public int countNotNull() {
        int c = 0;
        for (Neuron n : layer)
            c += n != null ? 1 : 0;
        return c;
    }

    /**
     * Used only for R-layer
     * @param index
     * @return
     */
    public double getValue(int index) {
        return layer[index].getValue();
    }

    /**
     * Слой принимает сигналы с предыдущего
     * Каждый нейрон высчитывает свое значение
     * @param input Значения нейронов на предыдущем слое
     */
    public void receiveSignal(double[] input) {
        for (Neuron n : layer)
            if (n != null) n.activate(input);
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
            if (layer[n] != null) layer[n].backPropagation(input, original[n]);
    }

    /**
     * @param input Значения нейронов на предыдущем слое
     * @param errors Взвешенные ошибки со следующего слоая
     */
    public void backPropagationHidden(double[] input, double[] errors) {
        for (int n = 0; n < layer.length; n++) {
            if (layer[n] != null) layer[n].backPropagationHidden(input, errors[n]);
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
                if (layer[k] == null) continue;
                double error = layer[k].getError();
                double w = layer[k].getWeights(indexPrev);
                sum += error * w;
            }
            errors[indexPrev] = sum;
        }

        return errors;
    }

    public void deleteConstantNeuron() {
        for (int n = 0; n < layer.length; n++)
            if (layer[n] != null && layer[n].isConstant()) layer[n] = null;
    }

    /**
     * Сброс начальных значений у нейронов
     */
    public void reset() {
        for (Neuron n : layer)
            if (n != null) n.reset();
    }

    public int countConstant() {
        int count = 0;

        for (Neuron n : layer)
            count += (n != null && n.isConstant()) ? 1 : 0;

        return count;
    }

    public void searchCopy() {
        for (int i = 0; i < layer.length; i++) {
            Neuron a = layer[i];
            if (a != null) {

                for (int copy = 0; copy < a.getCountCopy(); copy++) {
                    int indexPotentialCopy = a.getIndexCopy(copy);
                    Neuron potentialCopy = layer[indexPotentialCopy];
                    // Если значение copy не совпадает со значением a, то это не его дублирующий элемент
                    if (copy != i && a != null && potentialCopy != null && !a.equals(potentialCopy)) {
                        a.markToRemoveCopy(copy);
                    }
                }

                a.removeAllCopy();
            }
        }
    }

    public void logCopy(StringBuilder log) {
        int sum = 0;
        for (Neuron n : layer) {
            if (n == null) continue;
            int c = n.getCountCopy();
            log.append(String.format(Locale.ENGLISH, "%5d", c));
            sum += c;
        }
        log.append("\nSum: " + sum + "\n");
    }
}
