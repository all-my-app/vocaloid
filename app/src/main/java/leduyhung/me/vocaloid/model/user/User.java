package leduyhung.me.vocaloid.model.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

import leduyhung.me.vocaloid.rest.BaseApi;
import leduyhung.me.vocaloid.util.ClientUtil;
import leduyhung.me.vocaloid.util.GsonUtil;
import leduyhung.me.vocaloid.util.PreferenceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User implements FacebookCallback<LoginResult> {

    private final String KEY_CACHE_USER = "KEY_CACHE_USER";

    private static User instance;
    private transient Context mContext;
    private transient CallbackManager callbackManager;
    private transient Call<User> call;

    private String access_token;
    private UserInfo data;

    private transient OnUserCallback onUserCallback;

    public static User newInstance() {

        if (instance == null)
            instance = new User();
        return instance;
    }

    public User create(Context mContext) {

        this.mContext = mContext;
        return instance;
    }

    User() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {

//        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//
//
//            }
//        });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,gender, birthday");
//        request.setParameters(parameters);
//        request.executeAsync();
        setAccess_token(loginResult.getAccessToken().getToken());
        loginToServer();
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

    public void login() {

        if (mContext instanceof Activity) {
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("user_photos", "email",
                    "user_birthday", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, this);
        }
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
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    data = response.body().getUserData();
                    PreferenceUtil.newInstance().build(mContext).putString(KEY_CACHE_USER, GsonUtil.newInstance().toJson(response.body()));
                    if (onUserCallback != null)
                        onUserCallback.onLoginComplete();
                } else {
                    logout();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable e) {

                logout();
                if (!ClientUtil.isConnectInternet(mContext)) {

                } else {

                    if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {

                    } else {

                    }
                }
            }
        });
    }

    public interface OnUserCallback {
        void onLoginComplete();
    }

    public void registerCallback(OnUserCallback onUserCallback) {
        this.onUserCallback = onUserCallback;
    }

    public void unRegisterCallback() {
        this.onUserCallback = null;
    }
}
