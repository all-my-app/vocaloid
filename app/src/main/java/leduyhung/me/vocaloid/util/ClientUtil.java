package leduyhung.me.vocaloid.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ClientUtil {

    public static int getVersionCode(Context mContext) {

        int versioncode = 0;
        try {
            versioncode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return versioncode;
        }
    }

    public static String getPakageName(Context mContext) {

        return mContext.getApplicationContext().getPackageName();
    }

    public static boolean isConnectInternet(Context mContext) {
        if (mContext == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
