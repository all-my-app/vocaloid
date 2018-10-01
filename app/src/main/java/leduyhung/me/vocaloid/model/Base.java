package leduyhung.me.vocaloid.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.databinding.BaseObservable;

import java.util.Date;

import leduyhung.me.vocaloid.converter.ConverterDate;

public class Base extends BaseObservable {

    @ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int total_item;
    private int total_page;
    private int current_page;
    @TypeConverters(ConverterDate.class)
    private transient Date save_date;

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

    public Date getSave_date() {
        return save_date;
    }

    public void setSave_date(Date save_date) {
        this.save_date = save_date;
    }
}
