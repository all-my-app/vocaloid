package leduyhung.me.vocaloid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.leduyhung.loglibrary.Logg;

import leduyhung.me.vocaloid.custom.SlidingLayout.SlidingLayout;
import leduyhung.me.vocaloid.custom.SlidingLayout.SlidingLayoutListener;
import leduyhung.me.vocaloid.custom.SlidingLayout.SlidingState;

public class TestActivity extends AppCompatActivity {

    private SlidingLayout slidingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        slidingLayout = findViewById(R.id.sliding_layout);
        slidingLayout.setSlidingLayoutListener(new SlidingLayoutListener() {
            @Override
            public void onScrollChange(int y, SlidingState.STATE state) {

                Logg.error(getClass(), y + " ---- " + state.toString());
            }
        });
    }
}