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
import no.twomonkeys.sneek.app.shared.SimpleCallback;
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

    public static void sendRequest(Call<ResponseModel> call, final Contract contract, final MapCallback mcb, final SimpleCallback scb) {
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseModel = response.body();
                if (responseModel != null){
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

    public static SneekApi getNetworkService()
    {
        // TODO: When implementing login replace this
        // String authToken = DataHelper.getAuthToken();
        String authToken = "f383f4ad296fd296a8d955f389774492";
        if (authToken != auth_token)
        {
            auth_token = authToken;
            networkService = ServiceGenerator.createService(SneekApi.class, DataHelper.getAuthToken());
        }

        return networkService;
    }

    public static void handleError(Response<ResponseModel> response, final SimpleCallback scb) {
        ResponseModel responseModel = response.body();
        ResponseBody errorData = response.errorBody();
        ErrorModel errorModel;

        Log.d(TAG, "onResponse - Status : " + response.code());
        if (response.code() == 401)
        {
            //Log out
            // DataHelper.startActivity.logout();
        }
        Log.d(TAG, "onResponse - BODY : " + response.body());

        if (responseModel != null && responseModel.success) {
            //Success
            Log.v(TAG, "SUCESS " + responseModel.data);
            scb.callbackCall(null);
        } else {
            if (errorData == null) {
                HashMap<String, String> data = new HashMap<>();
                data.put("message_id", "generic_error");
                errorModel = new ErrorModel(DataHelper.getContext(), data);
                scb.callbackCall(errorModel);
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
                    scb.callbackCall(errorModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

