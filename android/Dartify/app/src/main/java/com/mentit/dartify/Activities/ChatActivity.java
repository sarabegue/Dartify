package com.mentit.dartify.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.Adapters.MensajeChatAdapter;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.HelperClasses.WhiteBorderTransformation;
import com.mentit.dartify.Models.MatchCard;
import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.Models.ViewModel.MatchCardViewModel;
import com.mentit.dartify.Models.ViewModel.MensajeChatViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Match.PutUserBlockedReportStatusTask;
import com.mentit.dartify.util.FormatUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;

public class ChatActivity extends AppCompatActivity implements PutUserBlockedReportStatusTask.OnTaskCompleted {
    private MatchCardViewModel matchCardViewModel;
    private MensajeChatViewModel mviewmodel;
    private RecyclerView recycler;
    private MensajeChatAdapter nAdapter;
    private RecyclerView.LayoutManager gLayoutManager;
    private TextView textFirstname;
    private EditText textMessage;
    private Button sendButton;
    private ImageButton blockButton;
    private ImageView imageAvatar1;
    private ImageView stickerImageButton;

    private Context context;
    ArrayList<MensajeChat> messagelist = new ArrayList<>();

    private long userid1;
    private long userid2;
    private String chatRoom;

    private LongPressPopup popupA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chat);
        Bundle b = getIntent().getExtras();

        context = this;

        blockButton = findViewById(R.id.blockButton);
        imageAvatar1 = findViewById(R.id.imageViewAvatar1);
        textMessage = findViewById(R.id.textMessage);
        sendButton = findViewById(R.id.sendButton);
        textFirstname = findViewById(R.id.textviewFirstName);
        stickerImageButton = findViewById(R.id.imageViewSticker);

        recycler = findViewById(R.id.recyclerChat);
        gLayoutManager = new LinearLayoutManager(this);
        nAdapter = new MensajeChatAdapter(messagelist, nClickListener, this);

        recycler.setLayoutManager(gLayoutManager);
        recycler.setAdapter(nAdapter);

        userid1 = SharedPreferenceUtils.getInstance(context).getLongValue(this.getString(R.string.user_id), 0);
        userid2 = b.getLong("userid2", -1);
        chatRoom = FormatUtil.getChatRoom(userid1, userid2);

        sendButton.setOnClickListener(enviarMensaje);
        blockButton.setOnClickListener(bloquearUsuario);
        imageAvatar1.setOnClickListener(verPerfilUsuario);

        popupA = new LongPressPopupBuilder(context)
                .setTarget(stickerImageButton)
                .setPopupView(R.layout.popup_layout_stickers, (popupTag, root) -> Log.d("INFLADO", popupTag))
                .setTag("popa")
                .build();

        popupA.register();
        hideKeyboard();
    }

    @Override
    public void onResume() {
        super.onResume();

        int membresia = SharedPreferenceUtils.getInstance(context).getIntValue("membresia", 1);

        matchCardViewModel = ViewModelProviders.of(this).get(MatchCardViewModel.class);
        new GetUserAsyncTask(this, userid2).execute();
        matchCardViewModel.getDataCard(userid2, membresia).observe(this, data -> {
            new GetUserAsyncTask(this, userid2).execute();
        });

        mviewmodel = ViewModelProviders.of(this).get(MensajeChatViewModel.class);
        mviewmodel.getData(chatRoom).observe(this, data -> {
            nAdapter.setData(data);
            recycler.scrollToPosition(nAdapter.getItemCount() - 1);
        });

        mviewmodel.joinRoom(chatRoom);
    }

    private View.OnClickListener enviarMensaje = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = textMessage.getText().toString();
            if (msg.length() > 0) {
                mviewmodel.guardarMensaje(chatRoom, userid1, userid2, msg, 1);
                mviewmodel.descargarMensajes();
                textMessage.setText("");
                hideKeyboard();
            }
        }
    };

    private View.OnClickListener verPerfilUsuario = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
            i = new Intent(context, ProfileActivity.class);

            Bundle b = new Bundle();
            b.putLong("id", userid2);

            i.putExtras(b);
            startActivity(i);
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

    private MensajeChatAdapter.OnItemClickListener nClickListener = (id, position) -> Log.d("CLIC", id + "");

    public void clickSticker(View v) {
        popupA.dismissNow();

        mviewmodel.guardarMensaje(FormatUtil.getChatRoom(userid1, userid2), userid1, userid2, v.getTag().toString(), 2);
    }

    public void setLabels(MatchCard m) {
        if (((ChatActivity) context).isDestroyed()) return;

        if (m != null) {
            if (m.getResource1() != null) {

                Glide
                        .with(context)
                        .load(m.getResource1())
                        .apply(new RequestOptions()
                                .centerCrop()
                                .fitCenter()
                                .transforms(new WhiteBorderTransformation(Color.WHITE), new CircleCrop())
                        )
                        .into(imageAvatar1);
            }
            textFirstname.setText(m.getTexto1());
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void OnTaskCompletedPutBlockedReportStatus() {
        //Usuario bloqueado
        //Log.d("BLOQUEADO", "1");
        finish();
    }

    private static class GetUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<AppCompatActivity> weakActivity;
        private MatchCardViewModel mcvm;
        private MatchCard mc;
        private long userid;

        public GetUserAsyncTask(AppCompatActivity activity, long userid) {
            weakActivity = new WeakReference<>(activity);
            mcvm = ViewModelProviders.of(activity).get(MatchCardViewModel.class);
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mc = mcvm.getMatch(userid);
            return null;
        }

        @Override
        protected void onPostExecute(Void x) {
            Activity activity = weakActivity.get();

            if (activity == null) return;

            ((ChatActivity) activity).setLabels(mc);
        }
    }
}
