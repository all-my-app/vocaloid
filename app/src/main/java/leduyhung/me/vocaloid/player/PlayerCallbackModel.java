package leduyhung.me.vocaloid.player;

public class PlayerCallbackModel {

    private Class tag;
    private PlayerCallback callback;

    public PlayerCallbackModel(Class tag, PlayerCallback callback) {
        this.tag = tag;
        this.callback = callback;
    }

    public Class getTag() {
        return tag;
    }

    public void setTag(Class tag) {
        this.tag = tag;
    }

    public PlayerCallback getCallback() {
        return callback;
    }

    public void setCallback(PlayerCallback callback) {
        this.callback = callback;
    }
}
