package leduyhung.me.vocaloid.custom.Seekbar;

import android.content.Context;
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
    private int width, height, currentSeek, oldSeek;
    private int colorSeek, colorBackground;
    private int maxProgress, progress;
    private boolean changeFromUser;

    private Paint paintSeek, paintBackground;

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
        this.height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawSeek(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            changeFromUser = true;
            this.currentSeek = (int) event.getX();
            if (currentSeek != oldSeek) {
                postInvalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            changeFromUser = false;
        }
        return true;
    }

    private void getAttribute(Context mContext, AttributeSet attrs) {

        this.mContext = mContext;

        if (attrs != null) {

        }
        progress = 0;
        maxProgress = 100;

        colorSeek = mContext.getResources().getColor(R.color.colorAccent);
        colorBackground = mContext.getResources().getColor(R.color.colorGray);

        paintSeek = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSeek.setColor(colorSeek);

        paintBackground = new Paint();
        paintBackground.setColor(colorBackground);
        changeFromUser = false;
    }

    private void drawBackground(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.ADD);
        canvas.drawRect(0, 0, width, height, paintBackground);
    }

    private void drawSeek(Canvas canvas) {

        calculateCurrentSeek();
        canvas.drawRect(0, 0, currentSeek, height, paintSeek);
    }

    private void calculateCurrentSeek() {
        if (maxProgress != 0 && !changeFromUser) {
            currentSeek = progress * width / maxProgress;
        }
        oldSeek = currentSeek;
    }

    public void setProgress(int progress) {
        if (progress <= maxProgress) {
            this.progress = progress;
            postInvalidate();
        }
    }
}