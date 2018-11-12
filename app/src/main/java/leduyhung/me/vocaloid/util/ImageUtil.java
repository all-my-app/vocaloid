package leduyhung.me.vocaloid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leduyhung.loglibrary.Logg;

import java.util.concurrent.ExecutionException;

public class ImageUtil {

    private Glide glide;

    private static ImageUtil instance;

    public static ImageUtil newInstance() {

        if (instance == null)
            instance = new ImageUtil();
        return instance;
    }

    public ImageUtil() {
    }

    public void loadImageFromNet(Context mContext, ImageView target, String link) {

        glide.with(mContext).load(link).into(target);
    }

    public void loadBitmapFromNet(final Context mContext, final String link, final int width, final int height, final RequestListener<Bitmap> listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    glide.with(mContext).asBitmap().load(link).addListener(listener).submit(width, height).get();
                } catch (ExecutionException e) {
                    Logg.error(ImageUtil.class, "loadBitmapFromNet: " + e.toString());
                } catch (InterruptedException e) {
                    Logg.error(ImageUtil.class, "loadBitmapFromNet: " + e.toString());
                }
            }
        }).start();
    }

}
