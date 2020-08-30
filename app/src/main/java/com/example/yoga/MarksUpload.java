package com.example.yoga;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MarksUpload extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://barabatiyoga.com/my/MarksUpload.php";
    private Map<String, String> params;

    public MarksUpload( String stage_no, String judge_no, String player1, String player2, String player3,String total_player1, String total_player2, String total_player3, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("stage_no", stage_no);
        params.put("judge_no", judge_no);
        params.put("player1", player1);
        params.put("player2", player2);
        params.put("player3", player3);
        params.put("total_player1", total_player1);
        params.put("total_player2", total_player2);
        params.put("total_player3", total_player3);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
