package root.iv.neuronet.perceptron.remelhart;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import root.iv.neuronet.Logger;
import root.iv.neuronet.perceptron.ArrayUtils;
import root.iv.neuronet.perceptron.cmd.FillRandomCommand;
import root.iv.neuronet.perceptron.rosenblat.Number;

public class PerceptronRumelhart {
    private SensorsLayer    layerS;
    private Layer           layerA;
    private Layer           layerA2;
    private Layer           layerR;

    private List<Number> originals;         // Образцы

    public PerceptronRumelhart(int sizeS, int sizeA, int sizeA2, int sizeR) {
        layerS = new SensorsLayer();
        layerA = new Layer(sizeA, sizeS, new FillRandomCommand());
        layerA2 = new Layer(sizeA2, sizeA, new FillRandomCommand());
        layerR = new Layer(sizeR, sizeA2, new FillRandomCommand());
    }

    public int getOutput(int[] input, StringBuilder logger) {
        forwardSignal(input);

        int iMax = 0;
        logger.append("Out: ");
        for (int r = 0; r < layerR.size(); r++) {
            if (layerR.getValue(r) > layerR.getValue(iMax))  iMax = r;
            logger.append(String.format(Locale.ENGLISH, "%4.1f", layerR.getValue(r)));
        }
        logger.append("\n");

        return iMax;
    }

    private void forwardSignal(int[] input) {
        // Задаем S-слой
        layerS.setValues(input);
        // Отправка сигнала с S-слоая на скрытый A-слой
        layerA.receiveSignal(layerS.getValues());
        // Отправка сигнала с A-слоя на A2
        layerA2.receiveSignal(layerA.getValues());
        // Отправка сигнала с A2-слоя на R-слой
        layerR.receiveSignal(layerA2.getValues());
    }

    private double backPropagationError(Number pattern, int countPatterns) {
        // Обратное распространение ошибки у R-слоя (обучение A-R связей)
        layerR.backPropagation(layerA2.getValues(), pattern.goodOutput(countPatterns));
        // У всех R-элементов теперь есть значения ошибки. Подсчитаем вектор ошибок для отправки на A2-слой
        double[] errorR = layerR.calculateWeightsError(layerA2.size());
        // Полученные взвешенные ошибки распространяем на А2 (обучение A2-R связей)
        layerA2.backPropagationHidden(layerA.getValues(), errorR);

        // У всех A2-элементов теперь есть значения ошибки. ПОдсчитаем вектор ошибок для отправки на A-слой
        double[] errorA2 = layerA2.calculateWeightsError(layerA.size());
        // олученные взвешенные ошибки распространяем на A слой (обучение A-R связей)
        layerA.backPropagationHidden(layerS.getValues(), errorA2);


        double error = 0.0;

        for (double r : errorR)
            error += Math.abs(r);
        for (double r : errorA2)
            error += Math.abs(r);

        return error;
    }

    /**
     *
     * @param minDelta - Минимально изменение, которого необходимо достич
     * @param logger - логи
     * идеальный образец имеет 1.0 на выходе только у одного элемента
     */
    public void train(double minDelta, double error, Logger logger) {

        List<Number> shufleOriginal = new LinkedList<>(originals);
        int countTrue = 0;
        for (long i = 0; countTrue < originals.size()*2; i++) {
            Collections.shuffle(shufleOriginal);
            for (int n = 0; n < shufleOriginal.size(); n++) {
                int[] pattern = shufleOriginal.get(n).getPixs();
                forwardSignal(pattern);
                double stepError = backPropagationError(shufleOriginal.get(n), shufleOriginal.size());


                logger.log(String.format(Locale.ENGLISH, "Learning: %d\n", shufleOriginal.get(n).getValue()));
                logger.log(String.format(Locale.ENGLISH, "Step error: %5.2f\n", stepError));
                StringBuilder l = new StringBuilder();
                int r = getOutput(pattern, l);
                logger.log(l.toString());

                if (originals.get(r).getValue() == shufleOriginal.get(n).getValue() && stepError < 1.0)
                    countTrue++;
                else
                    countTrue = 0;
                logger.log("CountTrue: " + countTrue);
                logger.log(" ");

            }
        }

        logger.log("\n");
    }

    public void setOriginalNumbers(List<Number> numbers) {
        this.originals = numbers;
    }
}
