package leduyhung.me.vocaloid.model.user;

import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import leduyhung.me.vocaloid.rest.BaseApi;
import leduyhung.me.vocaloid.util.ClientUtil;
import leduyhung.me.vocaloid.util.GsonUtil;
import leduyhung.me.vocaloid.util.PreferenceUtil;
import retrofit2.Call;
import retrofit2.Response;

public class User implements FacebookCallback<LoginResult> {

    private final String KEY_CACHE_USER = "KEY_CACHE_USER";

    private static User instance;
    private transient Context mContext;
    private transient CallbackManager callbackManager;
    private transient Call<User> call;

    private String access_token;
    private UserInfo data;

    public static User newInstance() {

        if (instance == null)
            instance = new User();
        return instance;
    }

    public void create(Context mContext) {

        this.mContext = mContext;
    }

    User() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public boolean isUserLogin() {

        String data = PreferenceUtil.newInstance().build(mContext).getString(KEY_CACHE_USER, "");
        if (!data.equals("")) {
            this.data = GsonUtil.newInstance().fromJson(data, User.class).getUserData();
            return true;
        } else {
            return false;
        }
    }

    public UserInfo getUserData() {
        return data;
    }

    public void logout() {

        PreferenceUtil.newInstance().build(mContext).clearByName(KEY_CACHE_USER);
        LoginManager.getInstance().logOut();
    }

    public void setActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Progress login
    public void loginToServer() {

        call = BaseApi.request().login(ClientUtil.getVersionCode(mContext),
                ClientUtil.getPakageName(mContext), this);
        try {
            Response<User> response = call.execute();
            if (response.isSuccessful()) {

                this.data = response.body().getUserData();
            } else {

            }
        } catch (IOException e) {
            if (!ClientUtil.isConnectInternet(mContext)) {

            } else {

                if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {

                } else {

                }
            }
            logout();
        } finally {
            call = null;
        }

    }
}
