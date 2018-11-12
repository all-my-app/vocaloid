package leduyhung.me.vocaloid.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;

import com.leduyhung.loglibrary.Logg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import leduyhung.me.vocaloid.model.song.SongInfo;

public class PlayerFactory {

    private static final int COUNT_TIME_SPEED = 500;

    private static PlayerFactory instance;

    private ArrayList<SongInfo> listLinkMedia;
    private int index;
    private PlayerState state;
    private PlayerMode mode;

    private Thread threadPlayer, threadLoadData;
    private Timer countTimePlayer;
    private MediaPlayer mediaPlayer;

    private PlayerCallback callback;

    public static PlayerFactory newInstance() {

        if (instance == null)
            instance = new PlayerFactory();
        return instance;
    }

    private PlayerFactory() {
        listLinkMedia = new ArrayList<>();
        index = 0;
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

    private void startCountTimePlayer() {
        if (countTimePlayer == null && state == PlayerState.PLAYING) {

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        if (callback != null)
                            callback.onPlaying(listLinkMedia.get(index), mediaPlayer.getCurrentPosition());
                    }
                }
            };
            countTimePlayer = new Timer();
            countTimePlayer.schedule(task, 0, COUNT_TIME_SPEED);
        }
    }

    private void stopCountTimePlayer() {
        if (countTimePlayer != null) {
            countTimePlayer.cancel();
        }
        countTimePlayer = null;
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
                    addListPlay(info);
                }
                if (state == PlayerState.STOP)
                    state = PlayerState.PREPARE;
            }
        });
        threadLoadData.start();
    }

    public void addListPlay(SongInfo song) {
        if (song != null)
            listLinkMedia.add(song);
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

    public ArrayList<SongInfo> getListMedia() {
        return listLinkMedia;
    }

    public void playSequence(final Context mContext, final int start) {

        if (state == PlayerState.PREPARE || state == PlayerState.PLAYING || state == PlayerState.PAUSE || state == PlayerState.LOADING) {
            Intent intent = new Intent(mContext, PlayerService.class);
            intent.putExtra(PlayerService.KEY_INDEX_PLAYER, start);
            if (state == PlayerState.PLAYING || state == PlayerState.PAUSE || state == PlayerState.LOADING)
                mContext.stopService(intent);
            mContext.startService(intent);
            index = start;
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
                    if (callback != null)
                        callback.onPrepare(listLinkMedia.get(index), index);
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
                                startCountTimePlayer();
                                if (callback != null)
                                    callback.onStart(listLinkMedia.get(index));
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                Logg.error(PlayerFactory.class, "Play complete");
                                if (callback != null)
                                    callback.onPlayingComplete();
                                next(mContext, true);
                            }
                        });
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                Logg.error(PlayerFactory.class, "Error: " + i + " -- " + i1);
                                if (callback != null)
                                    callback.onError();
                                mContext.stopService(new Intent(mContext, PlayerService.class));
                                return false;
                            }
                        });
                    } catch (IOException e) {
                        Logg.error(getClass(), "play: " + e.toString());
                        if (callback != null)
                            callback.onError();
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
        stopCountTimePlayer();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            state = PlayerState.PAUSE;
            if (callback != null)
                callback.onPause();
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            state = PlayerState.PLAYING;
            startCountTimePlayer();
        }
    }

    public void seekTo(int milliseconds) {
        if (mediaPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer.seekTo(milliseconds, MediaPlayer.SEEK_CLOSEST);
            } else {
                mediaPlayer.seekTo(milliseconds);
            }
        }
    }

    public void next(Context mContext, boolean isAutoNext) {

        stop(false);
        if (mode == PlayerMode.SHUFFLE)
            index = getRandomPosition();
        else if (mode == PlayerMode.SEQUENCE || !isAutoNext)
            index++;
        synchronized (listLinkMedia) {
            if (index >= listLinkMedia.size())
                index = listLinkMedia.size() - 1;
            playSequence(mContext, index);
        }
    }

    public void previous(Context mContext) {

        stop(false);
        if (mode == PlayerMode.SHUFFLE)
            index = getRandomPosition();
        else
            index--;
        synchronized (listLinkMedia) {
            if (index >= listLinkMedia.size())
                index = listLinkMedia.size() - 1;
            playSequence(mContext, index);
        }
    }

    public void stop(boolean destroy) {

        stopCountTimePlayer();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            if (callback != null)
                callback.onStop();
        }
        mediaPlayer = null;
        if (destroy) {
            state = PlayerState.STOP;
        }
    }

    public SongInfo getCurrentSong() {
        if (listLinkMedia != null && listLinkMedia.size() > index)
            return listLinkMedia.get(index);
        else
            return null;
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

    public void setCallback(PlayerCallback callback) {

        this.callback = callback;
    }
}