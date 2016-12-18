package no.twomonkeys.sneek.app.shared.models;

import java.util.Map;

import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.helpers.GenericContract;
import no.twomonkeys.sneek.app.shared.helpers.NetworkHelper;

/**
 * Created by simenlie on 27.10.2016.
 */

public class PinModel extends CRUDModel {

    int post_id;

    public PinModel(int post_id) {
        this.post_id = post_id;
    }

    @Override
    void build(Map map) {
        //Get post here
        System.out.println("Map pin: " + map);
    }


    public void save(NetworkCallback ncb)
    {
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().postPin(this.post_id + ""),
                GenericContract.generic_parse(),
                onDataReturned(),
                ncb);
    }

    public void delete(NetworkCallback ncb)
    {
        NetworkHelper.sendRequest(NetworkHelper.getNetworkService().deletePin(this.post_id + ""),
                GenericContract.generic_parse(),
                onDataReturned(),
                ncb);
    }
}
