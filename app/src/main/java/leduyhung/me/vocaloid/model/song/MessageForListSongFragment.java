package leduyhung.me.vocaloid.model.song;

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
    private String message;

    public MessageForListSongFragment(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}