package com.mentit.dartify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Adapters.GalleryAdapter;
import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.Models.POJO.User.Usuario;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.User.DownloadFacebookImagesTask;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements DownloadFacebookImagesTask.OnTaskCompleted {
    private ArrayList<Foto> fotos;

    private RecyclerView recycler;
    private RecyclerView.Adapter gAdapter;
    private RecyclerView.LayoutManager gLayoutManager;

    private Usuario userLogueado;
    private Button buttonDownload;
    private long buttonLastTimeClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_gallery);

        Bundle b = getIntent().getExtras();
        userLogueado = (Usuario) b.getSerializable("usuario");

        recycler = findViewById(R.id.recyclerGallery);
        buttonDownload = findViewById(R.id.buttonActualizarImagenes);

        gLayoutManager = new LinearLayoutManager(this);
        gLayoutManager = new GridLayoutManager(this, 3);

        buttonDownload.setOnClickListener(clickDownload);
    }

    private View.OnClickListener clickDownload = v -> new DownloadFacebookImagesTask(GalleryActivity.this, null).execute("");

    private GalleryAdapter.OnItemClickListener gClickListener = new GalleryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String name, int position) {
            Intent i;

            i = new Intent(GalleryActivity.this, PhotosetActivity.class);

            i.putExtra("usuario", userLogueado);

            startActivity(i);
        }
    };

    public void DownloadCompleted() {
        gAdapter = new GalleryAdapter(userLogueado.getFotografias(), R.layout.gallery_item_layout, gClickListener);

        recycler.setLayoutManager(gLayoutManager);
        recycler.setAdapter(gAdapter);
    }

    @Override
    public void OnTaskCompleted(ArrayList listaFotosFacebook) {
        //Toasty.success(this, "OOOOOKK").show();
    }
}