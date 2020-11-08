package com.samyotech.laundry.jsonparser;

import android.content.Context;

import com.samyotech.laundry.preferences.SharedPrefrence;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;


public class JSONParser {
    public static String PIC_KEY = "profilepic";
    public static String TAG_SUCCESS = "status";
    public static String TAG_MESSAGE = "message";
    public String SUCCESS = "";
    public String MESSAGE = "";
    public boolean RESULT = false;
    public Context context;
    public JSONObject jObj;
    String jsonObjResponse;
    private SharedPrefrence prefrence;

    public JSONParser(Context context, JSONObject response) {
        try {
            this.context = context;
            jObj = response;
            SUCCESS = getJsonString(jObj, TAG_SUCCESS);
            MESSAGE = html2text(getJsonString(jObj, TAG_MESSAGE));
            prefrence = SharedPrefrence.getInstance(context);
            /* else if (SUCCESS.equals("3")) {
                prefrence.clearAllPreferences();
                Intent intent = new Intent(context, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }*/
            RESULT = !SUCCESS.equals("0");

        } catch (Exception e) {
            jObj = null;
            e.printStackTrace();
        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean getBoolean(String val) {
        return val.equals("true");
    }

    public static JSONObject getJsonObject(JSONObject obj, String parameter) {
        try {
            return obj.getJSONObject(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }

    }

    public static String getJsonString(JSONObject obj, String parameter) {
        try {
            String val = obj.getString(parameter);
            if (val != null) {
                if (val.equalsIgnoreCase("null") || val.equalsIgnoreCase(""))
                    return "";
                else
                    return val;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static JSONArray getJsonArray(JSONObject obj, String parameter) {
        try {
            return obj.getJSONArray(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }

    }


}