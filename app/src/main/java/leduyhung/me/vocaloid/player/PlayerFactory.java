package leduyhung.me.vocaloid.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.leduyhung.loglibrary.Logg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import leduyhung.me.vocaloid.model.song.SongInfo;

public class PlayerFactory {

    private static PlayerFactory instance;

    private ArrayList<String> listLinkMedia;
    private int index;
    private boolean isPlaySequence;
    private PlayerState state;
    private PlayerMode mode;

    private Thread threadPlayer, threadLoadData;
    private MediaPlayer mediaPlayer;

    public static PlayerFactory newInstance() {

        if (instance == null)
            instance = new PlayerFactory();
        return instance;
    }

    private PlayerFactory() {
        listLinkMedia = new ArrayList<>();
        index = 0;
        isPlaySequence = false;
        state = PlayerState.STOP;
        mode = PlayerMode.SEQUENCE;
    }

    private int getRandomPosition() {
        if (listLinkMedia != null && listLinkMedia.size() > 0) {
            int totalSong = listLinkMedia.size();
            Random r = new Random();
            return r.nextInt(totalSong - 1);
        } else {
            return 0;
        }
    }

    public void addListPlay(final ArrayList<SongInfo> data) {
        if (threadLoadData != null) {
            threadLoadData.interrupt();
        }
        threadLoadData = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                for (SongInfo info : data) {
                    addListPlay(info.getLink());
                }
                if (state == PlayerState.STOP)
                    state = PlayerState.PREPARE;
            }
        });
        threadLoadData.start();
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

    public ArrayList<String> getListMedia() {
        return listLinkMedia;
    }

    public void playSequence(final Context mContext, final int start) {

        if (state == PlayerState.PREPARE || state == PlayerState.PLAYING || state == PlayerState.PAUSE || state == PlayerState.LOADING) {
            Intent intent = new Intent(mContext, PlayerService.class);
            intent.putExtra(PlayerService.KEY_INDEX_PLAYER, start);
            if (state == PlayerState.PLAYING)
                mContext.stopService(intent);
            mContext.startService(intent);
            index = start;
            isPlaySequence = true;
        } else {
            if (listLinkMedia.size() > 0 && start < listLinkMedia.size()) {
                state = PlayerState.PREPARE;
                playSequence(mContext, start);
            } else {
                Logg.error(getClass(), "playSequence: media not ready for play");
            }
        }
    }

    public void play(final Context mContext, final Uri uri) {

        if (threadPlayer != null)
            threadPlayer.interrupt();
        threadPlayer = new Thread(new Runnable() {
            @Override
            public void run() {

                if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                    mediaPlayer = new MediaPlayer();
                    state = PlayerState.LOADING;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
                        } else {
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        }
                        mediaPlayer.setDataSource(uri.toString());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                state = PlayerState.PLAYING;
                                Logg.error(PlayerFactory.class, "Prepared: Duration = " + mediaPlayer.getDuration());
                                mediaPlayer.start();
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                Logg.error(PlayerFactory.class, "Play complete");
                                if (!isPlaySequence)
                                    pause();
                                else {
                                    next(mContext, true);
                                }
                            }
                        });
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                Logg.error(PlayerFactory.class, "Error: " + i + " -- " + i1);
                                mContext.stopService(new Intent(mContext, PlayerService.class));
                                return false;
                            }
                        });
                    } catch (IOException e) {
                        Logg.error(getClass(), "play: " + e.toString());
                    }
                } else {
                    stop(false);
                    play(mContext, uri);
                }
            }
        });
        threadPlayer.start();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            state = PlayerState.PAUSE;
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            state = PlayerState.PLAYING;
        }
    }

    public void next(Context mContext, boolean isAutoNext) {

        stop(false);
        if (mode == PlayerMode.SHUFFLE)
            index = getRandomPosition();
        else if (mode == PlayerMode.SEQUENCE || !isAutoNext)
            index++;
        synchronized (listLinkMedia) {
            if (index < listLinkMedia.size())
                play(mContext, Uri.parse(listLinkMedia.get(index)));
            else {
                index = listLinkMedia.size() - 1;
                mContext.stopService(new Intent(mContext, PlayerService.class));
            }
        }
    }

    public void previous(Context mContext) {
        stop(false);
        if (mode == PlayerMode.SHUFFLE)
            index = getRandomPosition();
        else
            index--;
        synchronized (listLinkMedia) {
            if (index < listLinkMedia.size())
                play(mContext, Uri.parse(listLinkMedia.get(index)));
            else {
                index = listLinkMedia.size() - 1;
                mContext.stopService(new Intent(mContext, PlayerService.class));
            }
        }
    }

    public void stop(boolean destroy) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayer = null;
        if (destroy) {
            state = PlayerState.STOP;
        }
    }

    public void destroy(Context mContext) {
        mContext.stopService(new Intent(mContext, PlayerService.class));
        instance = null;

    }

    public PlayerState getState() {

        return state;
    }

    public PlayerMode getMode() {

        return mode;
    }

    public void setMode(PlayerMode mode) {
        this.mode = mode;
    }
}