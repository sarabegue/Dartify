package com.mentit.dartify.Activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.HelperClasses.UserUtils;
import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.Models.ViewModel.FotoUsuarioViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.User.PutOrderFavoritePhotosTask;
import com.mentit.dartify.util.Valores;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhotosetActivity extends AppCompatActivity implements PutOrderFavoritePhotosTask.OnTaskCompleted {
    private FotoUsuarioViewModel fviewmodel;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView iv5;
    private long userid;

    private Button buttonGuardar;
    private long buttonLastTimeClick = 0;

    private ArrayList<Foto> listaFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photoset);

        iv1 = findViewById(R.id.imageview1);
        iv2 = findViewById(R.id.imageview2);
        iv3 = findViewById(R.id.imageview3);
        iv4 = findViewById(R.id.imageview4);
        iv5 = findViewById(R.id.imageview5);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        iv1.setOnLongClickListener(new ImageViewLongClickListener(iv1));
        iv2.setOnLongClickListener(new ImageViewLongClickListener(iv2));
        iv3.setOnLongClickListener(new ImageViewLongClickListener(iv3));
        iv4.setOnLongClickListener(new ImageViewLongClickListener(iv4));
        iv5.setOnLongClickListener(new ImageViewLongClickListener(iv5));

        iv1.setOnDragListener(dragListener);
        iv2.setOnDragListener(dragListener);
        iv3.setOnDragListener(dragListener);
        iv4.setOnDragListener(dragListener);
        iv5.setOnDragListener(dragListener);

        iv1.setOnClickListener(new ImageViewClickListener(iv1));
        iv2.setOnClickListener(new ImageViewClickListener(iv2));
        iv3.setOnClickListener(new ImageViewClickListener(iv3));
        iv4.setOnClickListener(new ImageViewClickListener(iv4));
        iv5.setOnClickListener(new ImageViewClickListener(iv5));

        iv1.setTag(R.string.tag, 0);
        iv2.setTag(R.string.tag, 1);
        iv3.setTag(R.string.tag, 2);
        iv4.setTag(R.string.tag, 3);
        iv5.setTag(R.string.tag, 4);

        buttonGuardar.setOnClickListener(clickGuardar);

        userid = SharedPreferenceUtils.getInstance(this).getLongValue(this.getString(R.string.user_id), 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        fviewmodel = ViewModelProviders.of(this).get(FotoUsuarioViewModel.class);

        fviewmodel.getData(userid).observe(this, data -> {
            cargarImagenes(data);
        });
    }

    private View.OnClickListener clickGuardar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Bloqueo de botón
            if (SystemClock.elapsedRealtime() - buttonLastTimeClick < Valores.TIEMPO_LONG) return;
            buttonLastTimeClick = SystemClock.elapsedRealtime();

            new PutOrderFavoritePhotosTask(PhotosetActivity.this, userid, listaFotos).execute("");
        }
    };

    @Override
    public void OnTaskCompletedOrderFavoritePhotos() {
        fviewmodel.getData(userid);

        Intent i;
        i = new Intent(this, MainActivity.class);

        startActivity(i);
        finish();
    }

    //@Override
    public void OnTaskCompletedPhotos(JSONObject datos) {

        UserUtils utils = new UserUtils();
        utils.storeUserPhotos(datos, this);

        Intent i = new Intent(this, MainActivity.class);
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
                    int destino = Integer.parseInt(v.getTag(R.string.tag).toString());

                    for (int index = 0; index < listaFotos.size(); index++) {
                        Log.d("ORDEN PHOTOSET", listaFotos.get(index).getID() + "");
                    }

                    fviewmodel.updatePosition(listaFotos.get(destino).getID() + "", listaFotos.get(origen).getOrden() + "");
                    fviewmodel.updatePosition(listaFotos.get(origen).getID() + "", listaFotos.get(destino).getOrden() + "");

                    Collections.swap(listaFotos, destino, origen);
                    Log.d("DRAG origen", origen + "");
                    Log.d("DRAG destino", destino + "");
                    Log.d("DRAG", "DROP");

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
            ClipData d = ClipData.newPlainText("value", iv.getTag(R.string.tag).toString());
            iv.startDrag(d, new View.DragShadowBuilder(v), null, 0);
            return true;
        }
    }

    class ImageViewClickListener implements View.OnClickListener {
        private ImageView iv;

        ImageViewClickListener(ImageView v) {
            iv = v;
        }

        @Override
        public void onClick(View v) {
            Intent i;
                                /*
            Toasty.success(PhotosetActivity.this, "CLICK EN " + v.getTag(R.string.t0).toString(), Toast.LENGTH_SHORT).show();
            Log.d("click", v.getTag(R.string.t0).toString());
            i = new Intent(PhotosetActivity.this, GalleryActivity.class);

            startActivity(i);      */
        }
    }

    private final void cargarImagenes(List<FotoUsuario> dat) {
        int sizeb = 830;
        int sizes = 400;

        listaFotos = new ArrayList<>();

        for (int index = 0; index < dat.size(); index++) {
            FotoUsuario fu = dat.get(index);
            listaFotos.add(new Foto(fu.getId(), fu.getResource1(), fu.getNumorden()));
        }

        ColorDrawable placeholder = new ColorDrawable(Color.GRAY);

        try {
            Glide
                    .with(this)
                    .load(listaFotos.get(0).getSource())
                    .apply(new RequestOptions()
                            .placeholder(placeholder)
                            .fitCenter()
                            .centerCrop()
                    )
                    .into(iv1);
            Glide
                    .with(this)
                    .load(listaFotos.get(1).getSource())
                    .apply(new RequestOptions()
                            .fitCenter()
                            .centerCrop()
                    )
                    .into(iv2);
            Glide
                    .with(this)
                    .load(listaFotos.get(2).getSource())
                    .apply(new RequestOptions()
                            .placeholder(placeholder)
                            .fitCenter()
                            .centerCrop()
                    )
                    .into(iv3);
            Glide
                    .with(this)
                    .load(listaFotos.get(3).getSource())
                    .apply(new RequestOptions()
                            .placeholder(placeholder)
                            .fitCenter()
                            .centerCrop()
                    )
                    .into(iv4);
            Glide
                    .with(this)
                    .load(listaFotos.get(4).getSource())
                    .apply(new RequestOptions()
                            .placeholder(placeholder)
                            .fitCenter()
                            .centerCrop()
                    )
                    .into(iv5);
        } catch (Exception w) {
            Log.d("exception", w.getMessage());
        }
    }
}
