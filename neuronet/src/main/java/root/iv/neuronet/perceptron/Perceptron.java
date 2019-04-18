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
    private int[] weightsAR;
    private int[][] weightsSA;
    // Активизировавшиеся А-элементы
    private boolean[] activeA;
    private Configuration config;
    private int countEval;

    public Perceptron(Configuration config) {
        this.config = config;
        this.weightsAR = new int[config.getCountA()];   // Веса между A-R
        this.weightsSA = new int[config.getCountS()][config.getCountA()];   // Веса между S-A,
        this.activeA = new boolean[config.getCountA()];
        init(config.getWeightFillType());
    }

    private void init(WeightFillType type) {
        switch (type) {
            case RANDOM:
                randomInitWeightSA();
                break;
            case SINGLE:
                singleInitWeightSA();
                break;

            case SINGLE_FULL:
                singleFullInitWeightSA();
                break;
        }
        initAR();
        this.countEval = 0;
    }

    private void randomInitWeightSA() {
        // Заполнение {-1,0,1} - значениями связей SA
        Random random = new Random();
        for (int i = 0; i < config.getCountS(); i++) {
            for (int j = 0; j < config.getCountA(); j++) {
                int r = random.nextInt(3);
                weightsSA[i][j] = 1-r;  // {-1,0,1}
            }
        }
    }

    private void singleInitWeightSA() {
        if (config.getCountS() != config.getCountA()) throw new IllegalStateException("Чтобы создать единичную матрицу весов количество S и A-элементов должно совпадать");
        weightsSA = MathUtils.getSingleMatrix(config.getCountS());
    }

    private void singleFullInitWeightSA() {
        if (config.getCountS() != config.getCountA()) throw new IllegalStateException("Чтобы создать единичную матрицу весов количество S и A-элементов должно совпадать");
        weightsSA = MathUtils.getSingleFullMatrix(config.getCountS());
    }

    // Изначально всем RA верим по 1
    private void initAR() {
        for (int i = 0; i < weightsAR.length; i++)
            weightsAR[i] = 1;
    }

    // Является ли полученное число тем, на которое обучается сеть
    public int checkSum(Number number) {
        releaseAllA();
        // Пробуеа активировать A-слой
        for (int a = 0; a < config.getCountA(); a++) {
            int sumA = 0;   // Накапливаем сумму для A-элемента
            for (int s = 0; s < config.getCountS(); s++) {
                if (weightsSA[s][a] != 0) { // Связанные с данным А-элементом сенсоры, (можно убрать)
                    sumA += number.getPixel(s) * weightsSA[s][a];
                }
            }
            activeA[a] = sumA >= config.getBiasA();
        }

        int sumR = 0;
        // Перебираем А-элементы
        for (int a = 0; a < config.getCountA(); a++) {
            if (activeA[a]) {   // Если этот элемент активировался, то он участвует в формировании итоговой суммы
                sumR += weightsAR[a];
            }
        }

        return sumR;
    }

    private int countActivate() {
        int count = 0;
        for (int i = 0; i < activeA.length; i++)
        {
            if (activeA[i]) count++;
        }
        return count;
    }

    public boolean check(Number number, Logger logger) {
        int answer = checkSum(number);
        logger.log(String.format(Locale.ENGLISH, "A: %d", countActivate()));
        logger.log(String.format(Locale.ENGLISH, "Sum: %d", answer));
        logger.log(" ");
        return answer >= config.getBiasR();
    }


    public boolean check(Number number) {
        return checkSum(number) >= config.getBiasR();
    }

    // Деактивация всех A-элементов
    private void releaseAllA() {
        for (int i = 0; i < activeA.length; i++)
            activeA[i] = false;
    }

    // Уменьшение значений весов, если сеть ошиблась и выдала 1
    private void decrease() {
        for (int a = 0; a < activeA.length; a++) {
            if (activeA[a]) { // Если a активизировался
                weightsAR[a] -= 1;
            }
        }
    }

    // Увеличение значений весов, если сеть ошиблась и выдала 0
    private void increase() {
        for (int a = 0; a < activeA.length; a++) {
            if (activeA[a]) {
                weightsAR[a] += 1;
            }
        }
    }

    private void changeWeights(int eval) {
        if (eval < 0) decrease();
        if (eval > 0) increase();
        countEval++;
    }

    //        Тренировка сети
    public void traning(Number[] pattern, Number target) {
        init(config.getWeightFillType());
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
    }

    public int[][] getWeightsSA() {
        return weightsSA;
    }

    public int[] getWeightsAR() {
        return weightsAR;
    }
}
