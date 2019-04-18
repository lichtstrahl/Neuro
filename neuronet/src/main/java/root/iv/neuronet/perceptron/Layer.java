package root.iv.neuronet.perceptron;

public class Layer {
    private Neuron[] layer;

    public Layer(int size, int bias, int sizeNext, int sizePrev, WeightFillType fillType) {
        layer = new Neuron[size];
        for (int i = 0; i < size; i++)
            layer[i] = new Neuron(bias, sizeNext, sizePrev, fillType);
    }

    public void release() {
        for (Neuron n : layer)
            n.release();
    }

    public void reset() {
        for (Neuron n : layer)
            n.reset();
    }

    /**
     * Увелиение весов нейронов, выдавших правильный результат
     */
    public void inc() {
        for (Neuron n : layer)
            n.inc();
    }

    /**
     * Уменьшение весов активировавшихся по ошибке нейронов
     */
    public void dec() {
        for (Neuron n : layer)
            n.dec();
    }

    public boolean isActive(int index) {
        return layer[index].isActive();
    }

    public boolean isLive(int index) {
        return layer[index].isLive();
    }

    public int size() {
        return layer.length;
    }

    public Neuron get(int index) {
        return layer[index];
    }


}
