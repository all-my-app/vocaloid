package leduyhung.me.vocaloid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.model.song.Song;

@Dao
public interface SongDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSongs(Song... songs);

    @Query("SELECT * FROM " + Constants.DB.TABLE_SONG + " WHERE current_page IN (:page)")
    Song getSongsByPage(int page);

    @Query("SELECT * FROM " + Constants.DB.TABLE_SONG + " WHERE current_page IN (:page) AND favorite == 1")
    Song getSongsFavoriteByPage(int page);

    @Query("SELECT * FROM " + Constants.DB.TABLE_SONG + " WHERE current_page IN (:page) AND singerId IN (:singerId)")
    Song getSongsSingerByPage(int page, int singerId);

    @Query("SELECT id FROM " + Constants.DB.TABLE_SONG + " WHERE save_date <= (:date)")
    List<Integer> getSongsByTime(long date);

    @Query("SELECT id FROM " + Constants.DB.TABLE_SONG + " WHERE save_date <= (:date) AND favorite == 1")
    List<Integer> getSongsFavoriteByTime(long date);

    @Query("SELECT id FROM " + Constants.DB.TABLE_SONG + " WHERE save_date <= (:date) AND singerId == (:singerId)")
    List<Integer> getSongsSingerByTime(long date, int singerId);

    @Query("DELETE FROM " + Constants.DB.TABLE_SONG + " WHERE favorite == 1")
    void deleteFavoriteSongs();

    @Query("DELETE FROM " + Constants.DB.TABLE_SONG + " WHERE singerId IN (:singerId)")
    void deleteSingerSongs(int singerId);

    @Query("DELETE FROM " + Constants.DB.TABLE_SONG + " WHERE singerId == 0 AND favorite == 0")
    void deleteSongs();

    @Query("DELETE FROM " + Constants.DB.TABLE_SONG)
    void deleteAllSongs();
}
