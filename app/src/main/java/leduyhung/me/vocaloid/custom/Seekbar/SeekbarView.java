package leduyhung.me.vocaloid.custom.Seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import leduyhung.me.vocaloid.R;

public class SeekbarView extends View {

    private Context mContext;
    private int width, top, bottom, currentSeek, oldSeek;
    private int colorSeek, colorBackground;
    private int maxProgress, progress;
    private boolean changeFromUser;

    private Paint paintSeek, paintBackground;
    private SeekbarCallback callback;

    public SeekbarView(Context context) {
        super(context);
        getAttribute(context, null);
    }

    public SeekbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttribute(context, attrs);
    }

    public SeekbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttribute(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.width = getWidth();
        this.top = getHeight() / 3;
        this.bottom = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawSeek(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            changeFromUser = true;
            this.currentSeek = (int) event.getX();
            if (currentSeek != oldSeek) {
                postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            changeFromUser = true;
            this.currentSeek = (int) event.getX();
            if (currentSeek != oldSeek) {
                postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            changeFromUser = false;
        }
        return true;
    }

    private void getAttribute(Context mContext, AttributeSet attrs) {

        this.mContext = mContext;

        if (attrs != null) {

            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SeekbarView);
            colorSeek = typedArray.getColor(R.styleable.SeekbarView_seekbar_color, mContext.getResources().getColor(R.color.colorAccent));
            colorBackground = typedArray.getColor(R.styleable.SeekbarView_seekbar_background, mContext.getResources().getColor(R.color.colorPrimary));
            progress = typedArray.getInteger(R.styleable.SeekbarView_seekbar_progress, 0);
            maxProgress = typedArray.getInteger(R.styleable.SeekbarView_seekbar_max_progress, 100);
            typedArray.recycle();
        }

        paintSeek = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSeek.setColor(colorSeek);

        paintBackground = new Paint();
        paintBackground.setColor(colorBackground);
        changeFromUser = false;
    }

    private void drawBackground(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.ADD);
        canvas.drawRect(0, top, width, bottom, paintBackground);
    }

    private void drawSeek(Canvas canvas) {

        calculateCurrentSeek();
        canvas.drawRect(0, top, currentSeek, bottom, paintSeek);
    }

    private void calculateCurrentSeek() {
        if (maxProgress != 0) {
            if (!changeFromUser)
                currentSeek = progress * width / maxProgress;
            else {
                progress = currentSeek * maxProgress / width;
                if (callback != null)
                    callback.onSeekbarChange(progress);
            }
        }
        oldSeek = currentSeek;
    }

    public void setMaxProgress(int maxProgress) {

        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        if (progress <= maxProgress) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public void setCallback(SeekbarCallback callback) {
        this.callback = callback;
    }
}
