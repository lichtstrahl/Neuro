package root.iv.neuronet.perceptron.remelhart;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import root.iv.neuronet.Number;

public class PerceptronRumelhart {
    private List<Neuron> neurons;
    private List<Number> originals;         // Образцы

    public PerceptronRumelhart(int count) {
        neurons = new LinkedList<>();
        addNeurons(count);
    }

    public void addNeurons(int count) {
        for (int i = 0; i < count; i++) {
            neurons.add(new Neuron());
        }
    }

    public void setInput(int[] input) {
        for (Neuron n : neurons)
            n.setInput(input);
    }

    public int getOutput(StringBuilder logger) {
        double[] out = new double[neurons.size()];

        int i_max = 0;
        logger.append("Out: ");
        for (int i = 0; i < neurons.size(); i++) {
            double o = neurons.get(i).calculateOutput();
            if (o > out[i_max])  i_max = i;
            logger.append(String.format(Locale.ENGLISH, "%4.1f", out[i]));
        }
        logger.append("\n");

        return i_max;
    }

    public void adjustWages(double[] goodOutput) {
        for (int i = 0; i < neurons.size(); i++) {
            double delta = goodOutput[i] - neurons.get(i).calculateOutput();
            neurons.get(i).adjustWeights(delta);
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
