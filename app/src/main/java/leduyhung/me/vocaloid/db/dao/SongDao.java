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

    @Query("SELECT id FROM " + Constants.DB.TABLE_SONG + " WHERE save_date <= (:date)")
    List<Integer> getSongsByTime(long date);

    @Query("DELETE FROM " + Constants.DB.TABLE_SONG)
    void deleteAllSongs();
}
