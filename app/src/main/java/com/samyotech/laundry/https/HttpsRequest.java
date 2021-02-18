package com.samyotech.laundry.https;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.interfaces.Helper;
import com.samyotech.laundry.jsonparser.JSONParser;
import com.samyotech.laundry.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class
HttpsRequest {
    private final String match;
    private  String otp;
    private String user_id;
    private final Context ctx;
    private Map<String, String> params;
    private Map<String, File> fileparams;
    private Map<String, ArrayList<File>> multiFileparams;
    private JSONObject jObject;

    public HttpsRequest(String match, Map<String, String> params, Context ctx) {
        this.match = match;
        this.params = params;
        this.ctx = ctx;
    }


    public HttpsRequest(String match, Map<String, String> params, Map<String, File> fileparams, Context ctx) {
        this.match = match;
        this.params = params;
        this.fileparams = fileparams;
        this.ctx = ctx;
    }

    public HttpsRequest(String match, Map<String, String> params, Map<String, ArrayList<File>> multiFileparams, Context ctx, Context mctx) {
        this.match = match;
        this.params = params;
        this.multiFileparams = multiFileparams;
        this.ctx = ctx;
    }

    public HttpsRequest(String match, Context ctx) {
        this.match = match;
        this.ctx = ctx;
    }

    public HttpsRequest(String match, Context ctx, String user_id, String otp) {
        this.match = match;
        this.ctx = ctx;
        this.otp = otp;
        this.user_id = user_id;
    }

    public HttpsRequest(String match, JSONObject jObject, Context ctx) {
        this.match = match;
        this.jObject = jObject;
        this.ctx = ctx;


    }


    public void stringPostJson(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.API_URL + match)
                .addJSONObjectBody(jObject)
                .setTag("test")
                //.addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                        ProjectUtils.showLog(TAG, " response body --->" + jObject.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringPost(final String TAG, final Helper h) {

        //unsafe// OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();;
        ANRequest test = AndroidNetworking.post(Consts.API_URL + match)
                //   .setOkHttpClient(okHttpClient)
                .addBodyParameter(params)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build();
        ProjectUtils.showLog(TAG, " url --->" + test.getUrl());
        test.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                ProjectUtils.showLog(TAG, " param --->" + params.toString());
                JSONParser jsonParser = new JSONParser(ctx, response);
                if (jsonParser.RESULT) {
                    try {
                        h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringGet(final String TAG, final Helper h) {
        ANRequest request = AndroidNetworking.get(Consts.API_URL + match)
                .setTag("test")

                .setPriority(Priority.HIGH)
                .build();
        ProjectUtils.showLog(TAG, " url --->" + request.getUrl());

        request
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

   public void stringOTP(final String TAG, final Helper h) {
       ProjectUtils.showLog(TAG, " url --->" + match);
        ANRequest request = AndroidNetworking.get(Consts.API_URL + match)
                .setTag("test")
                .addQueryParameter("user_id", user_id)
                .addQueryParameter("otp", otp)
                .setPriority(Priority.HIGH)
                .build();
        ProjectUtils.showLog(TAG, " url --->" + request.getUrl());

        request
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }


     public void stringResendOTP(final String TAG, final Helper h) {
       ProjectUtils.showLog(TAG, " url --->" + match);
        ANRequest request = AndroidNetworking.get(Consts.API_URL + match)
                .setTag("test")
                .addQueryParameter("user_id", user_id)

                .setPriority(Priority.HIGH)
                .build();
        ProjectUtils.showLog(TAG, " url --->" + request.getUrl());

        request
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void imagePost(final String TAG, final Helper h) {
        AndroidNetworking.upload(Consts.API_URL + match)
                .addMultipartFile(fileparams)
                .addMultipartParameter(params)
                .setTag("uploadTest")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        ProjectUtils.showLog("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                        ProjectUtils.showLog(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);

                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void multiImagePost(final String TAG, final Helper h) {
        AndroidNetworking.upload(Consts.API_URL + match)
                .addMultipartFileList(multiFileparams)
                .addMultipartParameter(params)
                .setTag("uploadTest")
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        ProjectUtils.showLog("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProjectUtils.showLog(TAG, " response body --->" + response.toString());
                        ProjectUtils.showLog(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);

                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        ProjectUtils.showLog(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }


}