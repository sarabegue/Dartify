package com.mentit.dartify.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.mentit.dartify.R;
import com.mentit.dartify.util.Valores;


public class JustNowActivity extends AppCompatActivity {
    Button bContinuar;
    private long buttonLastTimeClick = 0;
    VideoView video1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_just_now);

        video1 = findViewById(R.id.videoView);
        bContinuar = findViewById(R.id.buttonContinuar);
        bContinuar.setOnClickListener(clickContinuarListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        video1.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_01));
        video1.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
        video1.start();
    }

    private View.OnClickListener clickContinuarListener = v -> {
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        Intent i;
        i = new Intent(JustNowActivity.this, JustNowQuizActivity.class);

        startActivity(i);
    };
}
