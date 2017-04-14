package com.shubhangi.groctaurantphp;

import org.json.JSONObject;

/**
 * Created by Shubhangi Bharti on 03-Apr-17.
 */

public class Callback {
    public interface JSONCallback{
        void call(JSONObject JSON);
    }
}
