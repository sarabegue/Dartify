package com.mentit.dartify.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.mentit.dartify.R;
import com.mentit.dartify.util.Valores;

public class PersonalityActivity extends AppCompatActivity {
    private View buttonPersonality;
    private long buttonLastTimeClick = 0;
    VideoView video1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_personality);

        video1 = findViewById(R.id.videoView);
        buttonPersonality = findViewById(R.id.buttonPersonalityQuiz);
        buttonPersonality.setOnClickListener(clickPersonalityQuiz);
    }

    @Override
    protected void onStart() {
        super.onStart();

        video1.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_03));
        video1.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
        video1.start();
    }

    private View.OnClickListener clickPersonalityQuiz = v -> {
        //Bloqueo de botón
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_SHORT) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        Intent i = new Intent(v.getContext(), PersonalityQuizActivity.class);
        startActivity(i);
        finish();
    };
}
