package com.launchkey.sdk.http;


import net.sf.json.JSONObject;

public interface JSONHttpCallback {

    public void onSuccess(JSONObject response);
    public void onFailure(int status, JSONObject response);
}
