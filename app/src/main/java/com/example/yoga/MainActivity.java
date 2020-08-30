package com.example.yoga;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText stage_no, username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stage_no = findViewById(R.id.stage_no);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        final Button btLogin = findViewById(R.id.btLogin);


        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validateNull()) {
                    if (isNetworkConnected()) {
                    final ProgressDialog progress = new ProgressDialog(MainActivity.this);
                    progress.setMessage("Verifying Login Details...");
                    progress.setCancelable(false);
                    progress.show();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                progress.dismiss();

                                    if (success) {
                                        int judge_type = jsonResponse.getInt("judge_type");
                                        boolean error = jsonResponse.getBoolean("error");
                                        if (judge_type == 1) {
                                            if (error) {
                                                String msg = jsonResponse.getString("msg");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setMessage(msg)
                                                        .setNegativeButton("OK", null)
                                                        .create()
                                                        .show();
                                            } else {
                                                int stage_no = jsonResponse.getInt("stage_no");
                                                Intent intent = new Intent(MainActivity.this, PlayerEntry.class);
                                                intent.putExtra("stage_no", stage_no);
                                                MainActivity.this.startActivity(intent);
                                            }
                                        } else if (judge_type == 2) {
                                            if (error) {
                                                String msg = jsonResponse.getString("msg");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setMessage(msg)
                                                        .setNegativeButton("OK", null)
                                                        .create()
                                                        .show();
                                            } else {
                                                int stage_no = jsonResponse.getInt("stage_no");
                                                int judge_no = jsonResponse.getInt("judge_no");
                                                Intent intent = new Intent(MainActivity.this, PlayerView.class);
                                                intent.putExtra("stage_no", stage_no);
                                                intent.putExtra("judge_no", judge_no);
                                                intent.putExtra("judge_type", judge_type);
                                                MainActivity.this.startActivity(intent);
                                            }
                                        } else {
                                            if (error) {
                                                String msg = jsonResponse.getString("msg");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setMessage(msg)
                                                        .setNegativeButton("OK", null)
                                                        .create()
                                                        .show();
                                            }
                                        }
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setMessage("Invalid Login Details")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        LoginRequest loginRequest = new LoginRequest(stage_no.getText().toString(), username.getText().toString(), password.getText().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(loginRequest);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Not Connected to Internet")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    Boolean validateNull() {
        if (TextUtils.isEmpty(stage_no.getText()) || TextUtils.isEmpty(username.getText())
                || TextUtils.isEmpty(password.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Fill out All Fields to Proceed ...")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        } else {
            return true;
        }
    }
}
