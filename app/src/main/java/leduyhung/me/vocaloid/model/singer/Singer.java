package leduyhung.me.vocaloid.model.singer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.converter.ConverterListSingerInfo;
import leduyhung.me.vocaloid.db.DatabaseManager;
import leduyhung.me.vocaloid.model.Base;
import leduyhung.me.vocaloid.rest.BaseApi;
import leduyhung.me.vocaloid.util.ClientUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Entity(tableName = Constants.DB.TABLE_SINGER)

public class Singer extends Base {

    @Ignore
    private transient Context mContext;

    @Ignore
    private transient Call<Singer> call;

    @TypeConverters(ConverterListSingerInfo.class)
    private ArrayList<SingerInfo> data;

    private OnSingerDataFromServer onSingerDataFromServer;

    public ArrayList<SingerInfo> getData() {
        return data;
    }

    public void setData(ArrayList<SingerInfo> data) {
        this.data = data;
    }

    public void create(Context mContext) {

        this.mContext = mContext;
    }

    public void getSingers(final String name, int page) {

        final Singer s = DatabaseManager.newInstance().create(mContext).getSingersByPage(page);
        if (!DatabaseManager.newInstance().create(mContext).isExpired(DatabaseManager.TAG_SINGER_DATABASE) &&
                s != null && s.getData().size() > 0 && name == null) {
            data = s.getData();
        } else {
            if (call == null || call.isCanceled()) {
                call = BaseApi.request().getSingers(ClientUtil.getVersionCode(mContext), ClientUtil.getPakageName(mContext), name, page);
                call.enqueue(new Callback<Singer>() {
                    @Override
                    public void onResponse(Call<Singer> call, Response<Singer> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (name == null)
                                DatabaseManager.newInstance().create(mContext).save(DatabaseManager.TAG_SINGER_DATABASE, response.body());
                            data = response.body().getData();
                            setTotal_item(response.body().getTotal_item());
                            setCurrent_page(response.body().getCurrent_page());
                            setTotal_page(response.body().getCurrent_page());
                            if (onSingerDataFromServer != null)
                                onSingerDataFromServer.onSuccess();
                        } else {
                            if (onSingerDataFromServer != null)
                                onSingerDataFromServer.onFail(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Singer> call, Throwable t) {
                        if (!ClientUtil.isConnectInternet(mContext)) {

                            if (onSingerDataFromServer != null)
                                onSingerDataFromServer.onFail(null);
                        } else {

                            if (t instanceof TimeoutException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {

                                if (onSingerDataFromServer != null)
                                    onSingerDataFromServer.onFail(null);
                            } else {

                                if (onSingerDataFromServer != null)
                                    onSingerDataFromServer.onFail(null);
                            }
                        }
                    }
                });
            }
        }
    }

    public interface OnSingerDataFromServer {

        void onSuccess();

        void onFail(String message);
    }

}