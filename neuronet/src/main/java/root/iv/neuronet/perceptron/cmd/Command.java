package root.iv.neuronet.perceptron.cmd;

public interface Command<T> {
    void execute();
    void setArgumet(T x);
}
