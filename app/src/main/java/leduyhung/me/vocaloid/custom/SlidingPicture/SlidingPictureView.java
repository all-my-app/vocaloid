package leduyhung.me.vocaloid.custom.SlidingPicture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import leduyhung.me.vocaloid.R;

public class SlidingPictureView extends HorizontalScrollView {

    private final int DURATION_TRANSITION = 17000;
    private final int DURATION_FADEIN = 3000;

    private Context mContext;
    private ObjectAnimator transition, fadeIn;
    private AnimatorSet animSet;
    private TimerTask timerTaskLoopAnimation;
    private Timer timerLoopAnimation;

    private FrameLayout layoutParent;

    private List<Drawable> pictures;
    private boolean isRun;
    private int widthPicture;
    private int indexPicture;

    public SlidingPictureView(Context context) {
        super(context);
        getAttribute(context, null);
    }

    public SlidingPictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
    }

    public SlidingPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttribute(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initLayout();
    }

    private void getAttribute(Context mContext, AttributeSet attrs) {
        this.mContext = mContext;
        if (attrs != null) {

        }
        pictures = new ArrayList<>();
        pictures.add(mContext.getResources().getDrawable(R.drawable.welcome1));
        pictures.add(mContext.getResources().getDrawable(R.drawable.welcome2));
        pictures.add(mContext.getResources().getDrawable(R.drawable.welcome3));
        pictures.add(mContext.getResources().getDrawable(R.drawable.welcome4));
        isRun = true;
    }

    private void initLayout() {
        if (layoutParent == null) {
            FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParent = new FrameLayout(mContext);
            layoutParent.setLayoutParams(params);
            addView(layoutParent);
            if (isRun)
                play();
        }
    }

    private void initAnimation(ImageView target) {
        transition = ObjectAnimator.ofFloat(target, TRANSLATION_X, -widthPicture);
        transition.setDuration(DURATION_TRANSITION);
        fadeIn = ObjectAnimator.ofFloat(target, ALPHA, 1f);
        fadeIn.setDuration(DURATION_FADEIN);
    }

    private void initTimerLoopAnimation() {
        timerTaskLoopAnimation = new TimerTask() {
            @Override
            public void run() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        addViewToRunAnimation();
                    }
                });
            }
        };
        timerLoopAnimation = new Timer();
        timerLoopAnimation.schedule(timerTaskLoopAnimation, DURATION_TRANSITION - DURATION_FADEIN);
    }

    private void addViewToRunAnimation() {
        final ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(layoutParent.getLayoutParams());
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        imageView.setImageDrawable(pictures.get(indexPicture));
        indexPicture++;
        if (indexPicture > 3)
            indexPicture = 0;
        imageView.setAdjustViewBounds(true);
        imageView.setAlpha(0f);
        layoutParent.addView(imageView);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                widthPicture = imageView.getMeasuredWidth() - ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
                runAnimation(imageView);
            }
        });
    }

    private void runAnimation(final ImageView imageView) {
        initAnimation(imageView);
        animSet = new AnimatorSet();
        animSet.setTarget(imageView);
        animSet.playTogether(fadeIn, transition);
        animSet.start();
        initTimerLoopAnimation();
        animSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutParent.removeView(imageView);
            }
        });
    }

    public void play() {

        isRun = true;
        addViewToRunAnimation();
    }

    public void stop() {
        if (timerLoopAnimation != null) {
            timerLoopAnimation.cancel();
            timerLoopAnimation.purge();
        }
        if (animSet != null && animSet.isRunning()) {
            animSet.cancel();
            System.gc();
        }
    }
}