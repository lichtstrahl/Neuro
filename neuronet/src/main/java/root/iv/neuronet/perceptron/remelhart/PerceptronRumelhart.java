package root.iv.neuronet.perceptron.remelhart;

import java.util.LinkedList;
import java.util.List;

public class PerceptronRumelhart {
    private List<Neuron> neurons;

    public PerceptronRumelhart() {
        neurons = new LinkedList<>();
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

    public double[] getOutputs() {
        double[] out = new double[neurons.size()];

        for (int i = 0; i < neurons.size(); i++)
            out[i] = neurons.get(i).calculateOutput();

        return out;
    }

    public void adjustWages(double[] goodOutput) {
        for (int i = 0; i < neurons.size(); i++) {
            double delta = goodOutput[i] - neurons.get(i).calculateOutput();
            neurons.get(i).adjustWeights(delta);
        }
    }
}
