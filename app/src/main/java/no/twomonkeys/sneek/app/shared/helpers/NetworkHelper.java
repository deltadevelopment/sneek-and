package no.twomonkeys.sneek.app.shared.helpers;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import no.twomonkeys.sneek.app.shared.MapCallback;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.apis.SneekApi;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.ResponseModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkHelper {

    private static final String TAG = "NetworkHelper";
    private static String auth_token;
    private static SneekApi networkService;

    public static void sendRequest(Call<ResponseModel> call, final Contract contract, final MapCallback mcb, final NetworkCallback scb) {
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseModel = response.body();
                if (responseModel != null) {
                    System.out.println("DATA: " + responseModel.data);
                    mcb.callbackCall(contract.generic_contract(responseModel.data));
                }
                handleError(response, scb);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Log.v(TAG, "FAILURE");
                t.printStackTrace();
            }
        });
    }

    public static SneekApi getNetworkService() {
        // TODO: When implementing login replace this
        // String authToken = DataHelper.getAuthToken();
        String authToken = DataHelper.getAuthToken();
        System.out.println("authtoken " + authToken);

        if (authToken == null) {
            networkService = ServiceGenerator.createService(SneekApi.class);
            return networkService;
        }
        if (authToken != auth_token) {
            auth_token = authToken;
            networkService = ServiceGenerator.createService(SneekApi.class, DataHelper.getAuthToken());
        }
        return networkService;
    }

    public static void handleError(Response<ResponseModel> response, final NetworkCallback scb) {
        ResponseModel responseModel = response.body();
        ResponseBody errorData = response.errorBody();
        ErrorModel errorModel;

        Log.d(TAG, "onResponse - Status : " + response.code());
        if (response.code() == 401) {
            //Log out
            DataHelper.startActivity.logout();
            System.out.println("Should log out the user here");
        }
        Log.d(TAG, "onResponse - BODY : " + response.body());

        if (responseModel != null && responseModel.success) {
            //Success
            Log.v(TAG, "SUCESS " + responseModel.data);
            scb.exec(null);
        } else {
            if (errorData == null) {
                HashMap<String, String> data = new HashMap<>();
                data.put("message_id", "generic_error");
                errorModel = new ErrorModel(DataHelper.getContext(), data);
                scb.exec(errorModel);
            } else {
                try {
                    Map<String, Object> retMap = new Gson().fromJson(errorData.string(), new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                    HashMap<String, Object> withData;
                    if (retMap.get("data") != null) {
                        withData = (HashMap<String, Object>) retMap;
                    } else {
                        withData = new HashMap<>();
                        withData.put("data", retMap);
                    }
                    errorModel = new ErrorModel(DataHelper.getContext(), withData);
                    scb.exec(errorModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void uploadFile(File file, String url, final NetworkCallback scb, ProgressRequestBody.UploadCallbacks listener) {
        SneekApi service =
                ServiceGenerator.createService(SneekApi.class);

        ProgressRequestBody prb = new ProgressRequestBody(file, listener);
        RequestBody requestFile = ProgressRequestBody.create(MediaType.parse("image/jpg"), file);

        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();

        Call<ResponseBody> call = service.upload(uri, prb);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                scb.exec(null);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }


}

