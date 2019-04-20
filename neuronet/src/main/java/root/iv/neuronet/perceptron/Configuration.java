package root.iv.neuronet.perceptron;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import root.iv.neuronet.perceptron.cmd.Command;

public class Configuration implements Serializable {
    public static final String CONFIG_FILENAME = "temp.config";
    private int biasA;
    private int biasR;
    private int countR;
    private int countA;
    private int countS;
    private Command<int[]> fillTypeSA;      // Тип заполнения весовых коэффициентов между SA
    private Command<int[]> fillTypeAR;

    public Configuration(int biasA, int biasR, int countR, int countA, int countS, Command<int[]> fillTypeSA, Command<int[]> fillTypeAR) {
        this.biasA = biasA;
        this.biasR = biasR;
        this.countR = countR;
        this.countA = countA;
        this.countS = countS;
        this.fillTypeSA = fillTypeSA;
        this.fillTypeAR = fillTypeAR;
    }

    public void update(Configuration configuration) {
        this.biasA = configuration.biasA;
        this.biasR = configuration.biasR;
        this.countR = configuration.countR;
        this.countA = configuration.countA;
        this.fillTypeSA = configuration.fillTypeSA;
        this.fillTypeAR = configuration.fillTypeAR;
    }

    public void saveToFile(File parent) {
        File file = new File(parent, CONFIG_FILENAME);
        try (
                ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))
        ) {
            stream.writeObject(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadFromFile(File path) {
        try (
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path))
                ) {
            Configuration temp = (Configuration) stream.readObject();
            update(temp);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public int getBiasA() {
        return biasA;
    }

    public int getBiasR() {
        return biasR;
    }

    public int getCountR() {
        return countR;
    }

    public int getCountA() {
        return countA;
    }

    public Command<int[]> getFillTypeSA() {
        return fillTypeSA;
    }

    public Command<int[]> getFillTypeAR() {
        return fillTypeAR;
    }

    public int getCountS() {
        return countS;
    }
}
