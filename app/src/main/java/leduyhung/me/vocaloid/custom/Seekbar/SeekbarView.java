package leduyhung.me.vocaloid.custom.Seekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SeekbarView extends View {

    private Context mContext;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void getAttribute(Context mContext, AttributeSet attrs) {

        this.mContext = mContext;

        if (attrs != null) {

        }
    }
}
