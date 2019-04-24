package root.iv.neuronet.perceptron.remelhart;

import java.util.List;

public class Train {
    private static final int NEURON_COUNT = 26;
    private PerceptronRumelhart perceptron;
    private List<TrainSet> trainSets;

    /**
     * @param set   - наборы для тренировок
     * @param count - количество нейронов
     */
    public Train(List<TrainSet> set, int count) {
        this.perceptron = new PerceptronRumelhart();
        this.perceptron.addNeurons(count);
        this.trainSets = set;
    }

    public void train(long count) {
        for (long i = 0; i < count; i++) {
            int index = ((int) (Math.random() * trainSets.size()));
            TrainSet set = trainSets.get(index);
            perceptron.setInput(set.getInput());
            perceptron.adjustWages(set.getGoodOutput());
        }
    }

    public void setInputs(List<Integer> inputs) {
        perceptron.setInput(inputs);
    }

    public void addTrainingSet(TrainSet set) {
        trainSets.add(set);
    }

    public List<Double> getOutputs() {
        return perceptron.getOutputs();
    }
}
