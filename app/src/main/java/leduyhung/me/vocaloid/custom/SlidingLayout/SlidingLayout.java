package leduyhung.me.vocaloid.custom.SlidingLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.leduyhung.loglibrary.Logg;

import leduyhung.me.vocaloid.R;

public class SlidingLayout extends ScrollView implements ViewTreeObserver.OnGlobalLayoutListener,
        ViewTreeObserver.OnScrollChangedListener {

    private final int POINT_TO_SCROLL_CHANGE_STATE = 100;
    private final int TIME_SMOOTH_SCROLL = 200;
    private final int ID_FRAME_CONTENT = 10929381;

    private Context mContext;

    private LinearLayout linearParent;
    private FrameLayout frameContent;
    private View vOverlay;

    private int width, height, heightToTouch;
    private float maxOverlay;
    private int maxScroll, currentY;

    private SlidingState.STATE state;

    private SlidingLayoutListener listener;

    public SlidingLayout(Context context) {
        super(context);
        getAtribute(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAtribute(context, attrs);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAtribute(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.width = getWidth();
        this.height = getHeight();
        maxScroll = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getChildCount() == 0) {
            getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {

        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initView();
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onScrollChanged() {

        currentY = getScrollY();
        vOverlay.setAlpha(calculateAlphaOverlay(currentY));
        if (listener != null)
            listener.onScrollChange(currentY, state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (state == SlidingState.STATE.SCROLLING) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                handleScrollWhenTouch();
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void getAtribute(Context mContext, AttributeSet attributeSet) {
        this.mContext = mContext;

        if (attributeSet != null) {

        }

        maxOverlay = 0.8f;
        heightToTouch = 0;
        state = SlidingState.STATE.COLLAPSE;
        getViewTreeObserver().addOnScrollChangedListener(this);
    }

    private void initView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearParent = new LinearLayout(mContext);
        linearParent.setOrientation(LinearLayout.VERTICAL);
        linearParent.setLayoutParams(layoutParams);
        vOverlay = new View(mContext);
        LayoutParams params = new LayoutParams(width, height - heightToTouch);
        vOverlay.setLayoutParams(params);
        vOverlay.setBackgroundColor(mContext.getResources().getColor(R.color.colorblack));
        vOverlay.setAlpha(0f);
        frameContent = new FrameLayout(mContext);
        frameContent.setId(ID_FRAME_CONTENT);
        frameContent.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        frameContent.setLayoutParams(params);
        linearParent.addView(vOverlay);
        linearParent.addView(frameContent);
        addView(linearParent);
    }

    private float calculateAlphaOverlay(int y) {

        return y * maxOverlay / maxScroll;
    }

    private void handleScrollWhenTouch() {

        if (state == SlidingState.STATE.EXPANSE) {
            if (currentY < maxScroll - POINT_TO_SCROLL_CHANGE_STATE)
                close();
            else
                open();
        } else if (state == SlidingState.STATE.COLLAPSE) {
            if (currentY > POINT_TO_SCROLL_CHANGE_STATE) {
                open();
            }
            else {
                close();
            }
        } else {

        }
    }

    private void smoothScroll(int y, final SlidingState.STATE newState) {
        ValueAnimator animScroll =
                ValueAnimator.ofInt(currentY, y);
        animScroll.setDuration(TIME_SMOOTH_SCROLL);
        animScroll.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                state = SlidingState.STATE.SCROLLING;
                smoothScrollTo(0, (int) valueAnimator.getAnimatedValue());
            }
        });
        animScroll.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                state = newState;
                if (listener != null)
                    listener.onScrollChange(currentY, state);
            }
        });
        animScroll.start();
    }

    public void open() {
        post(new Runnable() {
            @Override
            public void run() {
                smoothScroll(maxScroll, SlidingState.STATE.EXPANSE);
            }
        });
    }

    public void close() {
        post(new Runnable() {
            @Override
            public void run() {
                smoothScroll(0, SlidingState.STATE.COLLAPSE);
            }
        });
    }

    public SlidingState.STATE getState() {
        return state;
    }

    public void addFragment(FragmentManager manager, Fragment fragment) {

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(ID_FRAME_CONTENT, fragment).addToBackStack(fragment.getTag());
        transaction.commit();
    }

    public void setSlidingLayoutListener(SlidingLayoutListener listener) {
        this.listener = listener;
    }
}