package no.twomonkeys.sneek.app.shared.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 26/09/16 by chridal
 * Copyright 2MONKEYS AS
 */

public class GenericContract {


    public static Contract get_story() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return (Map) map.get("story");
            }
        };
    }

    public static Contract v1_get_stream() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return (Map) map.get("stream");
            }
        };
    }

    public static Contract get_feed() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                Log.v("Fetched map ","map fetched: " + map);


                return map;
            }
        };
    }

    public static Contract generate_upload_url() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return (Map) map.get("upload_url");
            }
        };
    }

    public static Contract generic_parse() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return map;
            }
        };
    }

    public static Contract v1_get_user_username_exists() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return map;
            }
        };
    }

    public static Contract v1_get_user() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return (Map) map.get("user");
            }
        };
    }



    public static Contract v1_post_user() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                Map newMap = (Map) map.get("user");
                newMap.put("story", map.get("story"));
                newMap.put("user_session", map.get("user_session"));

                return newMap;
            }
        };
    }

    public static Contract v1_post_login() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                Map newMap = (Map) map.get("user_session");
                newMap.put("user", map.get("user"));
                newMap.put("stalkings", map.get("stalkings"));

                return map;
            }
        };
    }

    public static Contract v1_get_stream_by_name() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return (Map) map.get("stream");
            }
        };
    }

    public static Contract v1_get_user_by_username() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                return (Map) map.get("user");
            }
        };
    }
}
