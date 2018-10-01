package leduyhung.me.vocaloid.model.song;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.converter.ConverterDate;
import leduyhung.me.vocaloid.converter.ConverterListSongInfo;
import leduyhung.me.vocaloid.db.AppDatabase;
import leduyhung.me.vocaloid.db.DatabaseManager;
import leduyhung.me.vocaloid.model.Base;
import leduyhung.me.vocaloid.rest.BaseApi;
import leduyhung.me.vocaloid.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Entity(tableName = Constants.DB.TABLE_SONG)
public class Song extends Base {

    @Ignore
    private transient Context mContext;
    @Ignore
    private transient OnGetDataFromServer onGetDataFromServer;
    @Ignore
    private transient Call<Song> call;

    private transient int singerId;
    private transient int favorite;

    @TypeConverters(ConverterListSongInfo.class)
    private ArrayList<SongInfo> data;
    @TypeConverters(ConverterDate.class)
    private Date save_date;

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public ArrayList<SongInfo> getData() {
        return data;
    }

    public void create(Context mContext) {

        this.mContext = mContext;
    }

    public void setData(ArrayList<SongInfo> data) {
        this.data = data;
    }

    public Date getSave_date() {
        return save_date;
    }

    public void setSave_date(Date save_date) {
        this.save_date = save_date;
    }

    // TODO: Progress get list songs
    public void getListSong(final String name, int page) {

        Song s = DatabaseManager.newInstance().create(mContext).getSongByPage(page);
        if (!DatabaseManager.newInstance().create(mContext).isExpired(DatabaseManager.TAG_SONG_DATABASE) && s != null &&
                s.getData().size() > 0 && name == null) {
            data = s.getData();
        } else {

            if (call == null || call.isCanceled()) {
                call = BaseApi.request().getSongs(ClientUtil.getVersionCode(mContext),
                        ClientUtil.getPakageName(mContext), name, page);
                call.enqueue(new Callback<Song>() {
                    @Override
                    public void onResponse(Call<Song> call, @NonNull Response<Song> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getData() != null && response.body().getData().size() > 0) {
                                if (name == null)
                                    DatabaseManager.newInstance().create(mContext).save(DatabaseManager.TAG_SONG_DATABASE, response.body());
                                data = response.body().getData();
                                setTotal_item(response.body().getTotal_item());
                                setCurrent_page(response.body().getCurrent_page());
                                setTotal_page(response.body().getCurrent_page());
                            }
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
    }

    // TODO: Progress get list song by singer
    public void getListSongOfSinger(int page, int singerId) {

        Song s = DatabaseManager.newInstance().create(mContext).getSongSingerByPage(page, singerId);
        if (!DatabaseManager.newInstance().create(mContext).isExpired(DatabaseManager.TAG_SONG_SINGER_DATABASE, singerId) &&
                s != null && s.getData().size() > 0) {
            data = s.getData();
        } else {
            if (call == null || call.isCanceled()) {
                call = BaseApi.request().getSongsBySinger(ClientUtil.getVersionCode(mContext),
                        ClientUtil.getPakageName(mContext), singerId, page);
                call.enqueue(new Callback<Song>() {
                    @Override
                    public void onResponse(Call<Song> call, Response<Song> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getData().size() > 0) {
                                data = response.body().getData();
                                setTotal_item(response.body().getTotal_item());
                                setCurrent_page(response.body().getCurrent_page());
                                setTotal_page(response.body().getCurrent_page());
                                DatabaseManager.newInstance().create(mContext).save(DatabaseManager.TAG_SONG_SINGER_DATABASE, response.body());
                            }
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
    }

    // TODO: Progress get list song favorite
    public void getSongsFavorite(int userId, int page) {

        Song s = DatabaseManager.newInstance().create(mContext).getSongFavoriteByPage(page);
        if (!DatabaseManager.newInstance().create(mContext).isExpired(DatabaseManager.TAG_SONG_FAVORITE_DATABASE) &&
                s != null && s.getData().size() > 0) {
            data = s.getData();
        } else {
            if (call == null || call.isCanceled()) {
                call = BaseApi.request().getSongsFavorite(ClientUtil.getVersionCode(mContext),
                        ClientUtil.getPakageName(mContext), userId, page);
                call.enqueue(new Callback<Song>() {
                    @Override
                    public void onResponse(Call<Song> call, Response<Song> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getData().size() > 0) {
                                data = response.body().getData();
                                setTotal_item(response.body().getTotal_item());
                                setCurrent_page(response.body().getCurrent_page());
                                setTotal_page(response.body().getCurrent_page());
                                DatabaseManager.newInstance().create(mContext)
                                        .save(DatabaseManager.TAG_SONG_FAVORITE_DATABASE, response.body());
                            }
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
    }

    // TODO: Progress update view of song
    public void updateViewSong() {

        BaseApi.request().updateViewSong(ClientUtil.getVersionCode(mContext),
                ClientUtil.getPakageName(mContext)).enqueue(null);
    }

    // TODO: Progress add or delete favorite song
    public void addOrDeleteSongFavorite(int userId, Base base) {

        BaseApi.request().addDeleteSongFavorite(ClientUtil.getVersionCode(mContext),
                ClientUtil.getPakageName(mContext), userId, base).enqueue(null);
    }

    public interface OnGetDataFromServer {

        void onSuccess();

        void onFail(String message);
    }

    public void setOnGetDataFromServer(OnGetDataFromServer onGetDataFromServer) {
        this.onGetDataFromServer = onGetDataFromServer;
    }
}