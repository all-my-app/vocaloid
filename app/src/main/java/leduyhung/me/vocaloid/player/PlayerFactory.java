package leduyhung.me.vocaloid.player;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import com.leduyhung.loglibrary.Logg;

import java.io.IOException;
import java.util.ArrayList;

import leduyhung.me.vocaloid.model.song.SongInfo;

public class PlayerFactory {

    private static PlayerFactory instance;

    private ArrayList<String> listLinkMedia;
    private int index;
    private boolean isPlaySequence;

    private Thread threadPlayer;
    private MediaPlayer mediaPlayer;

    public static PlayerFactory newInstance() {

        if (instance == null)
            instance = new PlayerFactory();
        return instance;
    }

    public PlayerFactory() {
        listLinkMedia = new ArrayList<>();
        index = 0;
        isPlaySequence = false;
    }

    public void addListPlay(final ArrayList<SongInfo> data) {
        if (threadPlayer != null) {
            threadPlayer.interrupt();
        }
        threadPlayer = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                for (SongInfo info: data) {
                    addListPlay(info.getLink());
                }
            }
        });
        threadPlayer.start();
    }

    public void addListPlay(String link) {
        if (link != null && link.length() > 0)
            listLinkMedia.add(link);
        else
            Logg.error(getClass(), "addListPlay: link is null");
    }

    public void removeItemFromListPlay(String link) {
        if (link != null && link.length() > 0) {
            listLinkMedia.remove(link);
        }
    }

    public void removeItemFromListPlay(int index) {
        listLinkMedia.remove(index);
    }

    public void clearListPlay() {
        listLinkMedia.clear();
    }

    public void playSequence(Context mContext, int start) {

        index = start;
        isPlaySequence = true;
        play(mContext, Uri.parse(listLinkMedia.get(index)));
    }

    public void play(final Context mContext, Uri uri) {

        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            mediaPlayer = new MediaPlayer();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                } else {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                mediaPlayer.setDataSource(mContext, uri);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Logg.error(PlayerFactory.class, "Prepared: Duration = " + mediaPlayer.getDuration());
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        Logg.error(PlayerFactory.class, "Buffering with percent = " + i);
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Logg.error(PlayerFactory.class, "Play complete");
                        if (!isPlaySequence)
                            pause();
                        else {
                            stop();
                            index++;
                            if (index < listLinkMedia.size())
                                play(mContext, Uri.parse(listLinkMedia.get(index)));
                            else
                                index = listLinkMedia.size() - 1;
                        }
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        Logg.error(PlayerFactory.class, "Error: " + i + " -- " + i1);
                        return false;
                    }
                });
            } catch (IOException e) {
                Logg.error(getClass(), "play: " + e.toString());
            }
        } else {
            stop();
            play(mContext, uri);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stop() {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }
}