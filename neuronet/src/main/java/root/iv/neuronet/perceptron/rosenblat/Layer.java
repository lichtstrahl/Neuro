package root.iv.neuronet.perceptron.rosenblat;

import java.util.LinkedList;
import java.util.List;

import root.iv.neuronet.Number;
import root.iv.neuronet.perceptron.cmd.Command;

public class Layer {
    private LinkedList<Neuron> layer;
    private int originalSize;
    private int bias;
    private int sizePrev;
    private Command<int[]> fillType;

    public Layer(int size, int bias, int sizePrev, Command fillType) {
        this.originalSize = size;
        this.bias = bias;
        this.sizePrev = sizePrev;
        this.fillType = fillType;
        layer = new LinkedList<>();
        reset();
    }

    public void release() {
        for (Neuron n : layer)
            if (n != null) n.release();
    }

    public void reset() {
        layer.clear();
        for (int i = 0; i < originalSize; i++)
            layer.add(new Neuron(bias, sizePrev, fillType));
    }

    /**
     * Удаление мертвых нейронов
     */
    public void deleteDeadNeurons() {
        for (int i = layer.size()-1; i >= 0; i--) {
            if (layer.get(i) != null && !layer.get(i).isLive())
                layer.set(i, null);
        }
    }

    /**
     * Поиск кореллирующих нейронов
     */
    public void deleteCopy() {
        for (int i = 0; i < layer.size(); i++) {
            Neuron neuron = layer.get(i);
            if (neuron != null) {
                for (int copy = 0; copy < neuron.countCopy(); copy++) {
                    int indexCopy = neuron.getIndexCopy(copy);
                    // Самого себя не удаляем
                    if (indexCopy != i) {
                        layer.set(indexCopy, null);
                    }
                }
            }
        }
    }

    /**
     * Активация слоя по числу (для сенсоров)
     */
    public void activate(Number number) {
        for (int p = 0; p < number.getSize(); p++) {
            if (number.getPixel(p) == 1) {
                layer.get(p).activate();
            }
        }
    }

    /**
     * @return Количество активировавшихся нейронов
     */
    public int countActivated() {
        int count = 0;
        for (Neuron n : layer)
            count += (n.isActive()) ? 1 : 0;
        return count;
    }

    /**
     * @return Количество живых нейронов
     */
    public int countLive() {
        int count = 0;
        for (Neuron n : layer)
            count += (n != null) ? 1 : 0;
        return count;
    }

    /**
     * Возвращает первый активный нейрон. Подразумевается, что он единственный
     * @return Номер активированного нейрона
     */
    public Integer getActiveNeuron() {
        Integer index = null;
        for (int i = 0; i < layer.size(); i++) {
            if (layer.get(i).isActive()) {
                if (index == null) {
                    index = i;
                } else {
                    return null;
                }
            }
        }
        return index;
    }

    public boolean isActive(int index) {
        return layer.get(index) != null && layer.get(index).isActive();
    }

    public boolean isLive(int index) {
        return layer.get(index) != null && layer.get(index).isLive();
    }

    public int countCopy(int index) {
        Neuron neuron = layer.get(index);
        if (neuron != null) {
            return neuron.countCopy();
        } else {
            return -1;
        }
    }

    public int curentSize() {
        return layer.size();
    }

    public Neuron get(int index) {
        return layer.get(index);
    }

    public List<Neuron> getNeurons() {
        return layer;
    }
}
