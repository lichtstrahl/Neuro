package root.iv.neuronet.perceptron;

import java.util.Random;

import root.iv.neuronet.Number;

public class Neuron {
    private int[] weightsNext;  // Веса со следующим слоем
    private int[] weightsPrev;  // Веса с предыдущим слоем
    private boolean state;
    private int bias;
    private boolean live;
    private WeightFillType fillType;

    public Neuron(int bias, int sizeNext, int sizePrev, WeightFillType fillType) {
        this.bias = bias;
        this.weightsNext = new int[sizeNext];
        this.weightsPrev = new int[sizePrev];
        this.fillType = fillType;
        updateWeights();
        release();
        reset();
    }

    public void updateWeights() {
        switch (this.fillType) {
            case SINGLE_FULL:
            case SINGLE:
                throw new UnsupportedOperationException("Поддерживается только случайное заполнение весов");

            case RANDOM:
                Random random = new Random();
                for (int i = 0; i < weightsPrev.length; i++) {
                    int r = random.nextInt(3);
                    weightsPrev[i] = 1-r; // {-1,0,1}
                }
                break;
        }

        for (int i = 0; i < weightsNext.length; i++)
            weightsNext[i] = 1;
    }

    /**
     * Проверяем, активирует данный A-элемент указанное число
     * @param target Проверяемое число
     */
    public void activate(Number target) {

        int sum = 0;   // Накапливаем сумму для элемента
        for (int p = 0; p < weightsPrev.length; p++) {
            sum += target.getPixel(p) * weightsPrev[p];
        }

        state = (sum >= bias);
        if (state) live = true;
    }

    /**
     * Иммитация передачи сигнала на следующий слой. В данном случае предполагается, что последний слой состоит из 1 элемента
     * @return Выходное количество сигнала. Представляет собой произведение 1 и веса
     */
    public int signalToNext() {
        if (weightsNext.length != 1) throw new IllegalStateException("Размерность выходного слоя должна быть 1");
        return (isActive()) ? weightsNext[0] : 0;
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

    public void reset() {
        live = false;
        updateWeights();
    }

    public void inc() {
        if (state) weightsNext[0]++;
    }

    public void dec() {
        if (state) weightsNext[0]--;
    }
}
