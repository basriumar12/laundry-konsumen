package com.samyotech.laundry.interfaces;

import org.json.JSONException;
import org.json.JSONObject;


public interface Helper {
    void backResponse(boolean flag, String msg, JSONObject response) throws JSONException;
}
