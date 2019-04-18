package root.iv.neuronet.perceptron;

import java.util.Locale;
import java.util.Random;

import root.iv.neuronet.Logger;
import root.iv.neuronet.MathUtils;
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
    private Layer layerA;
    private Configuration config;
    private int countEval;

    public Perceptron(Configuration config) {
        this.config = config;
        this.layerA = new Layer(config.getCountA(), config.getBiasA(), config.getCountR(), config.getCountS(), config.getWeightFillType());
        countEval = 0;
    }




    // Является ли полученное число тем, на которое обучается сеть
    public int checkSum(Number number) {
        layerA.release();
        int sumR = 0;
        // Пробуем активировать A-слой
        for (int a = 0; a < layerA.size(); a++) {
            Neuron neuron = layerA.get(a);
            neuron.activate(number);
            sumR += neuron.signalToNext();
        }

        return sumR;
    }

    private int countActivate() {
        int count = 0;
        for (int i = 0; i < layerA.size(); i++) {
            if (layerA.isActive(i)) count++;
        }
        return count;
    }

    public boolean check(Number number, StringBuilder log) {
        int sum = checkSum(number);
        log.append(String.format(Locale.ENGLISH, "All, A, Sum: %d, %d, %d\n", config.getCountA(), countActivate(), sum));

        return sum >= config.getBiasR();
    }


    public boolean check(Number number) {
        return checkSum(number) >= config.getBiasR();
    }

    private void changeWeights(int eval) {
        if (eval < 0) layerA.dec();
        if (eval > 0) layerA.inc();
        countEval++;
    }

    //        Тренировка сети
    public void traning(Number[] pattern, Number target, StringBuilder log) {
        layerA.reset();

        for (int i = 0;;i++) {
            // Генерируем случайное число от 0 до 9
            Number number = pattern[i % pattern.length];

            boolean answer = check(number);
            // На ложное число сеть сказала "ДА"
            if (number.getValue() != target.getValue() && answer) {
                changeWeights(-1);
            }

            // На нужное число сеть выдала "НЕТ"
            if (number.getValue() == target.getValue() && !answer) {
                changeWeights(1);
            }

            // Если без изменения весов произошло указанное количество итераций, то обучение заканчивается
            if (i - countEval == pattern.length*10) break;
        }

        log.append("Live: ");
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < layerA.size(); i++) {
            string.append(String.format(Locale.ENGLISH, "%2d", (layerA.isLive(i)) ? 1 : 0));
        }
        log.append(string.toString().concat("\n"));
    }
}
