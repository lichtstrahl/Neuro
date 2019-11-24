package root.iv.neuro.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import root.iv.neuro.R;

public class CoordinatorFragment extends Fragment {

    public static CoordinatorFragment getInstance() {
        return new CoordinatorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coordinator, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.fab)
    public void click(View view) {
        Snackbar.make(view, "Hello!", Snackbar.LENGTH_SHORT).show();
    }
}
