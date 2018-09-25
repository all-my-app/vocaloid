package leduyhung.me.vocaloid.db;

import android.content.Context;
import android.os.Bundle;

import com.leduyhung.loglibrary.Logg;

import java.util.Calendar;
import java.util.List;

import leduyhung.me.vocaloid.model.song.Song;

public class DatabaseManager {

    public static final String TAG_SONG_DATABASE = "TAG_SONG_DATABASE";

    private static DatabaseManager databaseManager;
    private Context mContext;

    public static DatabaseManager newInstance() {

        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public void create(Context mContext) {

        this.mContext = mContext;
    }

    public void save(String tag, Object data) {
        switch (tag) {
            case TAG_SONG_DATABASE:
                saveSong((Song) data);
                break;
        }
    }

    public boolean isExpired(String tag) {
        boolean result = false;
        switch (tag) {

            case TAG_SONG_DATABASE:
                result = isSongExpired();
                break;
        }
        return result;
    }

    private void saveSong(Song song) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);
        song.setSave_date(c.getTime());
        AppDatabase.newInstance(mContext).songDao().insertSongs(song);
    }

    private boolean isSongExpired() {
        Calendar c = Calendar.getInstance();
        List<Integer> lstId = AppDatabase.newInstance(mContext).songDao().getSongsByTime(c.getTime().getTime());
        if (lstId == null || lstId.size() > 0) {
            AppDatabase.newInstance(mContext).songDao().deleteAllSongs();
            Logg.info(getClass(), TAG_SONG_DATABASE + " song is expired. It will be delete");
            return true;
        }
        return false;
    }
}
