package leduyhung.me.vocaloid.ui.main.home.song;

import java.util.ArrayList;

import leduyhung.me.vocaloid.model.song.SongInfo;

public class MessageForListSongFragment {

    public static final int CODE_LOAD_LIST_SONG_SUCCESS = 100;
    public static final int CODE_LOAD_MORE_LIST_SONG_SUCCESS = 101;
    public static final int CODE_SEARCH_LIST_SONG_SUCCESS = 102;
    public static final int CODE_LOAD_LIST_SONG_FAIL = 103;
    public static final int CODE_LOAD_MORE_SONG_FAIL = 104;
    public static final int CODE_SEARCH_LIST_SONG_FAIL = 105;
    public static final int CODE_SERVER_ERROR = 106;
    public static final int CODE_SONG_LIST_ADAPTER_CLICK = 107;

    private int code;
    private int index;
    private String message;
    private ArrayList<SongInfo> songInfos;

    public MessageForListSongFragment(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public MessageForListSongFragment(int code, ArrayList<SongInfo> songInfos, int index) {
        this.code = code;
        this.songInfos = songInfos;
        this.index = index;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<SongInfo> getSongInfos() {
        return songInfos;
    }

    public int getIndex() {
        return index;
    }
}