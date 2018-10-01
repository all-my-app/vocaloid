package leduyhung.me.vocaloid.db;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.leduyhung.loglibrary.Logg;

import java.util.Calendar;
import java.util.List;

import leduyhung.me.vocaloid.model.singer.Singer;
import leduyhung.me.vocaloid.model.song.Song;

public class DatabaseManager {

    public static final String TAG_SONG_DATABASE = "TAG_SONG_DATABASE";
    public static final String TAG_SONG_FAVORITE_DATABASE = "TAG_SONG_FAVORITE_DATABASE";
    public static final String TAG_SONG_SINGER_DATABASE = "TAG_SONG_SINGER_DATABASE";
    public static final String TAG_SINGER_DATABASE = "TAG_SINGER_DATABASE";

    private static DatabaseManager databaseManager;
    private Context mContext;

    public static DatabaseManager newInstance() {

        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public DatabaseManager create(Context mContext) {

        this.mContext = mContext;
        return databaseManager;
    }

    public void save(String tag, Object data) {
        switch (tag) {
            case TAG_SONG_DATABASE:
            case TAG_SONG_FAVORITE_DATABASE:
            case TAG_SONG_SINGER_DATABASE:
                saveSong((Song) data);
                break;
            case TAG_SINGER_DATABASE:
                saveSinger((Singer) data);
                break;
        }
    }

    public boolean isExpired(String tag, int... params) {
        boolean result = false;
        switch (tag) {

            case TAG_SONG_DATABASE:
                result = isSongExpired();
                break;
            case TAG_SONG_FAVORITE_DATABASE:
                result = isSongFavoriteExpired();
                break;
            case TAG_SONG_SINGER_DATABASE:
                result = isSongSingerExpired(params[0]);
                break;
            case TAG_SINGER_DATABASE:
                result = isSingerExpired();
                break;
        }
        return result;
    }

    public void deleteAllSong() {
        if (mContext != null && !((Activity) mContext).isFinishing())
            AppDatabase.newInstance(mContext).songDao().deleteAllSongs();
    }

    public void deleteAllSinger() {
        if (mContext != null && !((Activity) mContext).isFinishing())
            AppDatabase.newInstance(mContext).singerDao().deleteAllSinger();
    }

    public Song getSongByPage(int page) {

        if (mContext != null && !((Activity) mContext).isFinishing())
            return AppDatabase.newInstance(mContext).songDao().getSongsByPage(page);
        return null;
    }

    public Song getSongFavoriteByPage(int page) {

        if (mContext != null && !((Activity) mContext).isFinishing())
            return AppDatabase.newInstance(mContext).songDao().getSongsFavoriteByPage(page);
        return null;
    }

    public Song getSongSingerByPage(int page, int singerId) {

        if (mContext != null && !((Activity) mContext).isFinishing())
            return AppDatabase.newInstance(mContext).songDao().getSongsSingerByPage(page, singerId);
        return null;
    }

    public Singer getSingersByPage(int page) {
        if (mContext != null && !((Activity) mContext).isFinishing())
            return AppDatabase.newInstance(mContext).singerDao().getSingersByPage(page);
        return null;
    }

    private void saveSong(Song song) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);
        song.setSave_date(c.getTime());
        if (mContext != null && !((Activity) mContext).isFinishing())
            AppDatabase.newInstance(mContext).songDao().insertSongs(song);
    }

    private void saveSinger(Singer singer) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 24);
        singer.setSave_date(c.getTime());
        if (mContext != null && !((Activity) mContext).isFinishing())
            AppDatabase.newInstance(mContext).singerDao().insertSinger(singer);
    }

    private boolean isSongExpired() {
        Calendar c = Calendar.getInstance();
        List<Integer> lstId = AppDatabase.newInstance(mContext).songDao().getSongsByTime(c.getTime().getTime());
        if (lstId == null || lstId.size() > 0 && mContext != null && !((Activity) mContext).isFinishing()) {
            AppDatabase.newInstance(mContext).songDao().deleteSongs();
            Logg.info(getClass(), TAG_SONG_DATABASE + " song is expired. It will be delete");
            return true;
        }
        return false;
    }

    private boolean isSingerExpired() {
        Calendar c = Calendar.getInstance();
        List<Integer> lstId = AppDatabase.newInstance(mContext).singerDao().getSingersByTime(c.getTime().getTime());
        if (lstId == null || lstId.size() > 0) {
            deleteAllSinger();
            Logg.info(getClass(), TAG_SINGER_DATABASE + " singer is expired. It will be delete");
            return true;
        }
        return false;
    }

    private boolean isSongFavoriteExpired() {
        Calendar c = Calendar.getInstance();
        List<Integer> lstId = AppDatabase.newInstance(mContext).songDao().getSongsFavoriteByTime(c.getTime().getTime());
        if (lstId == null || lstId.size() > 0 && mContext != null && !((Activity) mContext).isFinishing()) {
            AppDatabase.newInstance(mContext).songDao().deleteFavoriteSongs();
            Logg.info(getClass(), TAG_SONG_FAVORITE_DATABASE + " song is expired. It will be delete");
            return true;
        }
        return false;
    }

    private boolean isSongSingerExpired(int singerId) {
        Calendar c = Calendar.getInstance();
        List<Integer> lstId = AppDatabase.newInstance(mContext).songDao().getSongsSingerByTime(c.getTime().getTime(), singerId);
        if (lstId == null || lstId.size() > 0 && mContext != null && !((Activity) mContext).isFinishing()) {
            AppDatabase.newInstance(mContext).songDao().deleteSingerSongs(singerId);
            Logg.info(getClass(), TAG_SONG_SINGER_DATABASE + " song is expired. It will be delete");
            return true;
        }
        return false;
    }
}
