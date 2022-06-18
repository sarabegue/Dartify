package com.mentit.dartify.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Adapters.EstadoAdapter;
import com.mentit.dartify.Adapters.ProfileAdapter;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.EstadoCard;
import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.Models.PerfilCard;
import com.mentit.dartify.Models.ViewModel.EstadoCardViewModel;
import com.mentit.dartify.Models.ViewModel.FavoriteCardViewModel;
import com.mentit.dartify.Models.ViewModel.FotoUsuarioViewModel;
import com.mentit.dartify.Models.ViewModel.MensajeChatViewModel;
import com.mentit.dartify.Models.ViewModel.PerfilCardViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Match.PutUserBlockedReportStatusTask;
import com.mentit.dartify.util.FormatUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;

public class ProfileActivity extends AppCompatActivity implements PutUserBlockedReportStatusTask.OnTaskCompleted {
    PerfilCard perfil = null;
    private EstadoCardViewModel eviewmodel;
    private FotoUsuarioViewModel fviewmodel;
    private FavoriteCardViewModel favviewmodel;
    private MensajeChatViewModel mviewmodel;

    private RecyclerView recyclerEstados;
    private RecyclerView recyclerFotos;

    private EstadoAdapter nAdapterEstados;
    private ProfileAdapter nAdapterFotos;

    private RecyclerView.LayoutManager gLayoutManagerEstados;
    private RecyclerView.LayoutManager gLayoutManagerFotos;

    private ImageView chatButton;
    private ImageView stickerImageButton;
    private ImageView favoriteImageButton;
    private ImageButton blockButton;
    private Context context;

    private List<EstadoCard> listaEstados;
    private List<FotoUsuario> listaFotos;
    private List<FavoriteCard> favorites;
    private LongPressPopup popupA;

    private long userid1;
    private long userid2;
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        context = this;
        listaEstados = new ArrayList<>();
        listaFotos = new ArrayList<>();

        recyclerFotos = findViewById(R.id.recyclerViewProfile);
        recyclerEstados = findViewById(R.id.recyclerViewEstados);
        chatButton = findViewById(R.id.chatButton);
        stickerImageButton = findViewById(R.id.imageViewSticker);
        favoriteImageButton = findViewById(R.id.imageViewFavoritePerson);
        blockButton = findViewById(R.id.blockButton);

