package com.example.yoga;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlayerFetch extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://barabatiyoga.com/my/PlayerFetch.php";
    private Map<String, String> params;

    public PlayerFetch(String stage_no, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("stage_no", stage_no);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
