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
                System.out.println("MAP returned: " + map);
                return map;
            }
        };
    }

    public static Contract v1_post_post() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                Map newMap = (Map) map.get("post");

                return newMap;
            }
        };
    }

    public static Contract v1_get_user_moments() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                System.out.println("MAP IS " + map);

                Map storyMap = (Map) map.get("story");

                ArrayList newMap = (ArrayList) storyMap.get("posts");
                Map userMap = (Map) storyMap.get("user");

                Map hMap = new HashMap<String, String>();
                hMap.put("posts", newMap);
                hMap.put("is_following", userMap.get("is_following"));

                return hMap;
            }
        };
    }

    /*NSDictionary *storyDic = [dictionary objectForKey:@"story"];
        NSDictionary *userDic = [storyDic objectForKey:@"user"];
        NSDictionary *postsDic = [storyDic objectForKey:@"posts"];


        return @{@"posts" : postsDic, @"is_following" : [userDic objectForKey:@"is_following"]}; //contractFormat;*/

    public static Contract v1_post_pin() {
        return new Contract() {
            @Override
            public Map generic_contract(Map map) {
                Map newMap = (Map) map.get("post");

                return newMap;
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