        userid1 = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);
        userid2 = getIntent().getExtras().getLong("id");

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerFotos);

        gLayoutManagerFotos = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        gLayoutManagerEstados = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        nAdapterEstados = new EstadoAdapter(listaEstados, R.layout.cardview_elemento, nClickListenerStatus, this);
        nAdapterFotos = new ProfileAdapter(listaFotos, R.layout.cardview_profile, nClickListenerProfile, this);

        recyclerFotos.setHasFixedSize(true);
        recyclerFotos.setLayoutManager(gLayoutManagerFotos);
        recyclerFotos.setAdapter(nAdapterFotos);

        recyclerEstados.setHasFixedSize(false);
        recyclerEstados.setLayoutManager(gLayoutManagerEstados);
        recyclerEstados.setAdapter(nAdapterEstados);

        popupA = new LongPressPopupBuilder(context)
                .setTarget(stickerImageButton)
                .setPopupView(R.layout.popup_layout_stickers, (popupTag, root) -> {

                })
                .setTag("popa")
                .build();

        popupA.register();

        chatButton.setOnClickListener(iniciarChat);
        favoriteImageButton.setOnClickListener(clickFavoriteListener);
        blockButton.setOnClickListener(bloquearUsuario);
    }

    @Override
    public void onResume() {
        super.onResume();

        mviewmodel = ViewModelProviders.of(this).get(MensajeChatViewModel.class);
        eviewmodel = ViewModelProviders.of(this).get(EstadoCardViewModel.class);
        fviewmodel = ViewModelProviders.of(this).get(FotoUsuarioViewModel.class);
        favviewmodel = ViewModelProviders.of(this).get(FavoriteCardViewModel.class);

        eviewmodel.getData(userid2).observe(this, data -> nAdapterEstados.setData(data));

        fviewmodel.getData(userid2).observe(this, data -> nAdapterFotos.setData(data));
        favviewmodel.getData(userid1).observe(this, data -> {
            favorites = data;
            actualizarStatus();
        });
        new GetPerfilAsyncTask(this, userid2).execute();
    }

    private void actualizarStatus() {
        int s = 0;

        for (FavoriteCard f : favorites) {
            if (userid2 == f.getLongUserId2()) {
                s = 1;
                continue;
            }
        }
        refreshStatus(s);
    }

    private View.OnClickListener clickFavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            status = (status == 1) ? 0 : 1;

            if (status == 0) {
                favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_disabled_star));
                favviewmodel.deleteFavorite(userid1, userid2);
            }
            if (status == 1) {
                favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_enabled_star));
                favviewmodel.addFavorite(userid1, userid2);
            }
        }
    };

    private View.OnClickListener bloquearUsuario = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View mView = inflater.inflate(R.layout.layout_blockuser, null);
        final EditText textoreporte = mView.findViewById(R.id.textreporte);

        builder.setTitle(R.string.blockusertitle);
        builder.setView(mView);
        builder.setPositiveButton(R.string.blockuserbuttonpositive, (dialog, which) -> {
            new PutUserBlockedReportStatusTask(context, userid1, userid2, 1, textoreporte.getText().toString()).execute("");
        });

        builder.setNegativeButton(R.string.negativeButton, (dialog, which) -> {
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    };

    private ProfileAdapter.OnItemClickListener nClickListenerProfile = (id, position) -> {
        //callBack.sendFavoriteData(id);
    };

    private EstadoAdapter.OnItemClickListener nClickListenerStatus = (id, position) -> {
        //callBack.sendFavoriteData(id);
    };

    private View.OnClickListener iniciarChat = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userid2", userid2);
            startActivity(intent);
        }
    };

    public void refreshStatus(int s) {
        this.status = s;
        if (this.status == 1) {
            favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_enabled_star));
        } else {
            favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_disabled_star));
        }
    }

    public void clickSticker(View v) {
        popupA.dismissNow();
        mviewmodel.guardarMensaje(FormatUtil.getChatRoom(userid1, userid2), userid1, userid2, v.getTag().toString(), 2);
    }

    public void cargarDatosUsuario(PerfilCard p) {
        TableLayout tableLayoutPerfil = findViewById(R.id.tableLayoutPerfil);
        tableLayoutPerfil.setVisibility(View.GONE);
        return;
        /*

        if (p != null) {
            perfil = p;

            if (p.getCivilstate() == 0 &&
                    p.getHeight() == 0 &&
                    p.getWeight() == 0 &&
                    p.getSkin() == 0 &&
                    p.getHair() == 0 &&
                    p.getHaircolor() == 0 &&
                    p.getRace() == 0 &&
                    p.getOccupation() == 0 &&
                    p.getScholarship() == 0 &&
                    p.getAlcohol() == 0 &&
                    p.getSmoke() == 0 &&
                    p.getSpirituality() == 0 &&
                    p.getSiblings() == 0
            ) {
                //TableLayout tableLayoutPerfil = findViewById(R.id.tableLayoutPerfil);
                tableLayoutPerfil.setVisibility(View.GONE);
                return;
            }

            TableRow trEstado = findViewById(R.id.trEstado);
            TextView tvEstado = findViewById(R.id.tvCivilEstado);
            switch (p.getCivilstate()) {
                case 0: {
                    trEstado.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvEstado.setText("Soltero");
                    break;
                }
                case 2: {
                    tvEstado.setText("Casado");
                    break;
                }
                case 3: {
                    tvEstado.setText("Divorciado");
                    break;
                }
            }

            TableRow trestatura = findViewById(R.id.trEstatura);
            TextView tvestatura = findViewById(R.id.tvEstatura);
            if (p.getHeight() > 0) {
                tvestatura.setText(p.getHeight() + " cm");
            } else {
                trestatura.setVisibility(View.GONE);
            }

            TableRow trpeso = findViewById(R.id.trPeso);
            TextView tvpeso = findViewById(R.id.tvPeso);
            if (p.getWeight() > 0) {
                tvpeso.setText(p.getWeight() + " kg");
            } else {
                trpeso.setVisibility(View.GONE);
            }

            TableRow trpiel = findViewById(R.id.trPiel);
            TextView tvpiel = findViewById(R.id.tvPiel);
            switch (p.getSkin()) {
                case 0: {
                    trpiel.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvpiel.setText("Moreno");
                    break;
                }
                case 2: {
                    tvpiel.setText("Blanco");
                    break;
                }
            }

            TableRow trtipocabello = findViewById(R.id.trTipoCabello);
            TextView tvtipocabello = findViewById(R.id.tvTipoCabello);
            switch (p.getHair()) {
                case 0: {
                    trtipocabello.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvtipocabello.setText("Lacio");
                    break;
                }
                case 2: {
                    tvtipocabello.setText("Rizado");
                    break;
                }
                case 3: {
                    tvtipocabello.setText("Ondulado");
                    break;
                }
            }

            TableRow trcabello = findViewById(R.id.trCabello);
            TextView tvcabello = findViewById(R.id.tvCabello);
            switch (p.getHaircolor()) {
                case 0: {
                    trcabello.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvcabello.setText("Negro");
                    break;
                }
                case 2: {
                    tvcabello.setText("Castaño");
                    break;
                }
                case 3: {
                    tvcabello.setText("Rubio");
                    break;
                }
                case 4: {
                    tvcabello.setText("Rojizo");
                    break;
                }
            }

            TableRow trraza = findViewById(R.id.trRaza);
            TextView tvraza = findViewById(R.id.tvRaza);
            switch (p.getRace()) {
                case 0: {
                    trraza.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvraza.setText("Latino");
                    break;
                }
                case 2: {
                    tvraza.setText("Oriental");
                    break;
                }
                case 3: {
                    tvraza.setText("Chino");
                    break;
                }
            }

            TableRow trocupacion = findViewById(R.id.trOcupacion);
            TextView tvocupacion = findViewById(R.id.tvOcupacion);
            switch (p.getOccupation()) {
                case 0: {
                    trocupacion.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvocupacion.setText("Desempleado");
                    break;
                }
                case 2: {
                    tvocupacion.setText("Empresario");
                    break;
                }
                case 3: {
                    tvocupacion.setText("Ejecutivo");
                    break;
                }
                case 4: {
                    tvocupacion.setText("Presidente");
                    break;
                }
            }

            TableRow trescolaridad = findViewById(R.id.trEscolaridad);
            TextView tvescolaridad = findViewById(R.id.tvEscolaridad);
            switch (p.getScholarship()) {
                case 0: {
                    trescolaridad.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvescolaridad.setText("Secundaria o menor");
                    break;
                }
                case 2: {
                    tvescolaridad.setText("Medio superior");
                    break;
                }
                case 3: {
                    tvescolaridad.setText("Superior");
                    break;
                }
                case 4: {
                    tvescolaridad.setText("Posgrado");
                    break;
                }
            }

            TableRow tralcohol = findViewById(R.id.trAlcohol);
            TextView tvalcohol = findViewById(R.id.tvAlcohol);
            switch (p.getAlcohol()) {
                case 0: {
                    tralcohol.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvalcohol.setText("No");
                    break;
                }
                case 2: {
                    tvalcohol.setText("Ocasionalmente");
                    break;
                }
                case 3: {
                    tvalcohol.setText("Sí");
                    break;
                }
            }

            TableRow trfumo = findViewById(R.id.trFumo);
            TextView tvfumo = findViewById(R.id.tvFumo);
            switch (p.getSmoke()) {
                case 0: {
                    trfumo.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvfumo.setText("No");
                    break;
                }
                case 2: {
                    tvfumo.setText("Ocasionalmente");
                    break;
                }
                case 3: {
                    tvfumo.setText("Sí");
                    break;
                }
            }

            TableRow trespiritualidad = findViewById(R.id.trEspiritualidad);
            TextView tvespiritualidad = findViewById(R.id.tvEspiritualidad);
            switch (p.getSpirituality()) {
                case 0: {
                    trespiritualidad.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvespiritualidad.setText("Católica");
                    break;
                }
                case 2: {
                    tvespiritualidad.setText("Cristianismo");
                    break;
                }
                case 3: {
                    tvespiritualidad.setText("Budista");
                    break;
                }
                case 4: {
                    tvespiritualidad.setText("Testigo de Jehová");
                    break;
                }
                case 5: {
                    tvespiritualidad.setText("Mormona");
                    break;
                }
                case 6: {
                    tvespiritualidad.setText("Cábala");
                    break;
                }
                case 7: {
                    tvespiritualidad.setText("Judaísmo");
                    break;
                }
                case 8: {
                    tvespiritualidad.setText("Protestante");
                    break;
                }
                case 9: {
                    tvespiritualidad.setText("Espiritual pero no religioso");
                    break;
                }
                case 10: {
                    tvespiritualidad.setText("Ateo");
                    break;
                }
            }

            TableRow trhijos = findViewById(R.id.trHijos);
            TextView tvhijos = findViewById(R.id.tvHijos);
            switch (p.getSiblings()) {
                case 0: {
                    trhijos.setVisibility(View.GONE);
                    break;
                }
                case 1: {
                    tvhijos.setText("No");
                    break;
                }
                case 2: {
                    tvhijos.setText("Sí");
                    break;
                }
            }
        }

        */
    }

    @Override
    public void OnTaskCompletedPutBlockedReportStatus() {
        finish();
    }

    private static class GetPerfilAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<AppCompatActivity> weakActivity;
        private PerfilCardViewModel mcvm;
        private PerfilCard dato;
        private long userid;

        public GetPerfilAsyncTask(AppCompatActivity activity, long userid) {
            weakActivity = new WeakReference<>(activity);
            mcvm = ViewModelProviders.of(activity).get(PerfilCardViewModel.class);
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            dato = mcvm.getPerfil(userid);
            return null;
        }

        @Override
        protected void onPostExecute(Void x) {
            Activity activity = weakActivity.get();

            if (activity == null) return;

            ((ProfileActivity) activity).cargarDatosUsuario(dato);
        }
    }
}