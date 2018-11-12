package leduyhung.me.vocaloid.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leduyhung.loglibrary.Logg;

import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.model.song.SongInfo;
import leduyhung.me.vocaloid.ui.main.MainActivity;
import leduyhung.me.vocaloid.util.ClientUtil;
import leduyhung.me.vocaloid.util.ImageUtil;

public class PlayerNotification implements RequestListener<Bitmap>, PlayerCallback {

    public static final int NOTIFICATION_ID = 1992;
    public static final String CHANNEL_ID = "1992";

    private static final String ACTION_PLAYER_NOTIFICATION_PREVIOUS = "ACTION_PLAYER_NOTIFICATION_PREVIOUS";
    private static final String ACTION_PLAYER_NOTIFICATION_PLAY_PAUSE = "ACTION_PLAYER_NOTIFICATION_PLAY_PAUSE";
    private static final String ACTION_PLAYER_NOTIFICATION_NEXT = "ACTION_PLAYER_NOTIFICATION_NEXT";
    private static final String ACTION_PLAYER_NOTIFICATION_MODE = "ACTION_PLAYER_NOTIFICATION_MODE";

    private static Context mContext;
    private static NotificationCompat.Builder mBuilder;
    private static NotificationManagerCompat notificationManagerCompat;
    private static RemoteViews remoteViews;

    private NotificationManager notificationManager;
    private PlayerService playerService;
    private int width, height;

    public PlayerNotification(Context context, PlayerService playerService) {
        mContext = context;
        this.playerService = playerService;
    }

