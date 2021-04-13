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

import com.mentit.dartify.Models.Chiste;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Test.GetHumorTestTask;
import com.mentit.dartify.util.Valores;

import java.util.ArrayList;

public class HumorActivity extends AppCompatActivity implements GetHumorTestTask.OnTaskCompleted {
    private View buttonHumor;
    private long buttonLastTimeClick = 0;
    VideoView video1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_humor);

        video1 = findViewById(R.id.videoView);
        buttonHumor = findViewById(R.id.buttonHumorQuiz);
        buttonHumor.setOnClickListener(clickHumorQuiz);
    }

    @Override
    protected void onStart() {
        super.onStart();

        video1.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_02));
        video1.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
        video1.start();
    }

    private View.OnClickListener clickHumorQuiz = v -> {
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        new GetHumorTestTask(HumorActivity.this, 0).execute("");
    };

    @Override
    public void OnTaskCompletedGetHumor(ArrayList<Chiste> lista) {
        Intent i = new Intent(HumorActivity.this, HumorQuizActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("lista", lista);
        i.putExtras(b);
        startActivity(i);
        finish();
    }
}
