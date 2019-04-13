package root.iv.neuro.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import root.iv.neuronet.Perceptron;

public class NeuroFragment extends Fragment {
    private static final int SIZE_PREVIEW = 5;
    @BindView(R.id.canvas)
    protected SimpleCanvas simpleCanvas;
    @BindView(R.id.viewCurrentPattern)
    protected TextView viewCurrentPattern;
    @BindView(R.id.progress)
    protected ProgressBar progressBar;
    @BindView(R.id.listNumbers)
    RecyclerView listNumbers;
    private NumberAdapter numberAdapter;
    private int currentPattern = -1;
    private Perceptron perceptron = new Perceptron(SIZE_PREVIEW*SIZE_PREVIEW);
    private CompositeDisposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neuronet, container, false);
        ButterKnife.bind(this, view);

        disposable = new CompositeDisposable();

        numberAdapter = new NumberAdapter(getLayoutInflater());
        listNumbers.setAdapter(numberAdapter);
        listNumbers.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        updateCurrentPattern();

        return view;
    }

    private Bitmap getPreview() {
        Bitmap bitmap = Bitmap.createBitmap(simpleCanvas.getWidth(), simpleCanvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        simpleCanvas.draw(canvas);
        return Bitmap.createScaledBitmap(bitmap, SIZE_PREVIEW, SIZE_PREVIEW, false);
    }

    @OnClick(R.id.buttonClear)
    public void clickButtonClear() {
        simpleCanvas.clear();
        simpleCanvas.invalidate();
    }

    @OnClick(R.id.buttonAddPattern)
    public void clickAppendPattern() {
        Bitmap scaled = getPreview();
        numberAdapter.append(BitmapConverter.createNumber(scaled, currentPattern), scaled);
        updateCurrentPattern();
        Toast.makeText(this.getContext(), "Шаблонов: " + currentPattern, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonTrain)
    public void clickTrain() {
        // Запуск обучения
        progressBar.setVisibility(View.VISIBLE);
        final Number target = numberAdapter.getItem(0);
        Disposable d = Completable.fromCallable(() -> {
            perceptron.traning5(numberAdapter.getNumbers().toArray(new Number[0]), target, 10000);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Toast.makeText(this.getContext(), "Обучение " + target.getValue() + " Закончено", Toast.LENGTH_SHORT).show() ;
                            progressBar.setVisibility(View.GONE);
                        },
                        (error) -> App.logE(error.getMessage())
                );

        disposable.add(d);
    }

    @OnClick(R.id.buttonCheck)
    public void clickCheck() {
        int target = 0;
        Bitmap scaled = getPreview();
        boolean answer = perceptron.check(BitmapConverter.createNumber(scaled, target));
        Toast.makeText(this.getContext(), String.format(Locale.ENGLISH, "Проверка на %d - %b", target, answer), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }

    private void updateCurrentPattern() {
        if (++currentPattern == numberAdapter.getItemCount()) {
            currentPattern = 0;
        }

        viewCurrentPattern.setText(String.format(Locale.ENGLISH, "Нарисуйте %d", currentPattern));
    }

    public static NeuroFragment getInstance() {
        NeuroFragment fragment = new NeuroFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        return fragment;
    }
}
