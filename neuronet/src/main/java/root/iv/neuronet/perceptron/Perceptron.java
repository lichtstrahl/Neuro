package root.iv.neuronet.perceptron;

import java.util.Locale;

import root.iv.neuronet.Number;

/**
 * random, 2, 2 В целом нормальны
 * single, 1, 10
 * random, 2, len/10 Может нормально обучиться
 *
 * random, 2, len/5 С удвоенным количеством А-элементов
 */

public class Perceptron {
    // Активизировавшиеся А-элементы
    private Layer layerS;
    private Layer layerA;
    private Layer layerR;
    private Configuration config;
    private int countEval;

    public Perceptron(Configuration config) {
        this.config = config;
        this.layerS = new Layer(config.getCountS(), 0, 0, config.getFillTypeSA());
        this.layerA = new Layer(config.getCountA(), config.getBiasA(), config.getCountS(), config.getFillTypeSA());
        this.layerR = new Layer(config.getCountR(), config.getBiasR(), config.getCountA(), config.getFillTypeAR());

        countEval = 0;
    }

    // Является ли полученное число тем, на которое обучается сеть
    private boolean check(Number number) {
        layerS.release();           //  Все нейроны деактивируются
        layerS.activate(number);    //  Активируются сенсора по конкретному изображению
        layerA.release();           //  Все нейроны деактивируются
        layerR.release();           //  Все нейроны деактивируются


        // Пробуем активировать A-слой
        for (int a = 0; a < layerA.curentSize(); a++) {
            Neuron neuron = layerA.get(a);
            if (neuron != null) neuron.activate(layerS.getNeurons());
        }


        // Пробуем активировать R-слой
        Integer lastActivated = null;
        for (int r = 0; r < layerR.curentSize(); r++) {
            Neuron neuron = layerR.get(r);
            neuron.activate(layerA.getNeurons());
            if (neuron.isActive()) lastActivated = r;
        }

        // Сеть смогла определить число, если активировался лишь один нейрон и его номер совпадает с искомым номером
        return layerR.countActivated() == 1 && lastActivated != null && lastActivated == number.getValue();
    }

    public int check(Number number, StringBuilder log) {
        check(number);
        log.append("Check\n");
        Integer n = layerR.getActiveNeuron();

        return (n != null) ? n : -1;
    }

    /**
     *
     * @param errorType Тип произошедшей ошибки
     * @param original Номер нейрона, который ДОЛЖЕН был активизироваться.
     */
    private void changeWeights(ErrorType errorType, int original) {
        switch (errorType) {
            case ALL_NOT_ACTIVED:   // + все активные
                for (Neuron neuronR : layerR.getNeurons()) {
                    for (int a = 0; a < layerA.curentSize(); a++) {
                        if (layerA.isActive(a)) neuronR.inc(a);
                    }
                }
                break;

            case INCORRECT_ACTIVED: // + активные, связанные с искомым
                for (int a = 0; a < layerA.curentSize(); a++) {
                    if (layerA.isActive(a))
                        layerR.get(original).inc(a);
                }
                break;

            case EXTRA_ACTIVED: // - все активные
                for (int r = 0; r < layerR.curentSize(); r++) {
                    if (!layerR.isActive(r) || r == original) continue;
                    for (int a = 0; a < layerA.curentSize(); a++) {
                        if (layerA.isActive(a)) layerR.get(r).dec(a);
                    }
                }
                break;
        }

        countEval++;
    }

    //        Тренировка сети
    public void traning(Number[] pattern, int count, StringBuilder log) {
        this.layerR = new Layer(count, config.getBiasR(), config.getCountA(), config.getFillTypeAR());
        layerA.reset();
        layerR.reset();

        // Установить связь точка к точке
        for (int i = 0; i < this.layerA.curentSize(); i++)
            layerA.get(i).inc(i);

        int withoutError = 0;

        for (int i = 0; withoutError < (pattern.length*5);i++) {
            // Генерируем случайное число от 0 до 9
            int index = i % pattern.length;
            Number number = pattern[index];

            // Пробуем научиться узнавать число number. Узнала или нет?
            boolean answer = check(number);

            if (answer) {
                withoutError++;
            } else {
                withoutError = 0;
                ErrorType errorType = ErrorType.NONE;
                if (layerR.countActivated() == 0)   errorType = ErrorType.ALL_NOT_ACTIVED;
                if (layerR.countActivated() == 1)   errorType = ErrorType.INCORRECT_ACTIVED;
                if (layerR.countActivated() > 1)    errorType = ErrorType.EXTRA_ACTIVED;
                changeWeights(errorType, index);
            }
            log.append(String.format(Locale.ENGLISH, "%b\n", answer));
        }

        layerA.deleteDeadNeurons();
        log.append(String.format(Locale.ENGLISH, "Dead neuron: %d\n", config.getCountA() - layerA.countLive()));
        log.append("Live: ");
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < layerA.curentSize(); i++) {
            string.append(String.format(Locale.ENGLISH, "%2d", (layerA.isLive(i)) ? 1 : 0));
        }
        log.append(string.toString().concat("\n"));
    }

    public int countLiveA() {
        return layerA.countLive();
    }


    private enum ErrorType {
        ALL_NOT_ACTIVED,
        EXTRA_ACTIVED,
        INCORRECT_ACTIVED,
        NONE
    }
}
