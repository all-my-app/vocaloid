package leduyhung.me.vocaloid.player;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.leduyhung.loglibrary.Logg;

import java.util.ArrayList;

import leduyhung.me.vocaloid.model.song.SongInfo;


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
        PlayerFactory.newInstance().playSequence(this, 0);
        startForeground(1, new Notification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void init() {

    }


}