package leduyhung.me.vocaloid.util;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
}
