package root.iv.neuro.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import root.iv.neuro.R;
import root.iv.neuro.app.App;
import root.iv.neuro.ui.SimpleCanvas;
import root.iv.neuro.ui.adapter.NumberAdapter;
import root.iv.neuro.util.BitmapConverter;
import root.iv.neuronet.Number;
import root.iv.neuronet.perceptron.cmd.FillConstantCommand;
import root.iv.neuronet.perceptron.remelhart.PerceptronRumelhart;
import root.iv.neuronet.perceptron.remelhart.Train;
import root.iv.neuronet.perceptron.remelhart.TrainSet;
import root.iv.neuronet.perceptron.rosenblat.Configuration;


public class NeuroFragment extends Fragment {
    private static final int SIZE_PREVIEW = 10;
    private static final Configuration configurationPointToPoint = new Configuration(
            1,
            SIZE_PREVIEW*SIZE_PREVIEW/10,
            3,
            SIZE_PREVIEW*SIZE_PREVIEW, // количество A
            SIZE_PREVIEW*SIZE_PREVIEW,
            new FillConstantCommand(0),
            new FillConstantCommand(0)
    );
    private static final Configuration configuration = configurationPointToPoint;

    @BindView(R.id.canvas)
    protected SimpleCanvas simpleCanvas;
    @BindView(R.id.viewCurrentPattern)
    protected TextView viewCurrentPattern;
    @BindView(R.id.progress)
    protected ProgressBar progressBar;
    @BindView(R.id.listNumbers)
    protected RecyclerView listNumbers;
    private NumberAdapter numberAdapter;
    private int currentPattern = -1;
    private CompositeDisposable disposable;
    private Train train;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neuronet, container, false);
        ButterKnife.bind(this, view);

        disposable = new CompositeDisposable();

        numberAdapter = new NumberAdapter(getLayoutInflater(), v -> {
            int t = listNumbers.getChildAdapterPosition(v);
            Path p = numberAdapter.getPath(t);
            simpleCanvas.setPath(p);
            simpleCanvas.invalidate();
        });
        listNumbers.setAdapter(numberAdapter);
        listNumbers.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        updateCurrentPattern();

        return view;
    }

    private Bitmap getPreview() {
        return Bitmap.createScaledBitmap(getCanvasBitmap(), SIZE_PREVIEW, SIZE_PREVIEW, false);
    }

    private Bitmap getCanvasBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(simpleCanvas.getWidth(), simpleCanvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        simpleCanvas.draw(canvas);
        return bitmap;
    }

    @OnClick(R.id.buttonClear)
    void clickButtonClear() {
        simpleCanvas.clear();
        simpleCanvas.invalidate();
    }

    @OnClick(R.id.buttonAddPattern)
    void clickAppendPattern() {
        Bitmap scaled = getPreview();
        numberAdapter.append(BitmapConverter.createNumber(scaled, currentPattern), scaled, simpleCanvas.getPath());
        updateCurrentPattern();
        Toast.makeText(this.getContext(), "Шаблонов: " + currentPattern, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonReset)
    void clickButtonReset() {
        currentPattern = -1;
        updateCurrentPattern();
        numberAdapter.clear();
    }

    @OnClick(R.id.buttonCheck)
    void clickCheck() {
        Bitmap scaled = getPreview();
        StringBuilder logger = new StringBuilder();
//        int answer = perceptron.check(BitmapConverter.createNumber(scaled, 0), logger);
//        App.logI(logger.toString());
//        if (answer >= 0) {
//            Toast.makeText(this.getContext(), String.format(Locale.ENGLISH, "Это число %d", answer), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this.getContext(), "Не удалось определить число", Toast.LENGTH_SHORT).show();
//        }
        train.setInputs(BitmapConverter.createNumber(scaled, 0).getPixs());
        List<Double> out = train.getOutputs();
        logger.append("Out: ");
        for (Double d : out)
            logger.append(String.format(Locale.ENGLISH, "%4.1f", d));
        logger.append("\n");
        App.logI(logger.toString());
    }

    @OnClick(R.id.buttonTrain)
    void clickTrain() {
        progressBar.setVisibility(View.VISIBLE);
        Disposable d = Completable.fromCallable(() -> {
//            StringBuilder logger = new StringBuilder();
            List<TrainSet> sets = new LinkedList<>();
            for (int i = 0; i < numberAdapter.getItemCount(); i++)
                sets.add(buildTrainSet(numberAdapter.getNumbers().get(i), i));

            train = new Train();
            train.setTrainSets(sets);
            train.buildPerceptron(numberAdapter.getItemCount());
            train.train(1000);
//            perceptron.traning(numberAdapter.getNumbers().toArray(new Number[0]), numberAdapter.getItemCount(), logger);
//            App.logI(logger.toString());
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Toast.makeText(this.getContext(), "Обучение Закончено", Toast.LENGTH_SHORT).show() ;
//                            viewCurrentPattern.setText(String.format(Locale.ENGLISH, "Количество живых нйронов: %d", perceptron.countLiveA()));
                            progressBar.setVisibility(View.GONE);
                        }
                );

        disposable.add(d);
    }

    private TrainSet buildTrainSet(Number n, int index) {
        double[] good = new double[numberAdapter.getItemCount()];

        for (int i = 0; i < good.length; i++) {
            good[i] = (i == index) ? 1.0 : 0.0;
        }

        return new TrainSet(n.getPixs(), good);
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }

    private void updateCurrentPattern() {
        currentPattern++;
        viewCurrentPattern.setText(String.format(Locale.ENGLISH, "Нарисуйте %d", currentPattern));
    }

    public static NeuroFragment getInstance() {
        NeuroFragment fragment = new NeuroFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }
}
