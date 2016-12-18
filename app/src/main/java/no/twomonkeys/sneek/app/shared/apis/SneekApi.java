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

    //Post
    @POST("v1/post")
    Call<ResponseModel> postPost(@Body HashMap<String, HashMap> body);

    @DELETE("v1/post/{post_id}")
    Call<ResponseModel> deletePost(
            @Path("post_id") String post_id
    );

    @GET("v1/user/{user_id}/story")
    Call<ResponseModel> getUserMoments(
            @Path("user_id") int user_id
    );

    // Feed
    @GET("v1/feed/following")
    Call<ResponseModel> getFeedFollowing();

    // Followings
    @GET("v1/following")
    Call<ResponseModel> getFollowing();

    //Suggestion
    @GET("v1/feed/suggestions")
    Call<ResponseModel> getSuggestion();


    //User
    @POST("v1/user")
    Call<ResponseModel> postUser(@Body HashMap<String, HashMap> body);

    @GET("v1/user/username_exists/{username}")
    Call<ResponseModel> getUsernameExists(
            @Path("username") String username
    );

    @GET("v1/user/{user_id}")
    Call<ResponseModel> getUser(
            @Path("user_id") int user_id
    );

    //Login
    @POST("v1/login")
    Call<ResponseModel> postLogin(@Body HashMap<String, HashMap> body);

    //Upload
    @POST("v1/post/generate_upload_url")
    Call<ResponseModel> postGenerateToken(
    );

    // Upload
    @PUT
    @Headers("Content-Type: multipart/form-data;boundary=95416089-b2fd-4eab-9a14-166bb9c5788b")
    Call<ResponseBody> upload(@Url String url,
                              @Body RequestBody body);

    // Pin
    @POST("v1/post/{post_id}/pin")
    Call<ResponseModel> postPin(
            @Path("post_id") String post_id
    );

    @DELETE("v1/post/{post_id}/pin")
    Call<ResponseModel> deletePin(
            @Path("post_id") String post_id
    );


}
