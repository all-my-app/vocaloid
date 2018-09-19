package leduyhung.me.vocaloid.rest;

import java.util.concurrent.TimeUnit;

import leduyhung.me.vocaloid.Constants;
import leduyhung.me.vocaloid.util.GsonUtil;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApi {

    private static final int TIMEOUT = 5;

    private static Retrofit.Builder builder;
    private static TaskApi taskApi;

    public static TaskApi request() {

        if (taskApi == null) {

            builder = new Retrofit.Builder().baseUrl(Constants.Server.SERVER_ADDRESS)
                    .addConverterFactory(GsonConverterFactory.create(GsonUtil.newInstance()));
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = builder.client(httpClient.build()).build();
        taskApi = retrofit.create(TaskApi.class);

        return taskApi;
    }
}
