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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Score extends AppCompatActivity {

    EditText r1_1, r1_2, r1_3, r2_1, r2_2, r2_3, r3_1, r3_2, r3_3, r4_1, r4_2, r4_3, r5_1, r5_2, r5_3;
    int total_p1, total_p2, total_p3;
    String player1 = "", player2 = "", player3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        r1_1 = findViewById(R.id.r1_1);
        r1_2 = findViewById(R.id.r1_2);
        r1_3 = findViewById(R.id.r1_3);
        r2_1 = findViewById(R.id.r2_1);
        r2_2 = findViewById(R.id.r2_2);
        r2_3 = findViewById(R.id.r2_3);
        r3_1 = findViewById(R.id.r3_1);
        r3_2 = findViewById(R.id.r3_2);
        r3_3 = findViewById(R.id.r3_3);
        r4_1 = findViewById(R.id.r4_1);
        r4_2 = findViewById(R.id.r4_2);
        r4_3 = findViewById(R.id.r4_3);
        r5_1 = findViewById(R.id.r5_1);
        r5_2 = findViewById(R.id.r5_2);
        r5_3 = findViewById(R.id.r5_3);
        Button btSubmitMarks = findViewById(R.id.btSubmitMarks);


        Intent intent = getIntent();
        final int stage_no = intent.getIntExtra("stage_no", 0);
        final int judge_no = intent.getIntExtra("judge_no", 2);

        player1 = intent.getStringExtra("player1");
        player2 = intent.getStringExtra("player2");
        player3 = intent.getStringExtra("player3");

        LinearLayout all = findViewById(R.id.all);
        findAllTextViews(all);

        btSubmitMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sum_t1() && sum_t2() && sum_t3()) {
                    if (isNetworkConnected()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
                        builder.setMessage("Are you sure for final Submit ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                final ProgressDialog progress = new ProgressDialog(Score.this);
                                progress.setMessage("Uploading Player Marks...");
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
                                                Intent intent = new Intent(Score.this, PlayerView.class);
                                                intent.putExtra("stage_no", stage_no);
                                                intent.putExtra("judge_no", judge_no);
                                                intent.putExtra("judge_type", 2);
                                                Score.this.startActivity(intent);
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
                                                builder.setMessage("Can' Save the marks, Try Again")
                                                        .setNegativeButton("Retry", null)
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                MarksUpload marksUpload = new MarksUpload(stage_no + "", judge_no + "", player1, player2, player3, total_p1 + "", total_p2 + "", total_p3 + "", responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Score.this);
                                queue.add(marksUpload);

                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
        builder.setMessage("Please Submit the marks to Go Back")
                .setNegativeButton("OK", null)
                .create()
                .show();
    }

    private void findAllTextViews(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                findAllTextViews((ViewGroup) view);
            else if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setText(tv.getText().toString().replace("PLAYER 1", "PN-" + player1));
                tv.setText(tv.getText().toString().replace("PLAYER 2", "PN-" + player2));
                tv.setText(tv.getText().toString().replace("PLAYER 3", "PN-" + player3));
            }
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    Boolean sum_t1() {
        if (TextUtils.isEmpty(r1_1.getText()) || TextUtils.isEmpty(r2_1.getText()) || TextUtils.isEmpty(r3_1.getText())
                || TextUtils.isEmpty(r4_1.getText()) || TextUtils.isEmpty(r5_1.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
            builder.setMessage("Enter marks for each rounds of first player")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        }
        else if ( (Integer.parseInt(r1_1.getText().toString())>10) || (Integer.parseInt(r2_1.getText().toString())>10) ||
                (Integer.parseInt(r3_1.getText().toString()) >10) || (Integer.parseInt(r4_1.getText().toString())>10) ||
                (Integer.parseInt(r5_1.getText().toString())>10)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
            builder.setMessage("Maximum marks is 10, Check for each rounds of first player")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        }
        else {
            total_p1 = (Integer.parseInt(r1_1.getText().toString()) + Integer.parseInt(r2_1.getText().toString())
                    + Integer.parseInt(r3_1.getText().toString()) + Integer.parseInt(r4_1.getText().toString())
                    + Integer.parseInt(r5_1.getText().toString()));
            return true;

        }

    }

    Boolean sum_t2() {
        if (TextUtils.isEmpty(r1_2.getText()) || TextUtils.isEmpty(r2_2.getText()) || TextUtils.isEmpty(r3_2.getText())
                || TextUtils.isEmpty(r4_2.getText()) || TextUtils.isEmpty(r5_2.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
            builder.setMessage("Enter marks for each rounds of second player")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        }
        else if ( (Integer.parseInt(r1_2.getText().toString())>10) || (Integer.parseInt(r2_2.getText().toString())>10) ||
                (Integer.parseInt(r3_2.getText().toString()) >10) || (Integer.parseInt(r4_2.getText().toString())>10) ||
                (Integer.parseInt(r5_2.getText().toString())>10)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
            builder.setMessage("Maximum marks is 10, Check for each rounds of second player")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        }
        else {
            total_p2 = (Integer.parseInt(r1_2.getText().toString()) + Integer.parseInt(r2_2.getText().toString())
                    + Integer.parseInt(r3_2.getText().toString()) + Integer.parseInt(r4_2.getText().toString())
                    + Integer.parseInt(r5_2.getText().toString()));
            return true;
        }
    }

    Boolean sum_t3() {
        if (TextUtils.isEmpty(r1_3.getText()) || TextUtils.isEmpty(r2_3.getText()) || TextUtils.isEmpty(r3_3.getText())
                || TextUtils.isEmpty(r4_3.getText()) || TextUtils.isEmpty(r5_3.getText())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
            builder.setMessage("Enter marks for each rounds of third player")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        }
        else if ( (Integer.parseInt(r1_3.getText().toString())>10) || (Integer.parseInt(r2_3.getText().toString())>10) ||
                (Integer.parseInt(r3_3.getText().toString()) >10) || (Integer.parseInt(r4_3.getText().toString())>10) ||
                (Integer.parseInt(r5_3.getText().toString())>10)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Score.this);
            builder.setMessage("Maximum marks is 10, Check for each rounds of third player")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();
            return false;
        }
        else {
            total_p3 = (Integer.parseInt(r1_3.getText().toString()) + Integer.parseInt(r2_3.getText().toString())
                    + Integer.parseInt(r3_3.getText().toString()) + Integer.parseInt(r4_3.getText().toString())
                    + Integer.parseInt(r5_3.getText().toString()));
            return true;
        }
    }
}
