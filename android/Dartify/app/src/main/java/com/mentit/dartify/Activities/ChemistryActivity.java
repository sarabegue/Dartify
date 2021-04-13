package com.mentit.dartify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mentit.dartify.R;
import com.mentit.dartify.util.Valores;

public class ChemistryActivity extends AppCompatActivity {
    private View buttonPersonality;
    private View buttonHumor;

    private TextView textviewHumor;
    private TextView textviewPersonality;

    private long buttonLastTimeClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chemistry);

        textviewHumor = findViewById(R.id.textviewHumor);
        textviewPersonality = findViewById(R.id.textviewPersonality);

        buttonPersonality = findViewById(R.id.buttonPersonality);
        buttonHumor = findViewById(R.id.buttonHumor);

        buttonHumor.setOnClickListener(clickHumor);
        buttonPersonality.setOnClickListener(clickPersonality);

        textviewHumor.setBackgroundResource(R.drawable.actionbutton);
        textviewPersonality.setBackgroundResource(R.drawable.actionbutton);

    }

    private View.OnClickListener clickPersonality = v -> {
        //Bloqueo de botón
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        Intent i = new Intent(v.getContext(), PersonalityActivity.class);
        startActivity(i);
        finish();
    };

    private View.OnClickListener clickHumor = v -> {
        //Bloqueo de botón
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        Intent i = new Intent(v.getContext(), HumorActivity.class);
        startActivity(i);
        finish();
    };


}
