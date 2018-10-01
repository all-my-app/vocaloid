package leduyhung.me.vocaloid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.model.singer.Singer;

@Dao
public interface SingerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSinger(Singer... singers);

    @Query("SELECT * FROM " + Constants.DB.TABLE_SINGER + " WHERE current_page IN (:page)")
    Singer getSingersByPage(int page);

    @Query("SELECT id FROM " + Constants.DB.TABLE_SINGER + " WHERE save_date <= (:date)")
    List<Integer> getSingersByTime(long date);

    @Query("DELETE FROM " + Constants.DB.TABLE_SINGER)
    void deleteAllSinger();
}
