package root.iv.neuronet.perceptron.remelhart;

public class Layer {
    private Neuron[] layer;

    public Layer(int size, int sizePrev) {
        layer = new Neuron[size];

        for (int i = 0; i < size; i++) {
            layer[i] = new Neuron(sizePrev);
        }
    }

    public void setInput(int[] input) {
        for (Neuron n : layer)
            n.setInput(input);
    }

    public int size() {
        return layer.length;
    }

    public double calculateOutput(int index) {
        return layer[index].calculateOutput();
    }

    public void updateWeights(int index, double delta) {
        layer[index].updateWeights(delta);
    }
}
