package root.iv.neuro.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.junit.Assert;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import root.iv.neuro.R;
import root.iv.neuro.app.App;
import root.iv.neuro.ui.SimpleCanvas;
import root.iv.neuro.ui.adapter.NumberAdapter;
import root.iv.neuro.util.BitmapConverter;
import root.iv.neuronet.perceptron.cmd.FillConstantCommand;
import root.iv.neuronet.perceptron.remelhart.PerceptronRumelhart;
import root.iv.neuronet.perceptron.rosenblat.Configuration;
import timber.log.Timber;


public class NeuroFragment extends Fragment {
    private static final int SIZE_PREVIEW = 10;
    private static final int NEURON_COUNT = SIZE_PREVIEW*SIZE_PREVIEW;
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
    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.appbar_shadow)
    protected View appBarShadow;
    @BindView(R.id.buttonCheck)
    protected ImageButton buttonCkeck;
    private NumberAdapter numberAdapter;

    private int currentPattern = -1;
    @Nullable
    private CompositeDisposable disposable;
    private PerceptronRumelhart perceptron;
    private int countLive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neuronet, container, false);
        ButterKnife.bind(this, view);

        numberAdapter = new NumberAdapter(getLayoutInflater(), v -> {
            int t = listNumbers.getChildAdapterPosition(v);
            Path p = numberAdapter.getPath(t);
            simpleCanvas.setPath(p);
            simpleCanvas.invalidate();
        });
        listNumbers.setAdapter(numberAdapter);
        listNumbers.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        updateCurrentPattern();

        appBarLayout.addOnOffsetChangedListener((appbar, offset) -> {
            int range = appbar.getTotalScrollRange();
            float pr = -offset/(float)range; // Сколько процентов дистанции пройдено
            appBarShadow.setAlpha(pr*pr*pr*pr); // Замена x, на x*x
            App.logI(String.format(Locale.ENGLISH, "%8.3f", pr));
        });

        buttonCkeck.setVisibility(View.GONE);

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
        buttonCkeck.setVisibility(View.VISIBLE);
        Toast.makeText(this.getContext(), "Шаблонов: " + currentPattern, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonReset)
    void clickButtonReset() {
        currentPattern = -1;
        updateCurrentPattern();
        numberAdapter.clear();
        buttonCkeck.setVisibility(View.GONE);
    }

    @OnClick(R.id.buttonCheck)
    void clickCheck() {
        Bitmap scaled = getPreview();
        StringBuilder logger = new StringBuilder();
        int answer = perceptron.getOutput(BitmapConverter.createNumber(scaled, 0).getPixs(), logger);
        Toast.makeText(this.getContext(), String.format(Locale.ENGLISH, "Это число: " + numberAdapter.getNumbers().get(answer).getValue()), Toast.LENGTH_SHORT).show();
        App.logI(logger.toString());
    }

    @OnClick(R.id.buttonTrain)
    void clickTrain() {
        disposable = new CompositeDisposable();
        progressBar.setVisibility(View.VISIBLE);
        disposable.add(
                Completable.fromCallable(() -> {
                    perceptron = new PerceptronRumelhart(NEURON_COUNT, NEURON_COUNT, numberAdapter.getItemCount());
                    PerceptronRumelhart.Report report = perceptron
                            .setOriginalNumbers(numberAdapter.getNumbers())
                            .train(1.0, Timber::i);
                    Snackbar.make(this.appBarShadow, "Отчет получен", Snackbar.LENGTH_SHORT).show();
                    return countLive;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this.getContext(), "Обучение Закончено", Toast.LENGTH_SHORT).show();
                    viewCurrentPattern.setText(String.format(Locale.ENGLISH, "Живых нейронов: %d из %d", countLive, NEURON_COUNT));
                })
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) {
            progressBar.setVisibility(View.GONE);
            disposable.dispose();
        }
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
