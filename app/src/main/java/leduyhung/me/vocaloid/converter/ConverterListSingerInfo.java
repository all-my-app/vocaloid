package leduyhung.me.vocaloid.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import leduyhung.me.vocaloid.model.singer.SingerInfo;
import leduyhung.me.vocaloid.util.GsonUtil;

public class ConverterListSingerInfo {

    @TypeConverter
    public static ArrayList<SingerInfo> fromString(String value) {
        Type listType = new TypeToken<List<SingerInfo>>() {
        }.getType();
        return GsonUtil.newInstance().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(ArrayList<SingerInfo> list) {
        String json = GsonUtil.newInstance().toJson(list);
        return json;
    }
}
