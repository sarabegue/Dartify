package com.mentit.dartify.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mentit.dartify.R;

import java.io.InputStream;

public class TOSActivity extends AppCompatActivity {

    private TextView textViewTOS;
    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos);

        textViewTOS = findViewById(R.id.textViewTOS);
        buttonOK = findViewById(R.id.buttonOK);

        String tos = "";
        try {
            Resources res = getResources();
            InputStream is = res.openRawResource(R.raw.terms);
            byte[] b = new byte[is.available()];
            is.read(b);
            tos = new String(b);

            textViewTOS.setText(tos);
        } catch (Exception e) {
        }

        buttonOK.setOnClickListener(botonOK);
    }

    private View.OnClickListener botonOK = v -> {
        try {
            Intent i = new Intent(TOSActivity.this, MainActivity.class);
            startActivity(i);
        } catch (Exception e) {
            Log.d("IOEXCEPTION", e.getMessage());
            e.printStackTrace();
        }
    };
}
