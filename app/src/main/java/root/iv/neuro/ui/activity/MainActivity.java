package root.iv.neuro.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import root.iv.neuro.R;
import root.iv.neuro.ui.fragment.NeuroFragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFrame, NeuroFragment.getInstance())
                .commit();
    }
}
