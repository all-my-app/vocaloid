package leduyhung.me.vocaloid.player;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.util.ClientUtil;

public class PlayerNotification {

    public static final int NOTIFICATION_ID = 1992;
    public static final String CHANNE_ID = "1992";

    private Context mContext;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews remoteViews;

    public PlayerNotification(Context mContext) {
        this.mContext = mContext;
    }

    public Notification createNotification() {

        remoteViews = new RemoteViews(ClientUtil.getPakageName(mContext), R.layout.layout_media_notification);

        mBuilder = new NotificationCompat.Builder(mContext, CHANNE_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCustomBigContentView(remoteViews);
        return mBuilder.build();
    }
}
