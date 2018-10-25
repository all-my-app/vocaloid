package leduyhung.me.vocaloid.ui.main;

import java.util.ArrayList;

import leduyhung.me.vocaloid.model.song.SongInfo;

public class MessageForMainActivity {

    public static final int CODE_PLAY_MUSIC = 200;

    private int code;
    private ArrayList<SongInfo> songInfos;
    private int index;

    public MessageForMainActivity(int code, ArrayList<SongInfo> songInfos, int index) {
        this.code = code;
        this.songInfos = songInfos;
        this.index = index;
    }

    public int getCode() {
        return code;
    }

    public ArrayList<SongInfo> getSongInfo() {
        return songInfos;
    }
}
