package leduyhung.me.vocaloid.model.singer;

public class SingerInfo {

    private int id;
    private String name;
    private String avatar;
    private int total_song;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTotal_song() {
        return total_song;
    }

    public void setTotal_song(int total_song) {
        this.total_song = total_song;
    }
}
