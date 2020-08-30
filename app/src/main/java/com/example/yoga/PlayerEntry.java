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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerEntry extends AppCompatActivity {

    EditText player1, player2, player3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_entry);
        Intent intent = getIntent();
        final int stage_no = intent.getIntExtra("stage_no", 0);

        TextView tv_stage_no = findViewById(R.id.tv_stage_no);
        tv_stage_no.setText("Stage : " + stage_no);

        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        Button btPlayerEntry = findViewById(R.id.btPlayerEntry);

        Button btPlayerView = findViewById(R.id.btPlayerView);
        btPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerEntry.this, PlayerView.class);
                intent.putExtra("stage_no", stage_no);
                intent.putExtra("judge_type", 1);
                PlayerEntry.this.startActivity(intent);
            }
        });

        btPlayerEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateNull()) {
                    if (isNetworkConnected()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEntry.this);
                        builder.setMessage("Are you sure for final Submit ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final ProgressDialog progress = new ProgressDialog(PlayerEntry.this);
                                progress.setMessage("Updating Player List...");
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
                                                boolean error = jsonResponse.getBoolean("error");
                                                if (error) {
                                                    String msg = jsonResponse.getString("msg");
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEntry.this);
                                                    builder.setMessage(msg)
                                                            .setNegativeButton("OK", null)
                                                            .create()
                                                            .show();
                                                } else {
                                                    Intent intent = new Intent(PlayerEntry.this, PlayerView.class);
                                                    intent.putExtra("stage_no", stage_no);
                                                    intent.putExtra("judge_type", 1);
                                                    PlayerEntry.this.startActivity(intent);

                                                }
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEntry.this);
                                                builder.setMessage("One or More Invalid Player No")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                PlayerUpdate playerUpdate = new PlayerUpdate(stage_no + "", player1.getText().toString(), player2.getText().toString(), player3.getText().toString(), responseListener);
                                RequestQueue queue = Volley.newRequestQueue(PlayerEntry.this);
                                queue.add(playerUpdate);

                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEntry.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEntry.this);
        builder.setMessage("Do you want to Logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PlayerEntry.this, MainActivity.class);
                PlayerEntry.this.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", null);
        AlertDialog alert = builder.create();
        alert.show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    Boolean validateNull() {
        if (TextUtils.isEmpty(player1.getText()) || TextUtils.isEmpty(player2.getText())
                || TextUtils.isEmpty(player3.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayerEntry.this);
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
