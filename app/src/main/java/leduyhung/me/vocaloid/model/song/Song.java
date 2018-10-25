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
import leduyhung.me.vocaloid.R;
import leduyhung.me.vocaloid.converter.ConverterDate;
import leduyhung.me.vocaloid.converter.ConverterListSongInfo;
import leduyhung.me.vocaloid.db.DatabaseManager;
import leduyhung.me.vocaloid.model.Base;
import leduyhung.me.vocaloid.rest.BaseApi;
import leduyhung.me.vocaloid.ui.main.home.song.MessageForListSongFragment;
import leduyhung.me.vocaloid.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Entity(tableName = Constants.DB.TABLE_SONG)
public class Song extends Base {

    @Ignore
    private transient Context mContext;
    @Ignore
    private transient Call<Song> call;

    @ColumnInfo(index = true)
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int singerId;
    private int favorite;
    @TypeConverters(ConverterDate.class)
    private Date save_date;

    @TypeConverters(ConverterListSongInfo.class)
    private ArrayList<SongInfo> data;

    public Song() {
    }

    public Song(Context mContext) {
        this.mContext = mContext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSave_date() {
        return save_date;
    }

    public void setSave_date(Date save_date) {
        this.save_date = save_date;
    }

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

    public void setData(ArrayList<SongInfo> data) {
        this.data = data;
    }

    // TODO: Progress get list songs
    public void getListSong(final String name, final int page) {

        final Song s = DatabaseManager.newInstance().create(mContext).getSongByPage(page);
        if (!DatabaseManager.newInstance().create(mContext).isExpired(DatabaseManager.TAG_SONG_DATABASE) && s != null &&
                s.getData().size() > 0 && name == null) {
            data = s.getData();
            setTotal_item(s.getTotal_item());
            setCurrent_page(s.getCurrent_page());
            setTotal_page(s.getTotal_page());
            EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_LOAD_LIST_SONG_SUCCESS, ""));
        } else {

            if (call == null || call.isCanceled()) {
                call = BaseApi.request().getSongs(ClientUtil.getVersionCode(mContext),
                        ClientUtil.getPakageName(mContext), name, page);
                call.enqueue(new Callback<Song>() {
                    @Override
                    public void onResponse(Call<Song> call, @NonNull Response<Song> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (name == null)
                                DatabaseManager.newInstance().create(mContext).save(DatabaseManager.TAG_SONG_DATABASE, response.body());

                            data = response.body().getData();
                            setTotal_item(response.body().getTotal_item());
                            setCurrent_page(response.body().getCurrent_page());
                            setTotal_page(response.body().getCurrent_page());
                            EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_LOAD_LIST_SONG_SUCCESS, ""));

                        } else {

                            if (name != null)
                                EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_SEARCH_LIST_SONG_FAIL,
                                        mContext.getResources().getString(R.string.message_no_data_found)));

                            else {

                                if (page == 1) {
                                    if (response.code() == 403)
                                        EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_SERVER_ERROR,
                                                mContext.getResources().getString(R.string.message_need_update_app)));
                                    else
                                        EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_LOAD_LIST_SONG_FAIL,
                                                mContext.getResources().getString(R.string.message_no_data_found)));

                                } else {
                                    EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_LOAD_MORE_SONG_FAIL,
                                            mContext.getResources().getString(R.string.message_no_data_found)));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Song> call, Throwable t) {

                        if (!ClientUtil.isConnectInternet(mContext)) {

                            EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_SERVER_ERROR,
                                    mContext.getResources().getString(R.string.message_no_internet)));
                        } else {

                            if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_SERVER_ERROR,
                                        mContext.getResources().getString(R.string.message_timeout_request)));
                            } else {

                                EventBus.getDefault().post(new MessageForListSongFragment(MessageForListSongFragment.CODE_SERVER_ERROR,
                                        mContext.getResources().getString(R.string.message_server_error)));
                            }
                        }
                    }
                });
            }
        }
    }

    // TODO: Progress get list song by singer
    public void getListSongOfSinger(int page, final int singerId) {

        final Song s = DatabaseManager.newInstance().create(mContext).getSongSingerByPage(page, singerId);
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

                            data = response.body().getData();
                            setTotal_item(response.body().getTotal_item());
                            setCurrent_page(response.body().getCurrent_page());
                            setTotal_page(response.body().getCurrent_page());
                            Song song = response.body();
                            song.setSingerId(singerId);
                            DatabaseManager.newInstance().create(mContext).save(DatabaseManager.TAG_SONG_SINGER_DATABASE, song);
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<Song> call, Throwable t) {

                        if (!ClientUtil.isConnectInternet(mContext)) {

                        } else {

                            if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                            } else {

                            }
                        }
                    }
                });
            }
        }
    }

    // TODO: Progress get list song favorite
    public void getSongsFavorite(int userId, int page) {

        final Song s = DatabaseManager.newInstance().create(mContext).getSongFavoriteByPage(page);
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
                            data = response.body().getData();
                            setTotal_item(response.body().getTotal_item());
                            setCurrent_page(response.body().getCurrent_page());
                            setTotal_page(response.body().getCurrent_page());
                            Song song = response.body();
                            song.setFavorite(1);
                            DatabaseManager.newInstance().create(mContext)
                                    .save(DatabaseManager.TAG_SONG_FAVORITE_DATABASE, song);

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<Song> call, Throwable t) {

                        if (!ClientUtil.isConnectInternet(mContext)) {

                        } else {

                            if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                            } else {

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
}