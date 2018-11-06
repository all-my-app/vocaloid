package leduyhung.me.vocaloid.player;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.leduyhung.loglibrary.Logg;


public class PlayerService extends Service {

    public static final String KEY_INDEX_PLAYER = "KEY_INDEX_PLAYER";

    private static PlayerService instance;
    private PlayerFactory playerFactory;
    private Thread threadPlayer;

    public static PlayerService newInstance() {

        if (instance == null)
            instance = new PlayerService();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int start = intent.getIntExtra(KEY_INDEX_PLAYER, 0);
            PlayerFactory.newInstance().play(this, Uri.parse(PlayerFactory.newInstance().getListMedia().get(start)));
            startForeground(1, new Notification());
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayerFactory.newInstance().stop(false);
        Logg.error(getClass(), "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void init() {

    }


}