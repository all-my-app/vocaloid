package leduyhung.me.vocaloid.model.song;

import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import leduyhung.me.vocaloid.model.Base;
import leduyhung.me.vocaloid.rest.BaseApi;
import leduyhung.me.vocaloid.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Song extends Base {

    private transient Context mContext;
    private transient OnGetDataFromServer onGetDataFromServer;
    private transient Call<Song> call;

    private ArrayList<SongInfo> data;

    public ArrayList<SongInfo> getData() {
        return data;
    }

    public void create(Context mContext) {

        this.mContext = mContext;
    }

    // TODO: Progress get list songs
    public void getListSong(String name, int page) {

        if (call == null || call.isCanceled()) {
            call = BaseApi.request().getSongs(ClientUtil.getVersionCode(mContext),
                    ClientUtil.getPakageName(mContext), name, page);
            call.enqueue(new Callback<Song>() {
                @Override
                public void onResponse(Call<Song> call, @NonNull Response<Song> response) {

                    if (response.isSuccessful()) {

                        data = response.body().getData();
                        setTotal_item(response.body().getTotal_item());
                        setCurrent_page(response.body().getCurrent_page());
                        setTotal_page(response.body().getCurrent_page());
                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onSuccess();
                    } else {
                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onFail(null);
                    }
                }

                @Override
                public void onFailure(Call<Song> call, Throwable t) {

                    if (!ClientUtil.isConnectInternet(mContext)) {

                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onFail(null);
                    } else {

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                            if (onGetDataFromServer != null)
                                onGetDataFromServer.onFail(null);
                        } else {

                            if (onGetDataFromServer != null)
                                onGetDataFromServer.onFail(null);
                        }
                    }
                }
            });
        }
    }

    // TODO: Progress get list song by singer
    public void getListSongOfSinger(int singerId, int page) {

        if (call == null || call.isCanceled()) {
            call = BaseApi.request().getSongsBySinger(ClientUtil.getVersionCode(mContext),
                    ClientUtil.getPakageName(mContext), singerId, page);
            call.enqueue(new Callback<Song>() {
                @Override
                public void onResponse(Call<Song> call, Response<Song> response) {

                    if (response.isSuccessful()) {

                        data = response.body().getData();
                        setTotal_item(response.body().getTotal_item());
                        setCurrent_page(response.body().getCurrent_page());
                        setTotal_page(response.body().getCurrent_page());
                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onSuccess();
                    } else {
                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onFail(null);
                    }
                }

                @Override
                public void onFailure(Call<Song> call, Throwable t) {

                    if (!ClientUtil.isConnectInternet(mContext)) {

                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onFail(null);
                    } else {

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                            if (onGetDataFromServer != null)
                                onGetDataFromServer.onFail(null);
                        } else {

                            if (onGetDataFromServer != null)
                                onGetDataFromServer.onFail(null);
                        }
                    }
                }
            });
        }
    }

    // TODO: Progress get list song favorite
    public void getSongsFavorite(int userId, int page) {
        if (call == null || call.isCanceled()) {
            call = BaseApi.request().getSongsFavorite(ClientUtil.getVersionCode(mContext),
                    ClientUtil.getPakageName(mContext), userId, page);
            call.enqueue(new Callback<Song>() {
                @Override
                public void onResponse(Call<Song> call, Response<Song> response) {

                    if (response.isSuccessful()) {

                        data = response.body().getData();
                        setTotal_item(response.body().getTotal_item());
                        setCurrent_page(response.body().getCurrent_page());
                        setTotal_page(response.body().getCurrent_page());
                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onSuccess();
                    } else {
                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onFail(null);
                    }
                }

                @Override
                public void onFailure(Call<Song> call, Throwable t) {

                    if (!ClientUtil.isConnectInternet(mContext)) {

                        if (onGetDataFromServer != null)
                            onGetDataFromServer.onFail(null);
                    } else {

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                            if (onGetDataFromServer != null)
                                onGetDataFromServer.onFail(null);
                        } else {

                            if (onGetDataFromServer != null)
                                onGetDataFromServer.onFail(null);
                        }
                    }
                }
            });
        }
    }

    // TODO: Progress update view of song
    public void updateViewSong() {
        if (call == null || call.isCanceled()) {
            call = BaseApi.request().updateViewSong(ClientUtil.getVersionCode(mContext),
                    ClientUtil.getPakageName(mContext));
            call.enqueue(null);
        }
    }

    public interface OnGetDataFromServer {

        void onSuccess();

        void onFail(String message);
    }

    public void setOnGetDataFromServer(OnGetDataFromServer onGetDataFromServer) {
        this.onGetDataFromServer = onGetDataFromServer;
    }
}