package root.iv.neuronet.perceptron.remelhart;

import java.util.List;
import java.util.Locale;

public class Train {
    private PerceptronRumelhart perceptron;
    private List<TrainSet> trainSets;

    public Train() {
    }

    /**
     *
     * @param count - Количество этапов, сколько раз нужно прогнать все примеры
     */
    public void train(long count) {
        for (long i = 0; i < count; i++) {
            for (TrainSet set : trainSets) {
                perceptron.setInput(set.getInput());
                perceptron.adjustWages(set.getGoodOutput());
            }
        }
    }

    public void setInputs(int[] inputs) {
        perceptron.setInput(inputs);
    }

    public void addTrainingSet(TrainSet set) {
        trainSets.add(set);
    }

    /**
     * Возвращает индекс R-элемента, лучше всего среагировавшего
     * @return
     */
    public int getOutputs(StringBuilder logger) {
        double[] out =  perceptron.getOutputs();
        logger.append("Out: ");
        int i_max = 0;

        for (int i = 0; i < out.length; i++) {
            if (out[i] > out[i_max]) i_max = i;
            logger.append(String.format(Locale.ENGLISH, "%4.1f", out[i]));
        }
        logger.append("\n");

        return i_max;
    }


    public void buildPerceptron(int count) {
        this.perceptron = new PerceptronRumelhart();
        this.perceptron.addNeurons(count);
    }

    public void setTrainSets(List<TrainSet> trainSets) {
        this.trainSets = trainSets;
    }
}
