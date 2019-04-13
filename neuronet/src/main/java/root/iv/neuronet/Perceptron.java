package root.iv.neuronet;

import java.util.Locale;
import java.util.Random;

public class Perceptron {
    //  Порог функции активации
    private static final int BIAS_R = 2;    // Порог выходного слоя
    private static final int BIAS_A = 2;    // Порог А-слоя
    private int[] weightsAR;
    private int[][] weightsSA;
    private int numSensors;
    private int numAssociators;
    // Активизировавшиеся А-элементы
    private boolean[] activeA;

    public Perceptron(int sensors) {
        this.numSensors = sensors;
        this.numAssociators = sensors;
        this.weightsAR = new int[numAssociators];   // Веса между A-R
        this.weightsSA = new int[numSensors][numAssociators];   // Веса между S-A,
        this.activeA = new boolean[numAssociators];
        randomInitWeightSA();
    }

    private void randomInitWeightSA() {
        // Заполнение {-1,0,1} - значениями связей SA
        Random random = new Random();
        for (int i = 0; i < numSensors; i++) {
            for (int j = 0; j < numAssociators; j++) {
                int r = random.nextInt(3);
                weightsSA[i][j] = 1-r;  // {-1,0,1}
            }
        }
    }

    private void singleInitWeightSA() {
        if (numSensors != numAssociators) throw new IllegalStateException("Чтобы создать единичную матрицу весов количество S и A-элементов должно совпадать");
        weightsSA = MathUtils.getSingleMatrix(numSensors);
    }

    // Является ли полученное число тем, на которое обучается сеть
    public boolean check(Number number) {
        releaseAllA();
        // Пробуеа активировать A-слой
        for (int a = 0; a < numAssociators; a++) {
            int sumA = 0;   // Накапливаем сумму для A-элемента
            for (int s = 0; s < numSensors; s++) {
                if (weightsSA[s][a] != 0) { // Связанные с данным А-элементом сенсоры, (можно убрать)
                    sumA += number.getPixel(s) * weightsSA[s][a];
                }
            }
            activeA[a] = sumA > BIAS_A;
        }

        int sumR = 0;
        // Перебираем А-элементы
        for (int a = 0; a < numAssociators; a++) {
            if (activeA[a]) {   // Если этот элемент активировался, то он участвует в формировании итоговой суммы
                sumR += weightsAR[a];
            }
        }

        return sumR > BIAS_R;
    }

    // Деактивация всех A-элементов
    private void releaseAllA() {
        for (int i = 0; i < activeA.length; i++)
            activeA[i] = false;
    }

    // Уменьшение значений весов, если сеть ошиблась и выдала 1
    private void decrease(Number number) {
        for (int a = 0; a < activeA.length; a++) {
            if (activeA[a]) { // Если a активизировался
                weightsAR[a] -= 1;
            }
        }
    }

    // Увеличение значений весов, если сеть ошиблась и выдала 0
    private void increase(Number number) {
        for (int a = 0; a < activeA.length; a++) {
            if (activeA[a]) {
                weightsAR[a] += 1;
            }
        }
    }

    //        Тренировка сети
    public void traning5(Number[] pattern, Number target, int iterations) {
        for (int i = 0; i < iterations; i++) {
            // Генерируем случайное число от 0 до 9
            Number number = pattern[i % pattern.length];

            boolean answer = check(number);
            // На ложное число сеть сказала "ДА"
            if (number.getValue() != target.getValue() && answer) {
                decrease(number);
            }

            // На нужное число сеть выдала "НЕТ"
            if (number.getValue() == target.getValue() && !answer) {
                increase(number);
            }

        }
    }

    public int[][] getWeightsSA() {
        return weightsSA;
    }

    public int[] getWeightsAR() {
        return weightsAR;
    }

}
