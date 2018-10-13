package leduyhung.me.vocaloid.custom.Indicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.leduyhung.loglibrary.Logg;

import java.util.List;

import leduyhung.me.vocaloid.R;

public class IndicatorView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {

    private final int DURATION_ANIMATION = 300;

    private Context mContext;

    private int width, height;
    private int widthHeightIndicator;
    private int yIndicator;
    private int currentIndex;
    private int firstCoordinate;

    private int color;
    private int radius;
    private int distance;
    private int numberIndicator;

    private DotSelectView dotSelectView;

    private ObjectAnimator transitionX;

    public IndicatorView(Context context) {
        super(context);
        getAttribute(mContext, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttribute(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    public boolean onPreDraw() {
        if (width == 0) {
            initIndicator();
        }
        getViewTreeObserver().removeOnPreDrawListener(this);
        return false;
    }

    private void getAttribute(Context context, AttributeSet attrs) {

        this.mContext = context;
        if (attrs != null) {

            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
            color = typedArray.getColor(R.styleable.IndicatorView_indicator_dot_color, mContext.getResources().getColor(R.color.colorWhite));
            numberIndicator = typedArray.getInteger(R.styleable.IndicatorView_indicator_dot_number, 0) - 1;
            radius = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_dot_radius, 0);
            distance = typedArray.getDimensionPixelOffset(R.styleable.IndicatorView_indicator_dot_distance, 0);
            typedArray.recycle();
        }
        setWillNotDraw(false);
    }

    private void initIndicator() {
        if (numberIndicator > 0) {
            width = getWidth();
            height = getHeight();
            yIndicator = height / 2 - radius / 2;
            int sizeIndicator = (radius * 2) * numberIndicator + ((numberIndicator - 1) * distance);
            firstCoordinate = width / 2 - sizeIndicator / 2 - radius / 2;
            FrameLayout.LayoutParams layoutParams = new LayoutParams(radius, radius);
            for (int i = 0; i <= numberIndicator; i++) {
                final DotUnSelectView dotUnSelectView = new DotUnSelectView(mContext);
                dotUnSelectView.setLayoutParams(layoutParams);
                dotUnSelectView.setX(firstCoordinate + (i * (radius * 2) + distance));
                dotUnSelectView.setY(yIndicator);
                dotUnSelectView.setColor(color);
                dotUnSelectView.setRadius(radius);
                addView(dotUnSelectView);
            }

            dotSelectView = new DotSelectView(mContext);
            dotSelectView.setLayoutParams(layoutParams);
            dotSelectView.setX(firstCoordinate + (currentIndex * (radius * 2) + distance));
            dotSelectView.setY(yIndicator);
            dotSelectView.setColor(color);
            dotSelectView.setRadius(radius);
            addView(dotSelectView);
        }
    }

    private void animationDotSelect(int x) {
        if (transitionX != null && transitionX.isRunning()) {
            transitionX.cancel();
        }
        transitionX = ObjectAnimator.ofFloat(dotSelectView, View.TRANSLATION_X, x);
        transitionX.setDuration(DURATION_ANIMATION);
        transitionX.start();
    }

    public void setCurrentIndex(int index, boolean needAnimation) {
        if (index < 0 || index > numberIndicator)
            return;
        this.currentIndex = index;
        int newX = firstCoordinate + (currentIndex * (radius * 2) + distance);
        if (dotSelectView != null) {
            if (dotSelectView.getX() == newX)
                return;
            if (needAnimation)
                animationDotSelect(newX);
            else
                dotSelectView.setX(newX);
        }
    }

    public void create() {

    }
}