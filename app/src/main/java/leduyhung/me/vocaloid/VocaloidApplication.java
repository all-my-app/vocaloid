package leduyhung.me.vocaloid;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.FacebookSdk;
import com.leduyhung.loglibrary.Logg;

public class VocaloidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Logg.init(getResources().getString(R.string.app_name), true);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
