package root.iv.neuronet.perceptron;

import java.util.List;

import root.iv.neuronet.perceptron.cmd.Command;

public class Neuron {
    private int[] weightsPrev;  // Веса с предыдущим слоем
    private boolean state;
    private int bias;
    private boolean live;

    public Neuron(int bias, int sizePrev, Command<int[]> fillCommand) {
        this.bias = bias;
        this.weightsPrev = new int[sizePrev];
        fillCommand.setArgumet(weightsPrev);
        fillCommand.execute();
        release();
        live = false;
    }

    /**
     * Получаем массив целых чисел из {0,1}
     * Этот массив символизирует активировавшиеся элементы с предыдущего слоя
     * @param prev Проверяемое число
     */
    public void activate(List<Neuron> prev) {

        int sum = 0;   // Накапливаем сумму для элемента
        for (int p = 0; p < weightsPrev.length; p++) {
            if (prev.get(p) == null) continue;                      // Если вес == 0, значит
            sum += ((prev.get(p).state) ? 1 : 0) * weightsPrev[p];  // Если активировался, то 1. Если нет, то 0
        }

        state = (sum >= bias);
        if (state) live = true;
    }

    public void activate() {
        state = true;
    }

    public boolean isActive() {
        return state;
    }

    public void release() {
        state = false;
    }

    public boolean isLive() {
        return live;
    }

    /**
     * Увеличивается вес конкретной связи
     * @param index Элемент, с которым следует увеличить связь
     */
    public void inc(int index) {
        weightsPrev[index]++;
    }

    public void dec(int index) {
        weightsPrev[index]--;
    }
}
