package leduyhung.me.vocaloid.rest;

import leduyhung.me.vocaloid.model.Base;
import leduyhung.me.vocaloid.model.song.Song;
import leduyhung.me.vocaloid.model.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskApi {

    @POST("/v1/auth")
    Call<User> login(@Header("version") int version, @Header("pakage") String pakage, @Body User user);

    @GET("/v1/songs")
    Call<Song> getSongs(@Header("version") int version, @Header("pakage") String pakage,
                        @Query("name") String name, @Query("page") int page);

    @PUT("/v1/song/{songId}")
    Call<Song> updateViewSong(@Header("version") int version, @Header("pakage") String pakage);

    @GET("/v1/singer/{singerId}/songs")
    Call<Song> getSongsBySinger(@Header("version") int version, @Header("pakage") String pakage,
                                @Path("singerId") int singerId, @Query("page") int page);

    @GET("/v1/user/{userId}/songs")
    Call<Song> getSongsFavorite(@Header("version") int version, @Header("pakage") String pakage,
                                @Path("userId") int singerId, @Query("page") int page);

    @POST("/v1/user/{userId}/songs")
    Call<Song> addDeleteSongFavorite(@Header("version") int version, @Header("pakage") String pakage, @Path("userId") int userId,
                               @Body Base base);
}
