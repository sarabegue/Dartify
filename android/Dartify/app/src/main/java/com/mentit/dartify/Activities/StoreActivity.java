package com.mentit.dartify.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mentit.dartify.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

public class StoreActivity extends AppCompatActivity {
    CarouselView c;
    int TOTAL_PAGES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_store);

        c = findViewById(R.id.carouselView);
        c.setPageCount(TOTAL_PAGES);

        c.setViewListener(vListener);
    }

    ViewListener vListener = position -> {
        View v = getLayoutInflater().inflate(R.layout.storeitem_layout, null);

        String title = "";
        String text = "";

        LinearLayout ll = v.findViewById(R.id.storeitem_slide);
        TextView titleview = v.findViewById(R.id.storeitem_title);
        TextView textview = v.findViewById(R.id.storeitem_text);
        ImageView imgview = v.findViewById(R.id.storeitem_image);

        int img = 0;
        int col = 0;

        switch (position) {
            case 0: {
                title = "¡Justo ahora!";
                text = "En donde encontrarás a personas que en el momento son compatibles";
                img = R.drawable.store_justnow;
                col = R.color.store1;
                break;
            }
            case 1: {
                title = "4 saludos al día";
                text = "Podrás mandar 4 saludos al día en lugar de solo 1";
                img = R.drawable.store_hand;
                col = R.color.store2;
                break;
            }
            case 2: {
                title = "Sin anuncios";
                text = "Pasa el tiempo que quieras dentro de la aplicación sin molestos anuncios";
                img = R.drawable.store_cross;
                col = R.color.store3;
                break;
            }
            case 3: {
                title = "Guardar a tus match en tu lista de favoritos";
                text = "Guarda tus match favoritos sin preocuparte en que desaparezcan cada 24 horas";
                img = R.drawable.store_star;
                col = R.color.store4;
                break;
            }
            case 4: {
                title = "Desbloquea 4 perfiles al día con filtro";
                text = "En donde encontrarás a más personas como tú";
                img = R.drawable.store_user;
                col = R.color.store5;
                break;
            }
        }

        imgview.setImageResource(img);
        titleview.setText(title);
        textview.setText(text);
        ll.setBackgroundResource(col);

        return v;
    };
}