    public Notification createNotification() {

        PlayerFactory.newInstance().setCallback(new PlayerCallbackModel(getClass(), this));
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        width = mContext.getResources().getDimensionPixelSize(R.dimen.media_notification_size_thumbnail);
        height = width;
        SongInfo songInfo = PlayerFactory.newInstance().getCurrentSong();
        String songName = "-";
        String singerName = "-";
        if (songInfo != null) {

            songName = songInfo.getName();
            singerName = songInfo.getSinger();
            ImageUtil.newInstance().loadBitmapFromNet(mContext, songInfo.getThumbnail(), width, height, this);
        }
        notificationManagerCompat = NotificationManagerCompat.from(mContext);
        remoteViews = new RemoteViews(ClientUtil.getPakageName(mContext), R.layout.layout_media_notification);
        remoteViews.setTextViewText(R.id.txt_song, songName);
        remoteViews.setTextViewText(R.id.txt_singer, singerName);
        if (PlayerFactory.newInstance().getMode() == PlayerMode.SEQUENCE) {
            remoteViews.setImageViewResource(R.id.img_play_mode, R.drawable.ic_sequence_white);
        } else if (PlayerFactory.newInstance().getMode() == PlayerMode.LOOP) {
            remoteViews.setImageViewResource(R.id.img_play_mode, R.drawable.ic_loop_white);
        } else {
            remoteViews.setImageViewResource(R.id.img_play_mode, R.drawable.ic_shuffle_white);
        }

        Intent openMain = new Intent(mContext, MainActivity.class);
        openMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntentOpenMain = PendingIntent.getActivity(mContext, 0,
                openMain, 0);
        createNotificationChannel();
        mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCustomBigContentView(remoteViews)
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntentOpenMain);
        initEventCLick();
        return mBuilder.build();
    }

    public void destroyNotification() {
        notificationManagerCompat.cancel(NOTIFICATION_ID);
        notificationManagerCompat = null;
        mBuilder = null;
        remoteViews = null;
        mContext = null;
        System.gc();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                // The user-visible name of the channel.
                CharSequence name = "MediaSession";
                // The user-visible description of the channel.
                String description = "MediaSession and MediaPlayer";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(
                        new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
        }
    }

    private void initEventCLick() {
        Intent eventIntent = new Intent(ACTION_PLAYER_NOTIFICATION_PREVIOUS);
        eventIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent clickIntent = PendingIntent.getBroadcast(mContext, 0, eventIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.relative_previous, clickIntent);

        eventIntent = new Intent(ACTION_PLAYER_NOTIFICATION_PLAY_PAUSE);
        eventIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntent = PendingIntent.getBroadcast(mContext, 0, eventIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.relative_play, clickIntent);

        eventIntent = new Intent(ACTION_PLAYER_NOTIFICATION_NEXT);
        eventIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntent = PendingIntent.getBroadcast(mContext, 0, eventIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.relative_next, clickIntent);

        eventIntent = new Intent(ACTION_PLAYER_NOTIFICATION_MODE);
        eventIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntent = PendingIntent.getBroadcast(mContext, 0, eventIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.relative_play_mode, clickIntent);
    }

    private void updateNextContent() {

        if (remoteViews != null && notificationManager != null) {
            SongInfo songInfo = PlayerFactory.newInstance().getCurrentSong();
            String songName = "-";
            String singerName = "-";
            int width = mContext.getResources().getDimensionPixelSize(R.dimen.media_notification_size_thumbnail);
            int height = width;
            if (songInfo != null) {

                songName = songInfo.getName();
                singerName = songInfo.getSinger();
            }
            remoteViews.setTextViewText(R.id.txt_song, songName);
            remoteViews.setTextViewText(R.id.txt_singer, singerName);
            remoteViews.setImageViewResource(R.id.img_play, R.drawable.ic_pause_white);
            notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
            ImageUtil.newInstance().loadBitmapFromNet(mContext, songInfo.getThumbnail(), width, height, this);
        }
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
        Logg.error(getClass(), " onLoadFailed ");
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
        if (notificationManagerCompat != null && remoteViews != null) {
            remoteViews.setImageViewBitmap(R.id.img_thumbnail, resource);
            notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
            Logg.error(getClass(), " onResourceReady");
        }
        return false;
    }

    @Override
    public void onPrepare(SongInfo songInfo, int index) {
        updateNextContent();
        playerService.startForeground(NOTIFICATION_ID, createNotification());
    }

    @Override
    public void onStart(SongInfo songInfo) {

    }

    @Override
    public void onPlaying(SongInfo songInfo, int currentTime) {

    }

    @Override
    public void onPlayingComplete() {

    }

    @Override
    public void onPause() {

        playerService.stopForeground(false);
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onError() {

    }

    public static class EventPlayerNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                String event = intent.getAction();
                if (event != null && !event.equals("")) {
                    switch (event) {
                        case ACTION_PLAYER_NOTIFICATION_PREVIOUS:
                            Logg.error(getClass(), "previous click");
                            PlayerFactory.newInstance().previous(mContext);
                            break;
                        case ACTION_PLAYER_NOTIFICATION_PLAY_PAUSE:
                            if (PlayerFactory.newInstance().getState() == PlayerState.PLAYING) {
                                Logg.error(getClass(), "Pause click");
                                PlayerFactory.newInstance().pause();
                                remoteViews.setImageViewResource(R.id.img_play, R.drawable.ic_play_white);
                                mBuilder.setAutoCancel(true);
                                notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
                            } else if (PlayerFactory.newInstance().getState() == PlayerState.PAUSE){
                                Logg.error(getClass(), "Play click");
                                PlayerFactory.newInstance().resume();
                                remoteViews.setImageViewResource(R.id.img_play, R.drawable.ic_pause_white);
                                mBuilder.setAutoCancel(false);
                                notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
                            }
                            break;
                        case ACTION_PLAYER_NOTIFICATION_NEXT:
                            Logg.error(getClass(), "next click");
                            PlayerFactory.newInstance().next(mContext, false);
                            break;
                        case ACTION_PLAYER_NOTIFICATION_MODE:
                            Logg.error(getClass(), "Play mode click");
                            if (PlayerFactory.newInstance().getMode() == PlayerMode.SEQUENCE) {
                                PlayerFactory.newInstance().setMode(PlayerMode.LOOP);
                                remoteViews.setImageViewResource(R.id.img_play_mode, R.drawable.ic_loop_white);
                                notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
                            } else if (PlayerFactory.newInstance().getMode() == PlayerMode.LOOP) {
                                PlayerFactory.newInstance().setMode(PlayerMode.SHUFFLE);
                                remoteViews.setImageViewResource(R.id.img_play_mode, R.drawable.ic_shuffle_white);
                                notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
                            } else {
                                PlayerFactory.newInstance().setMode(PlayerMode.SEQUENCE);
                                remoteViews.setImageViewResource(R.id.img_play_mode, R.drawable.ic_sequence_white);
                                notificationManagerCompat.notify(NOTIFICATION_ID, mBuilder.build());
                            }
                            break;
                    }
                }
            }
        }
    }
}
