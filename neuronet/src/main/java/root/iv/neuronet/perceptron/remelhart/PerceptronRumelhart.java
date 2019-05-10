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
    private Layer           layerR;

    private List<Number> originals;         // Образцы

    public PerceptronRumelhart(int sizeS, int sizeA, int sizeR) {
        layerS = new SensorsLayer();
        layerA = new Layer(sizeA, sizeS, new FillRandomCommand());
        layerR = new Layer(sizeR, sizeA, new FillRandomCommand());
    }

    public int getOutput(int[] input, StringBuilder logger) {
        forwardSignal(input, false);

        int iMax = 0;
        logger.append("Out: ");
        for (int r = 0; r < layerR.size(); r++) {
            if (layerR.getValue(r) > layerR.getValue(iMax))  iMax = r;
            logger.append(String.format(Locale.ENGLISH, "%4.1f", layerR.getValue(r)));
        }
        logger.append("\n");

        return iMax;
    }

    private void forwardSignal(int[] pattern, boolean searchCopy) {
        // Задаем значение S-слоя
        layerS.setValues(pattern);
        // Отправка сигнала с S-слоая на скрытый A-слой
        layerA.receiveSignal(layerS.getValues());
        // Отправка сигнала с A-слоя на R-слой
        layerR.receiveSignal(layerA.getValues());

        // Посчитали. У А-элементов есть какие-то значения.
        if (searchCopy) layerA.searchCopy();
    }

    private double backPropagationError(double[] original) {
        // Обратное распространение ошибки у R-слоя (обучение A-R связей)
        layerR.backPropagation(layerA.getValues(), original);
        // У всех R-элементов теперь есть значения ошибки. Подсчитаем вектор ошибок для отправки на A-слой
        double[] errorR = layerR.calculateWeightsError(layerA.size());
        // Полученные взвешенные ошибки распространяем на А (обучение S-A связей)
        layerA.backPropagationHidden(layerS.getValues(), errorR);

        return ArrayUtils.sumABS(errorR);
    }

    /**
     * @param logger - логи
     * @return Количество живых нейронов в A-слое
     * идеальный образец имеет 1.0 на выходе только у одного элемента
     */
    public int train(double error, Logger logger) {

        List<Number> shufleOriginal = new LinkedList<>(originals);
        int countTrue = 0;
        for (long i = 0; countTrue < originals.size()*2; i++) {
            Collections.shuffle(shufleOriginal);
            layerA.reset();
            for (int n = 0; n < shufleOriginal.size(); n++) {
                int[] pattern = shufleOriginal.get(n).getPixs();
                forwardSignal(pattern, false);
                double stepError = backPropagationError(shufleOriginal.get(n).goodOutput(shufleOriginal.size()));

                StringBuilder l = new StringBuilder();
                int r = getOutput(pattern, l);

                if (originals.get(r).getValue() == shufleOriginal.get(n).getValue() && stepError < error)
                    countTrue++;
                else
                    countTrue = 0;

                // Logging
                logger.log(String.format(Locale.ENGLISH, "Learning: %d%n", shufleOriginal.get(n).getValue()));
                logger.log(String.format(Locale.ENGLISH, "Step error: %5.2f%n", stepError));
                logger.log(l.toString());
                logger.log("CountTrue: " + countTrue);
                logger.log(" ");
            }
            logger.log("Count constant: " + layerA.countConstant());
            StringBuilder l = new StringBuilder();
            layerA.logCopy(l);
            logger.log(l.toString());
            layerA.deleteConstantNeuron();
            logger.log(" ");
        }

        logger.log(" ");
        return layerA.countNotNull();
    }

    public void setOriginalNumbers(List<Number> numbers) {
        this.originals = numbers;
    }
}
