package root.iv.neuronet.perceptron.remelhart;

import java.util.List;
import java.util.Locale;

import root.iv.neuronet.Number;

public class PerceptronRumelhart {
    private Neuron[] neurons;
    private List<Number> originals;         // Образцы

    public PerceptronRumelhart(int count, int sizeA) {
        neurons = new Neuron[count];
        for (int i = 0; i < count; i++) {
            neurons[i] = new Neuron(sizeA);
        }
    }

    public void setInput(int[] input) {
        for (Neuron n : neurons)
            n.setInput(input);
    }

    public int getOutput(StringBuilder logger) {
        double[] out = new double[neurons.length];

        int iMax = 0;
        logger.append("Out: ");
        for (int i = 0; i < neurons.length; i++) {
            out[i] = neurons[i].calculateOutput();
            if (out[i] > out[iMax])  iMax = i;
            logger.append(String.format(Locale.ENGLISH, "%4.1f", out[i]));
        }
        logger.append("\n");

        return iMax;
    }


    public void adjustWages(double[] goodOutput) {
        for (int i = 0; i < neurons.length; i++) {
            double delta = goodOutput[i] - neurons[i].calculateOutput();
            neurons[i].updateWeights(delta);
        }
    }

    /**
     *
     * @param count - Количество этапов, сколько раз нужно прогнать все примеры
     * Идеальный образец имеет 1.0 на выходе только у одного элемента
     */
    public void train(long count) {
        double[] good = new double[originals.size()];

        for (long i = 0; i < count; i++) {
            for (int n = 0; n < originals.size(); n++) {
                setInput(originals.get(n).getPixs());
                for (int k = 0; k < good.length; k++)
                    good[k] = 0.0;
                good[n] = 1.0;
                adjustWages(good);
            }
        }
    }

    public void setOriginalNumbers(List<Number> numbers) {
        this.originals = numbers;
    }
}
