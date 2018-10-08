package leduyhung.me.vocaloid;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.leduyhung.loglibrary.Logg;

public class VocaloidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logg.init(getResources().getString(R.string.app_name), true);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
