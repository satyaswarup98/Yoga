package com.example.yoga;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlayerUpdate extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://barabatiyoga.com/my/PlayerUpdate.php";
    private Map<String, String> params;

    public PlayerUpdate(String stage_no, String player1, String player2, String player3, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("stage_no", stage_no);
        params.put("player1", player1);
        params.put("player2", player2);
        params.put("player3", player3);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
