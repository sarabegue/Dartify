package com.mentit.dartify.Activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Test.PutJustNowTask;
import com.mentit.dartify.util.Valores;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class JustNowQuizActivity extends AppCompatActivity implements PutJustNowTask.OnTaskCompleted {

    private ImageView ivo0;
    private ImageView ivo1;
    private ImageView ivo2;
    private ImageView ivo3;
    private ImageView ivo4;
    private ImageView ivo5;
    private ImageView ivo6;
    private ImageView ivo7;
    private ImageView ivo8;
    private ImageView ivo9;
    private ImageView ivo10;
    private ImageView ivo11;
    private ImageView ivo12;
    private ImageView ivo13;
    private ImageView ivo14;
    private ImageView ivo15;

    private Button buttonEndQuiz;
    private long buttonLastTimeClick = 0;

    private long userid;

    ArrayList<Integer> listaColores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_just_now_quiz);

        listaColores = new ArrayList<>();

        ArrayList<Integer> colores = new ArrayList();

        colores.add(R.drawable.justnow_blue);
        colores.add(R.drawable.justnow_green);
        colores.add(R.drawable.justnow_red);
        colores.add(R.drawable.justnow_yellow);
        colores.add(R.drawable.justnow_gray);
        colores.add(R.drawable.justnow_brown);
        colores.add(R.drawable.justnow_black);
        colores.add(R.drawable.justnow_purple);

        Collections.shuffle(colores);

        for (int index = 0; index < colores.size(); index++) {
            listaColores.add(colores.get(index));
        }

        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);
        listaColores.add(R.drawable.justnow_placeholder);

        ivo0 = findViewById(R.id.imageviewjncolor0);
        ivo1 = findViewById(R.id.imageviewjncolor1);
        ivo2 = findViewById(R.id.imageviewjncolor2);
        ivo3 = findViewById(R.id.imageviewjncolor3);
        ivo4 = findViewById(R.id.imageviewjncolor4);
        ivo5 = findViewById(R.id.imageviewjncolor5);
        ivo6 = findViewById(R.id.imageviewjncolor6);
        ivo7 = findViewById(R.id.imageviewjncolor7);
        ivo8 = findViewById(R.id.imageviewjncolor8);
        ivo9 = findViewById(R.id.imageviewjncolor9);
        ivo10 = findViewById(R.id.imageviewjncolor10);
        ivo11 = findViewById(R.id.imageviewjncolor11);
        ivo12 = findViewById(R.id.imageviewjncolor12);
        ivo13 = findViewById(R.id.imageviewjncolor13);
        ivo14 = findViewById(R.id.imageviewjncolor14);
        ivo15 = findViewById(R.id.imageviewjncolor15);

        buttonEndQuiz = findViewById(R.id.buttonEndQuiz);

        ivo0.setOnDragListener(dragListener);
        ivo1.setOnDragListener(dragListener);
        ivo2.setOnDragListener(dragListener);
        ivo3.setOnDragListener(dragListener);
        ivo4.setOnDragListener(dragListener);
        ivo5.setOnDragListener(dragListener);
        ivo6.setOnDragListener(dragListener);
        ivo7.setOnDragListener(dragListener);
        ivo8.setOnDragListener(dragListener);
        ivo9.setOnDragListener(dragListener);
        ivo10.setOnDragListener(dragListener);
        ivo11.setOnDragListener(dragListener);
        ivo12.setOnDragListener(dragListener);
        ivo13.setOnDragListener(dragListener);
        ivo14.setOnDragListener(dragListener);
        ivo15.setOnDragListener(dragListener);

        ivo0.setOnLongClickListener(new ImageViewLongClickListener(ivo0));
        ivo1.setOnLongClickListener(new ImageViewLongClickListener(ivo1));
        ivo2.setOnLongClickListener(new ImageViewLongClickListener(ivo2));
        ivo3.setOnLongClickListener(new ImageViewLongClickListener(ivo3));
        ivo4.setOnLongClickListener(new ImageViewLongClickListener(ivo4));
        ivo5.setOnLongClickListener(new ImageViewLongClickListener(ivo5));
        ivo6.setOnLongClickListener(new ImageViewLongClickListener(ivo6));
        ivo7.setOnLongClickListener(new ImageViewLongClickListener(ivo7));
        ivo8.setOnLongClickListener(new ImageViewLongClickListener(ivo8));
        ivo9.setOnLongClickListener(new ImageViewLongClickListener(ivo9));
        ivo10.setOnLongClickListener(new ImageViewLongClickListener(ivo10));
        ivo11.setOnLongClickListener(new ImageViewLongClickListener(ivo11));
        ivo12.setOnLongClickListener(new ImageViewLongClickListener(ivo12));
        ivo13.setOnLongClickListener(new ImageViewLongClickListener(ivo13));
        ivo14.setOnLongClickListener(new ImageViewLongClickListener(ivo14));
        ivo15.setOnLongClickListener(new ImageViewLongClickListener(ivo15));

        buttonEndQuiz.setOnClickListener(clickContinuarListener);

        cargarImagenes();

        userid = SharedPreferenceUtils.getInstance(this).getLongValue(this.getString(R.string.user_id), 0);
    }

    private View.OnClickListener clickContinuarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
            buttonLastTimeClick = SystemClock.elapsedRealtime();

            ArrayList<String> lista = new ArrayList<>();

            for (int index = 8; index < listaColores.size(); index++) {
                switch (listaColores.get(index)) {
                    case R.drawable.justnow_blue: {
                        lista.add("1");
                        break;
                    }
                    case R.drawable.justnow_black: {
                        lista.add("2");
                        break;
                    }
                    case R.drawable.justnow_brown: {
                        lista.add("3");
                        break;
                    }
                    case R.drawable.justnow_gray: {
                        lista.add("4");
                        break;
                    }
                    case R.drawable.justnow_green: {
                        lista.add("5");
                        break;
                    }
                    case R.drawable.justnow_purple: {
                        lista.add("6");
                        break;
                    }
                    case R.drawable.justnow_red: {
                        lista.add("7");
                        break;
                    }
                    case R.drawable.justnow_yellow: {
                        lista.add("8");
                        break;
                    }
                }
            }
            if (lista.size() == 8) {
                new PutJustNowTask(JustNowQuizActivity.this, userid, lista).execute("");
            } else {
                Toasty.info(JustNowQuizActivity.this, "Acomoda todos los colores en la parte inferior", Toast.LENGTH_LONG, true).show();
            }
        }
    };

    @Override
    public void OnTaskJustNowQuiz() {
        //Toasty.success(JustNowQuizActivity.this, "Guardado", Toast.LENGTH_LONG, true).show();

        Intent i;
        i = new Intent(JustNowQuizActivity.this, MainActivity.class);
        startActivity(i);
    }

    private View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED: {
                    return true;
                }
                case DragEvent.ACTION_DRAG_ENTERED: {
                    return true;
                }
                case DragEvent.ACTION_DRAG_LOCATION: {
                    return true;
                }
                case DragEvent.ACTION_DRAG_EXITED: {
                    return true;
                }
                case DragEvent.ACTION_DROP: {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    int origen = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                    int destino = Integer.parseInt(v.getTag().toString());

                    Collections.swap(listaColores, destino, origen);

                    cargarImagenes();

                    //Log.d("DRAG origen", origen + "");
                    //Log.d("DRAG destino", destino + "");
                    //Log.d("DRAG", "DROP");

                    return true;
                }
                case DragEvent.ACTION_DRAG_ENDED: {
                    return true;
                }
                default: {
                    break;
                }
            }
            return false;
        }
    };

    class ImageViewLongClickListener implements View.OnLongClickListener {
        private ImageView iv;

        ImageViewLongClickListener(ImageView v) {
            iv = v;
        }

        @Override
        public boolean onLongClick(View v) {
            ClipData d = ClipData.newPlainText("value", iv.getTag().toString());
            iv.startDrag(d, new View.DragShadowBuilder(v), null, 0);
            return true;
        }
    }

    private final void cargarImagenes() {
        ivo0.setBackground(ContextCompat.getDrawable(this, listaColores.get(0)));
        ivo1.setBackground(ContextCompat.getDrawable(this, listaColores.get(1)));
        ivo2.setBackground(ContextCompat.getDrawable(this, listaColores.get(2)));
        ivo3.setBackground(ContextCompat.getDrawable(this, listaColores.get(3)));
        ivo4.setBackground(ContextCompat.getDrawable(this, listaColores.get(4)));
        ivo5.setBackground(ContextCompat.getDrawable(this, listaColores.get(5)));
        ivo6.setBackground(ContextCompat.getDrawable(this, listaColores.get(6)));
        ivo7.setBackground(ContextCompat.getDrawable(this, listaColores.get(7)));
        ivo8.setBackground(ContextCompat.getDrawable(this, listaColores.get(8)));
        ivo9.setBackground(ContextCompat.getDrawable(this, listaColores.get(9)));
        ivo10.setBackground(ContextCompat.getDrawable(this, listaColores.get(10)));
        ivo11.setBackground(ContextCompat.getDrawable(this, listaColores.get(11)));
        ivo12.setBackground(ContextCompat.getDrawable(this, listaColores.get(12)));
        ivo13.setBackground(ContextCompat.getDrawable(this, listaColores.get(13)));
        ivo14.setBackground(ContextCompat.getDrawable(this, listaColores.get(14)));
        ivo15.setBackground(ContextCompat.getDrawable(this, listaColores.get(15)));
    }
}


