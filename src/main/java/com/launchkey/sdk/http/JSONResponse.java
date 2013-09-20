package com.launchkey.sdk.http;

import net.sf.json.JSONObject;

public class JSONResponse {
    private JSONObject json = new JSONObject();
    private boolean isSuccess;

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
