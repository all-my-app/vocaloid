package leduyhung.me.vocaloid;

public final class Constants {

    public final class Server {

        public static final String SERVER_ADDRESS = "http://animeworld.ddns.net:8888";
    }

    public final class DB {
        public static final String DB_NAME = "VOCALOID_DB";
        public static final int DB_VERSION = 1;
        public static final String TABLE_SONG = "SONG_TABLE";
    }

    public final class Recycler {

        public static final int RECYCLER_ITEM_NO_DATA = 0;
        public static final int RECYCLER_ITEM_HAS_DATA = 1;
        public static final int RECYCLER_ITEM_LOAD_MORE = 2;
    }
}
