package leduyhung.me.vocaloid.player;

import leduyhung.me.vocaloid.model.song.SongInfo;

public interface PlayerCallback {

    void onPrepare(SongInfo songInfo, int index);

    void onStart(SongInfo songInfo);

    void onPlaying(SongInfo songInfo, int currentTime);

    void onPlayingComplete();

    void onPause();

    void onStop();

    void onError();
}
