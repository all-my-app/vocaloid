package leduyhung.me.vocaloid.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;

public class Base extends BaseObservable {

    @ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int total_item;
    private int total_page;
    private int current_page;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_item() {
        return total_item;
    }

    public int getTotal_page() {
        return total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setTotal_item(int total_item) {
        this.total_item = total_item;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }
}
