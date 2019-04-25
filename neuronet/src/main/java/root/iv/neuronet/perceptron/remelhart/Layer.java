package root.iv.neuronet.perceptron.remelhart;

public class Layer {
    private Neuron[] layer;
    private int[] input;

    public Layer(int size, int sizePrev) {
        layer = new Neuron[size];

        for (int i = 0; i < size; i++) {
            layer[i] = new Neuron(sizePrev);
        }
    }

    public void setInput(int[] input) {
        this.input = input;
    }

    public int size() {
        return layer.length;
    }

    public double calculateOutput(int index) {
        return layer[index].calculateOutput(input);
    }

    /**
     * Обновление весов, собственно обучение
     * @param goodOutput Образец, к которому должна стремиться сеть
     * @return Суммарное изменение весов, которое было проведено на данном этапе
     */
    public double updateWeights(double[] goodOutput) {
        double globalDelta = 0.0;

        for (int r = 0; r < layer.length; r++) {
            double delta = goodOutput[r] - layer[r].calculateOutput(input);
            layer[r].updateWeights(input, delta);
            globalDelta += Math.abs(delta);
        }

        return globalDelta;
    }
}
