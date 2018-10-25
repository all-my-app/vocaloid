package leduyhung.me.vocaloid.player;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.leduyhung.loglibrary.Logg;

import java.util.ArrayList;
import java.util.List;

import leduyhung.me.vocaloid.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PlayerService extends MediaBrowserServiceCompat {

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playBackState;
    private PlayerFactory playerFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public BrowserRoot onGetRoot(String s, int i, Bundle bundle) {
        Logg.error(getClass(), "onGetRoot " + s);
        return new BrowserRoot(getString(R.string.app_name), null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        Logg.error(getClass(), "onLoadChildren " + parentId);
        result.sendResult(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        mediaSession = new MediaSessionCompat(this, getClass().getName());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);

        playBackState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO);
        mediaSession.setPlaybackState(playBackState.build());
        mediaSession.setCallback(new MediaSessionCallback());
        setSessionToken(mediaSession.getSessionToken());
        playerFactory = new PlayerFactory(this);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        private final List<MediaSessionCompat.QueueItem> mPlaylist = new ArrayList<>();
        private int index = -1;
        MediaMetadataCompat mPreparedMedia;

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            Logg.error(PlayerService.class, "onAddQueueItem");
            mPlaylist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            index = (index == -1 ? 0 : index);
            mediaSession.setQueue(mPlaylist);
            playerFactory.addListPlay(description.getMediaUri().toString());
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            Logg.error(PlayerService.class, "onRemoveQueueItem");
            mPlaylist.remove(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            index = (mPlaylist.isEmpty() ? -1 : index);
            mediaSession.setQueue(mPlaylist);
        }

        @Override
        public void onPrepare() {

            Logg.error(PlayerService.class, "onPrepare");
            if (mPlaylist.isEmpty())
                return;

            mediaSession.setMetadata(mPreparedMedia);

            if (!mediaSession.isActive())
                mediaSession.setActive(true);
        }

        @Override
        public void onPlay() {
            Logg.error(PlayerService.class, "onPlay");

            if (mPlaylist.isEmpty() || !mediaSession.isActive())
                return;

            // TODO: play muisc

            playerFactory.playSequence(0);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            playerFactory.play(uri);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }
    }
}