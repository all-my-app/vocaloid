package leduyhung.me.vocaloid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PreferenceUtil {

    private static final String NAME_CACHE = "ANIME_CACHE";

    private static PreferenceUtil preferenceUtil;

    private Context mContext;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public static PreferenceUtil newInstance() {

        if (preferenceUtil == null)
            preferenceUtil = new PreferenceUtil();
        return preferenceUtil;
    }

    public PreferenceUtil build(Context ctx) {

        this.mContext = ctx;

        if (shared == null) {
            shared = mContext.getSharedPreferences(NAME_CACHE, Context.MODE_PRIVATE);
            editor = shared.edit();
        }
        return preferenceUtil;
    }

    public void putString(String key, String value) {

        editor.putString(key, value).commit();
    }

    public String getString(String key, String defValue) {

        return shared.getString(key, defValue);
    }

    public void putInt(String key, int value) {

        editor.putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {

        return shared.getInt(key, defValue);
    }

    public void putFloat(String key, float value) {

        editor.putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {

        return shared.getFloat(key, defValue);
    }

    public void putBoolean(String key, boolean value) {

        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {

        return shared.getBoolean(key, defValue);
    }

    public void clearByName(String key) {

        editor.remove(key).commit();
    }

    public void clearAll() {

        editor.clear().apply();
    }
}