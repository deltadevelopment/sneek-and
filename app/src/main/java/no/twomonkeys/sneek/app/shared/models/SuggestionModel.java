package no.twomonkeys.sneek.app.shared.models;

import java.util.ArrayList;
import java.util.Map;

import no.twomonkeys.sneek.app.shared.MapCallback;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;
import no.twomonkeys.sneek.app.shared.interfaces.ArrayCallback;

/**
 * Created by simenlie on 24.10.2016.
 */

public class SuggestionModel extends CRUDModel {

    @Override
    void build(Map map) {

    }

    public SuggestionModel(Map map){
        build(map);
    }

    public static void fetchAll(final ArrayCallback arrayCallback){

        final ArrayList suggestions = new ArrayList<FollowingModel>();

        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().getSuggestion(),
                GenericContract.generic_parse(),
                new MapCallback() {
                    @Override
                    public void callbackCall(Map map) {
                        ArrayList<Map> followingsArray = (ArrayList<Map>) map.get("users");

                        for(Map followingMap : followingsArray){
                            suggestions.add(new UserModel(followingMap));
                        }
                    }
                },
                new NetworkCallback() {
                    @Override
                    public void exec(ErrorModel errorModel) {
                        arrayCallback.exec(suggestions, errorModel);
                    }
                }
        );

    }
}
