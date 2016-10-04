package no.twomonkeys.sneek.app.shared.apis;

import org.json.JSONObject;

import java.util.HashMap;

import no.twomonkeys.sneek.app.shared.helpers.ProgressRequestBody;
import no.twomonkeys.sneek.app.shared.models.ResponseModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;


public interface SneekApi {

    // Followings
    @GET("v1/following")
    Call<ResponseModel> getFollowing();

    // Upload

    @PUT
    @Headers("Content-Type: multipart/form-data;boundary=95416089-b2fd-4eab-9a14-166bb9c5788b")
    Call<ResponseBody> upload(@Url String url,
                              @Body RequestBody body);

}
