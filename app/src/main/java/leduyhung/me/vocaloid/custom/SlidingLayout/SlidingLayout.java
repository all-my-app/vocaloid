package leduyhung.me.vocaloid.custom.SlidingLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class SlidingView extends ScrollView implements ViewTreeObserver.OnGlobalLayoutListener {

    private Context mContext;

    private FrameLayout frameParent, frameContent;

    private int width, height;

    public SlidingView(Context context) {
        super(context);
        getAtribute(context, null);
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAtribute(context, attrs);
    }

    public SlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAtribute(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.width = getWidth();
        this.height = getHeight();
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

    }

    private void getAtribute(Context mContext, AttributeSet attributeSet) {
        this.mContext = mContext;

        if (attributeSet != null) {

        }
    }
}