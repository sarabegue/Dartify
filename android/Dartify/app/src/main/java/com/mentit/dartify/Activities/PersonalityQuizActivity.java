package com.mentit.dartify.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Test.PutPersonalityTestTask;
import com.mentit.dartify.util.Valores;

import es.dmoral.toasty.Toasty;

public class PersonalityQuizActivity extends AppCompatActivity implements PutPersonalityTestTask.OnTaskCompleted {
    private ProgressBar progressBar;
    private Button buttonEndPersonalityQuiz;
    private long buttonLastTimeClick = 0;

    final long time = 60000;

    private boolean opcionA = false;
    private boolean opcionB = false;
    private boolean opcionC = false;
    private boolean opcionX = false;
    private boolean opcionY = false;
    private boolean opcionZ = false;

    private LinearLayout view1;
    private LinearLayout view2;
    private LinearLayout view3;

    private TextView tv11;
    private TextView tv12;
    private TextView tv21;
    private TextView tv22;
    private TextView tv31;
    private TextView tv32;

    private long userid;
    private int numeneatipo;

    private int pantalla = 1;
    private Boolean finalizado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_personality_quiz);
        userid = SharedPreferenceUtils.getInstance(this).getLongValue(this.getString(R.string.user_id), 0);

        progressBar = findViewById(R.id.timeBar);
        buttonEndPersonalityQuiz = findViewById(R.id.buttonEndPersonalityQuiz);
        buttonEndPersonalityQuiz.setOnClickListener(clicklistener);

        view1 = findViewById(R.id.tvGroup1);
        view2 = findViewById(R.id.tvGroup2);
        view3 = findViewById(R.id.tvGroup3);

        tv11 = findViewById(R.id.tvg11);
        tv12 = findViewById(R.id.tvg12);
        tv21 = findViewById(R.id.tvg21);
        tv22 = findViewById(R.id.tvg22);
        tv31 = findViewById(R.id.tvg31);
        tv32 = findViewById(R.id.tvg32);

        view1.setOnClickListener(clickOpcion);
        view2.setOnClickListener(clickOpcion);
        view3.setOnClickListener(clickOpcion);

        setUpObserver();
    }

    private View.OnClickListener clickOpcion = v -> {
        String viewtag = v.getTag().toString();

        if (pantalla == 1) {
            switch (viewtag) {
                case "tvGroup1": {
                    estatusGrupo1(true, false, false);
                    break;
                }
                case "tvGroup2": {
                    estatusGrupo1(false, true, false);
                    break;
                }
                case "tvGroup3": {
                    estatusGrupo1(false, false, true);
                    break;
                }
            }
        }

        if (pantalla == 2) {
            switch (viewtag) {
                case "tvGroup1": {
                    estatusGrupo2(true, false, false);
                    break;
                }
                case "tvGroup2": {
                    estatusGrupo2(false, true, false);
                    break;
                }
                case "tvGroup3": {
                    estatusGrupo2(false, false, true);
                    break;
                }
            }
        }
        Log.d("TAG", viewtag);
    };

    private View.OnClickListener clicklistener = v -> {
        if (pantalla == 1)
            if (!opcionA && !opcionB && !opcionC)
                return;
        if (pantalla == 2)
            if (!opcionX && !opcionY && !opcionZ)
                return;

        //Bloqueo de botón
        if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_SHORT) return;
        buttonLastTimeClick = SystemClock.elapsedRealtime();

        if (pantalla == 1) {
            pantalla = 2;
            setText();
            view1.setBackgroundResource(R.drawable.dotted_border_blue);
            view2.setBackgroundResource(R.drawable.dotted_border_blue);
            view3.setBackgroundResource(R.drawable.dotted_border_blue);

            return;
        }

        if (pantalla == 2) {
            calcularEneatipo();
            Log.d("ENEATIPOC", numeneatipo + "");
            new PutPersonalityTestTask(PersonalityQuizActivity.this, userid, numeneatipo).execute("");
        }
    };

    @Override
    public void OnTaskCompletedPersonalityTest() {
        try {
            Thread thread = new Thread() {
                public void run() {
                    runOnUiThread(() -> Toasty.success(PersonalityQuizActivity.this, "¡Muy bien!", Toast.LENGTH_LONG).show());
                }
            };
            thread.start();
            Intent i = new Intent(this, MainActivity.class);
            finalizado = true;
            startActivity(i);
            finish();
        } catch (Exception w) {
            Log.d("", w.getMessage());
        }
    }

    private void setUpObserver() {
        progressBar.getViewTreeObserver().removeOnGlobalLayoutListener(gll);
        progressBar.getViewTreeObserver().addOnGlobalLayoutListener(gll);
    }

    private ViewTreeObserver.OnGlobalLayoutListener gll = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            startAnimation();
            progressBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    };

    private void startAnimation() {
        int width = progressBar.getWidth();
        progressBar.setMax(width);

        ValueAnimator animator = ValueAnimator.ofInt(width, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(time);

        animator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            progressBar.setProgress(value);
            Log.d("ANIMATOR", value + "");

            if (value == 0 && !finalizado) {
                Toasty.error(this, "Se terminó el tiempo, inténtalo nuevamente").show();

                Intent i = new Intent(this, PersonalityActivity.class);
                startActivity(i);
                finish();
            }
        });

        animator.start();
    }

    private void setText() {
        tv11.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv12.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv21.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv22.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv31.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv32.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv11.setText("Cooperador\nEntusiasta\nGeneroso");
        tv12.setText("Empático\nProtector\nSociable");
        tv21.setText("Consecuente\nReservado\nSensible");
        tv22.setText("Cauteloso\nPosesivo\nDecidido");
        tv31.setText("Perfeccionista\nDesapegado\nOrdenado");
        tv32.setText("Reservado\nInteligente\nObjetivo");
    }

    public void estatusGrupo1(boolean a, boolean b, boolean c) {
        opcionA = a;
        opcionB = b;
        opcionC = c;

        view1.setBackgroundResource(R.drawable.dotted_border_blue);
        view2.setBackgroundResource(R.drawable.dotted_border_blue);
        view3.setBackgroundResource(R.drawable.dotted_border_blue);

        tv11.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv12.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv21.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv22.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv31.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv32.setTextColor(getResources().getColor(R.color.color_darkgray));

        if (a) {
            tv11.setTextColor(getResources().getColor(R.color.white));
            tv12.setTextColor(getResources().getColor(R.color.white));
            view1.setBackgroundResource(R.drawable.dotted_border_blue_selected);
        }
        if (b) {
            tv21.setTextColor(getResources().getColor(R.color.white));
            tv22.setTextColor(getResources().getColor(R.color.white));
            view2.setBackgroundResource(R.drawable.dotted_border_blue_selected);
        }
        if (c) {
            tv31.setTextColor(getResources().getColor(R.color.white));
            tv32.setTextColor(getResources().getColor(R.color.white));
            view3.setBackgroundResource(R.drawable.dotted_border_blue_selected);
        }
    }

    public void estatusGrupo2(boolean x, boolean y, boolean z) {
        opcionX = x;
        opcionY = y;
        opcionZ = z;

        view1.setBackgroundResource(R.drawable.dotted_border_blue);
        view2.setBackgroundResource(R.drawable.dotted_border_blue);
        view3.setBackgroundResource(R.drawable.dotted_border_blue);

        tv11.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv12.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv21.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv22.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv31.setTextColor(getResources().getColor(R.color.color_darkgray));
        tv32.setTextColor(getResources().getColor(R.color.color_darkgray));

        if (x) {
            tv11.setTextColor(getResources().getColor(R.color.white));
            tv12.setTextColor(getResources().getColor(R.color.white));
            view1.setBackgroundResource(R.drawable.dotted_border_blue_selected);
        }
        if (y) {
            tv21.setTextColor(getResources().getColor(R.color.white));
            tv22.setTextColor(getResources().getColor(R.color.white));
            view2.setBackgroundResource(R.drawable.dotted_border_blue_selected);
        }
        if (z) {
            tv31.setTextColor(getResources().getColor(R.color.white));
            tv32.setTextColor(getResources().getColor(R.color.white));
            view3.setBackgroundResource(R.drawable.dotted_border_blue_selected);
        }
    }

    public void calcularEneatipo() {
        if (opcionA && opcionX) numeneatipo = 7;
        if (opcionA && opcionY) numeneatipo = 8;
        if (opcionA && opcionZ) numeneatipo = 3;
        if (opcionB && opcionX) numeneatipo = 9;
        if (opcionB && opcionY) numeneatipo = 4;
        if (opcionB && opcionZ) numeneatipo = 5;
        if (opcionC && opcionX) numeneatipo = 2;
        if (opcionC && opcionY) numeneatipo = 6;
        if (opcionC && opcionZ) numeneatipo = 1;
    }
}
