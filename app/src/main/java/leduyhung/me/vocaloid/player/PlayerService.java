package leduyhung.me.vocaloid.player;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.leduyhung.loglibrary.Logg;


public class PlayerService extends Service {

    public static final String KEY_INDEX_PLAYER = "KEY_INDEX_PLAYER";
    private PlayerNotification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int start = intent.getIntExtra(KEY_INDEX_PLAYER, 0);
            if (PlayerFactory.newInstance().getListMedia() != null && PlayerFactory.newInstance().getListMedia().size() > start) {
                PlayerFactory.newInstance().play(this, Uri.parse(PlayerFactory.newInstance().getListMedia().get(start).getLink()));
                startForeground(PlayerNotification.NOTIFICATION_ID, notification.createNotification());
            } else {
                stopSelf();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayerFactory.newInstance().stop(this, false);
        notification.destroyNotification();
        Logg.error(getClass(), "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void init() {

        notification = new PlayerNotification(this, this);

    }
}