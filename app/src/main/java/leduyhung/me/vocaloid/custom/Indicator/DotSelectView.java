package leduyhung.me.vocaloid.custom.Indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class DotSelectView extends View implements ViewTreeObserver.OnPreDrawListener {

    private final int STROKE_WIDTH = 2;

    private int radius;
    private int color;
    private boolean needDraw, isFirstTime;

    private Paint paint;

    public DotSelectView(Context context) {
        super(context);
        init();
    }

    public DotSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            isFirstTime = true;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFirstTime) {
            getViewTreeObserver().addOnPreDrawListener(this);
            isFirstTime = false;
        }
        if (needDraw) {
            drawDot(canvas);
            needDraw = false;
        }
    }

    @Override
    public boolean onPreDraw() {
        if (!needDraw) {
            update();
        }
        getViewTreeObserver().removeOnPreDrawListener(this);
        return false;
    }

    private void init() {

        needDraw = false;
        isFirstTime = true;
    }

    private void drawDot(Canvas canvas) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.ADD);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius / 2 - STROKE_WIDTH, paint);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void update() {
        needDraw = true;
        postInvalidate();
    }
}
