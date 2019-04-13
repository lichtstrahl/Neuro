package root.iv.neuro.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import root.iv.neuro.R;
import root.iv.neuro.ui.SimpleCanvas;
import root.iv.neuro.util.BitmapConverter;
import root.iv.neuronet.Number;
import root.iv.neuronet.Perceptron;

public class NeuroFragment extends Fragment {
    private static final int SIZE_PREVIEW = 6;
    @BindView(R.id.canvas)
    protected SimpleCanvas simpleCanvas;
    @BindView(R.id.preview)
    protected ImageView imagePreview;
    @BindView(R.id.viewCurrentPattern)
    protected TextView viewCurrentPattern;
    private Number[] pattern;
    private int currentPattern = -1;
    private Perceptron perceptron = new Perceptron(SIZE_PREVIEW*SIZE_PREVIEW);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neuronet, container, false);
        ButterKnife.bind(this, view);

        imagePreview.setOnClickListener(v -> {
            Bitmap scaled = getPreview();
            imagePreview.setImageBitmap(scaled);
            Toast.makeText(this.getContext(), String.format(Locale.ENGLISH, "%d %d", scaled.getWidth(), scaled.getHeight()), Toast.LENGTH_SHORT).show();
        });

        pattern = new Number[2];
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
        pattern[currentPattern] = BitmapConverter.createNumber(scaled, currentPattern);
        updateCurrentPattern();
        imagePreview.setImageBitmap(scaled);
        Toast.makeText(this.getContext(), "Шаблонов: " + currentPattern, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonTrain)
    public void clickTrain() {
        int target = 0;
        Bitmap scaled = getPreview();
        boolean answer = perceptron.check(BitmapConverter.createNumber(scaled, target));
        Toast.makeText(this.getContext(), String.format(Locale.ENGLISH, "Проверка на %d - %b", target, answer), Toast.LENGTH_SHORT).show();
    }

    private void updateCurrentPattern() {
        if (++currentPattern == pattern.length) {
            // Запуск обучения

            perceptron.traning5(pattern, pattern[0], 10000);
            Toast.makeText(this.getContext(), "Обучение " + pattern[0].getValue() + " Закончено", Toast.LENGTH_SHORT).show();


//            Completable.fromCallable(() -> {
//                Perceptron perceptron = new Perceptron(pattern[0].getSize());
//                perceptron.traning5(pattern, pattern[4], 50000, (str) -> {
//                    App.logI(str);
//                });
//                return true;
//            })
//            .subscribeOn(Schedulers.io())
//            .subscribeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                    () -> {
//
//                    },
//                    (error) ->{
//                        App.logE(error.getMessage());
//                    }
//            );

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
