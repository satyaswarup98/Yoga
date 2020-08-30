package com.example.yoga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerView extends AppCompatActivity {
    String player1 = "", player2 = "", player3 = "";
     TextView tv_player1,tv_player2 , tv_player3 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_view);
        Intent intent = getIntent();

        final int stage_no = intent.getIntExtra("stage_no", 0);
        final int judge_no = intent.getIntExtra("judge_no", 0);
        int judge_type = intent.getIntExtra("judge_type", 0);

        TextView tv_judge_no = findViewById(R.id.tv_judge_no);
        TextView tv_stage_no = findViewById(R.id.tv_stage_no);

        tv_player1 = findViewById(R.id.tv_player1);
       tv_player2 = findViewById(R.id.tv_player2);
      tv_player3 = findViewById(R.id.tv_player3);
        Button btGotoMarking = findViewById(R.id.btGotoMarking);


        Button btPlayerEntry = findViewById(R.id.btPlayerEntry);
        btPlayerEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PlayerView.this, PlayerEntry.class);
                intent.putExtra("stage_no", stage_no);
                PlayerView.this.startActivity(intent);
            }
        });


        if (judge_type == 2) {
            btGotoMarking.setVisibility(View.VISIBLE);
            tv_judge_no.setVisibility(View.VISIBLE);
            tv_judge_no.setText("Judge : " + judge_no);
            btPlayerEntry.setVisibility(View.GONE);
        } else {
            tv_judge_no.setText("Judge : ");
            tv_judge_no.setVisibility(View.GONE);
            btGotoMarking.setVisibility(View.GONE);
            btPlayerEntry.setVisibility(View.VISIBLE);
        }
        tv_stage_no.setText("Stage : " + stage_no);

        btGotoMarking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateNull()) {
                    if (isNetworkConnected()) {
                        Intent intent = new Intent(PlayerView.this, Score.class);
                        intent.putExtra("stage_no", stage_no);
                        intent.putExtra("judge_no", judge_no);
                        intent.putExtra("player1", player1);
                        intent.putExtra("player2", player2);
                        intent.putExtra("player3", player3);
                        PlayerView.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerView.this);
                        builder.setMessage("Not Connected to Internet")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();
                    }
                }
            }
        });
        if(!isNetworkConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayerView.this);
                builder.setMessage("Not Connected to Internet")
                        .setNegativeButton("OK", null)
                        .create()
                        .show();

        }
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");


                                if (success) {
                                    player1 = jsonResponse.getInt("player1") + "";
                                    player2 = jsonResponse.getInt("player2") + "";
                                    player3 = jsonResponse.getInt("player3") + "";
                                    tv_player1.setText(player1);
                                    tv_player2.setText(player2);
                                    tv_player3.setText(player3);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PlayerFetch playerFetch = new PlayerFetch(stage_no + "", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PlayerView.this);
                    queue.add(playerFetch);
                }
            }, 0, 5000);


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerView.this);
        builder.setMessage("Do you want to Logout ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PlayerView.this, MainActivity.class);
                PlayerView.this.startActivity(intent);
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
        if (TextUtils.isEmpty(tv_player1.getText()) || TextUtils.isEmpty(tv_player2.getText())
                || TextUtils.isEmpty(tv_player3.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayerView.this);
            builder.setMessage("No player available at the movement, Wait for players to come.")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        } else {
            return true;
        }
    }
}
