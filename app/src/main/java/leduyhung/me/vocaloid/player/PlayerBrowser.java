package leduyhung.me.vocaloid.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.leduyhung.loglibrary.Logg;

import java.util.ArrayList;

import leduyhung.me.vocaloid.model.song.SongInfo;


public class PlayerBrowser extends MediaBrowserCompat.ConnectionCallback {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_PLAY_REPEAT_ONE_SONG = 1;
    public static final int TYPE_SHUFFLE = 3;

    private Context mContext;
    private MediaBrowserCompat mediaBrowser;
    private MediaControllerCompat mediaController;

    private ArrayList<SongInfo> waitData;

    public PlayerBrowser(Context mContext) {
        this.mContext = mContext;
        mediaBrowser = new MediaBrowserCompat(mContext,
                new ComponentName(mContext, PlayerService.class),
                this,
                null);
    }

    @Override
    public void onConnected() {
        Logg.error(PlayerBrowser.class, "onConnected");
        MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
        try {
            mediaController = new MediaControllerCompat(mContext, token);
            if (waitData != null) {
                prepareSong(waitData);
            }
        } catch (RemoteException e) {
            Logg.error(PlayerBrowser.class, "Create a MediaControllerCompat faild: " + e.toString());
        }
        MediaControllerCompat.setMediaController((Activity) mContext, mediaController);
        PlaybackStateCompat playbackState = mediaController.getPlaybackState();
        if (playbackState != null)
            Logg.error(PlayerBrowser.class, "Playback state = " + playbackState.getState());
    }

    @Override
    public void onConnectionSuspended() {
        Logg.error(PlayerBrowser.class, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed() {
        Logg.error(PlayerBrowser.class, "onConnectionFailed");
    }

    private void addSong(ArrayList<SongInfo> songInfos) {
        MediaMetadataCompat mediaMetadataCompat;
        for (SongInfo s : songInfos) {
            mediaMetadataCompat = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, s.getLink()).build();
            mediaController.addQueueItem(mediaMetadataCompat.getDescription());
        }
        waitData = null;
    }

    public void connect() {
        if (!mediaBrowser.isConnected())
            mediaBrowser.connect();
        ((Activity) mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void disconnect() {
        if (mediaBrowser.isConnected())
            mediaBrowser.disconnect();
    }

    public void prepareSong(ArrayList<SongInfo> songInfos) {

        if (mediaController != null) {
            if (songInfos != null && songInfos.size() > 0) {

                addSong(songInfos);
                mediaController.getTransportControls().prepare();
            }
        } else {
            if (songInfos != null && songInfos.size() > 0) {

                waitData = songInfos;
            }
        }
    }

    public void playSequence(int start) {
        Logg.error(getClass(), "playSequence index start = " + start);
        Bundle bundle = new Bundle();
        bundle.putInt(PlayerService.KEY_INDEX_PLAYER, start);
        mediaController.getTransportControls().playFromUri(Uri.parse("moa moa"), bundle);
    }

    public void play(String link) {
        if (link != null && link.length() > 0)
            mediaController.getTransportControls().playFromUri(Uri.parse(link), null);
    }

    public void pause() {
        mediaController.getTransportControls().pause();
    }

    public void stop() {
        mediaController.getTransportControls().stop();
    }

    public void seekTo(long time) {
        mediaController.getTransportControls().seekTo(time);
    }

    public void next() {
        mediaController.getTransportControls().skipToNext();
    }

    public void previous() {
        mediaController.getTransportControls().skipToPrevious();
    }

    public void setTypePlay(int type) {
        switch (type) {
            case TYPE_PLAY_REPEAT_ONE_SONG:
                mediaController.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
                break;
            case TYPE_SHUFFLE:
                mediaController.getTransportControls().setRepeatMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
                break;
            default:
                mediaController.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
        }
    }
}
