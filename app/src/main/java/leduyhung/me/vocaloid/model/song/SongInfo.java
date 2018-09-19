package leduyhung.me.vocaloid.model.song;

import java.util.Locale;

public class SongInfo {

    private int id;
    private String name;
    private String link;
    private String thumbnail;
    private int duration;
    private int view;
    private String singer;

    public SongInfo() {
    }

    public SongInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getDuration() {
        return duration;
    }

    public String getStringDuration() {
        return convertSecondToString(duration);
    }

    public int getView() {
        return view;
    }

    public String getSinger() {
        return singer;
    }

    public String convertSecondToString(int totalSecs) {

        if (totalSecs > 0) {

            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        return "00:00";
    }
}
