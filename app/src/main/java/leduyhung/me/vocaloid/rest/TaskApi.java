package leduyhung.me.vocaloid.rest;

import leduyhung.me.vocaloid.model.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TaskApi {

    @POST("/v1/auth")
    Call<User> login(@Header("version") int version, @Header("pakage") String pakage, @Body User user);
}
