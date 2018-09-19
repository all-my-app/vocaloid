package leduyhung.me.vocaloid.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

    private static Gson gson;

    public static Gson newInstance() {

        if (gson == null)
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }
}
