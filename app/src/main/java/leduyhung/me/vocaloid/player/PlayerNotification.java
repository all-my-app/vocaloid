package leduyhung.me.vocaloid.player;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import leduyhung.me.vocaloid.R;

public class PlayerNotification {

    public static final int NOTIFICATION_ID = 1992;
    public static final String CHANNE_ID = "1992";

    private Context mContext;
    private NotificationCompat.Builder mBuilder;

    public PlayerNotification(Context mContext) {
        this.mContext = mContext;
    }

    public Notification createNotification() {
        mBuilder = new NotificationCompat.Builder(mContext, CHANNE_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Content")
                .setCategory(Notification.CATEGORY_PROGRESS)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return mBuilder.build();
    }
}
