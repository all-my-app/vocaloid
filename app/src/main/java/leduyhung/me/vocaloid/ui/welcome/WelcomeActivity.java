package leduyhung.me.vocaloid.ui.welcome;

import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leduyhung.loglibrary.Logg;

import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.TestActivity;
import leduyhung.me.vocaloid.custom.Indicator.IndicatorView;
import leduyhung.me.vocaloid.custom.SlidingPicture.SlidingPictureView;
import leduyhung.me.vocaloid.ui.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final String KEY_INSTANCE_INDEX_CONTENT = "KEY_INSTANCE_INDEX_CONTENT";

    private TextView tContent, tBack, tNext;
    private SlidingPictureView slidingPictureView;
    private IndicatorView indicatorView;

    private int indexContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (savedInstanceState != null) {
            indexContent = savedInstanceState.getInt(KEY_INSTANCE_INDEX_CONTENT);
        } else {
            indexContent = 0;
        }
        initView();
        showContentByIndex(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INSTANCE_INDEX_CONTENT, indexContent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_back:
                if (indexContent > 0)
                    indexContent--;
                showContentByIndex(true);
                break;
            case R.id.txt_next:
                if (indexContent < 2) {
                    indexContent++;
                    showContentByIndex(true);
                } else {
//                    tNext.setEnabled(false);
                    slidingPictureView.destroy();
                    startActivity(new Intent(this, MainActivity.class));
//                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        slidingPictureView.stop();
    }

    private void initView() {
        tContent = findViewById(R.id.txt_intro);
        tBack = findViewById(R.id.txt_back);
        tNext = findViewById(R.id.txt_next);
        indicatorView = findViewById(R.id.indicator);
        slidingPictureView = findViewById(R.id.sliding_picture);
        tBack.setOnClickListener(this);
        tNext.setOnClickListener(this);
        indicatorView.create();
    }

    private void showContentByIndex(boolean needAnimation) {
        indicatorView.setCurrentIndex(indexContent, needAnimation);
        switch (indexContent) {
            case 0:
                tBack.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition((ViewGroup) tContent.getRootView());
                }
                tContent.setText(getResources().getString(R.string.welcome_intro_1));
                break;
            case 1:
                tBack.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition((ViewGroup) tContent.getRootView());
                }
                tContent.setText(getResources().getString(R.string.welcome_intro_2));
                break;
            case 2:
                tBack.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition((ViewGroup) tContent.getRootView());
                }
                tContent.setText(getResources().getString(R.string.welcome_intro_3));
                break;
            default:
                indexContent = 2;
        }
    }
}
