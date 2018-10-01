package leduyhung.me.vocaloid.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.db.dao.SingerDao;
import leduyhung.me.vocaloid.db.dao.SongDao;
import leduyhung.me.vocaloid.model.song.Song;

@Database(entities = Song.class, version = Constants.DB.DB_VERSION,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase appDatabase;

    public abstract SongDao songDao();
    public abstract SingerDao singerDao();

    public static AppDatabase newInstance(Context ctx) {

        if (appDatabase == null) {

            appDatabase = Room.databaseBuilder(ctx, AppDatabase.class, Constants.DB.DB_NAME)
                    .allowMainThreadQueries().addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            }).build();
        }

        return appDatabase;
    }

    public static void destroyAppDatabase() {

        appDatabase = null;
    }
}
