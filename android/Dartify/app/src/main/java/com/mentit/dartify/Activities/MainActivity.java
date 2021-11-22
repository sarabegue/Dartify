package com.mentit.dartify.Activities;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mentit.dartify.Adapters.PagerAdapter;
import com.mentit.dartify.Fragments.ChatListFragment;
import com.mentit.dartify.Fragments.FavoriteFragment;
import com.mentit.dartify.Fragments.HomeFragment;
import com.mentit.dartify.Fragments.MenuFragment;
import com.mentit.dartify.Fragments.NotificationFragment;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.NotificationCard;
import com.mentit.dartify.Models.POJO.User.Usuario;
import com.mentit.dartify.Models.PerfilCard;
import com.mentit.dartify.Models.ViewModel.MatchCardViewModel;
import com.mentit.dartify.Models.ViewModel.MensajeChatViewModel;
import com.mentit.dartify.Models.ViewModel.NotificationCardViewModel;
import com.mentit.dartify.Models.ViewModel.PerfilCardViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.Notification.PutNotificationDeviceUserTask;
import com.mentit.dartify.HelperClasses.CustomViewPager;
import com.mentit.dartify.Tasks.User.GetUserProfileTask;

import es.dmoral.toasty.Toasty;

import android.os.Handler;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
        MenuFragment.OnFragmentMenuListener,
        FavoriteFragment.DataListener,
        HomeFragment.DataListener,
        NotificationFragment.DataListener,
        ChatListFragment.DataListener,
        GetUserProfileTask.OnTaskCompleted {

    private NotificationCardViewModel nviewmodel;
    private MensajeChatViewModel cviewmodel;
    private MatchCardViewModel mviewmodel;
    private PerfilCardViewModel pviewmodel;

    private CustomViewPager viewpager;
    private TabLayout tablayout;
    private Bundle b;
    private Context context;
    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        context = this;

        tablayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);
        viewpager.setPagingEnabled(false);

        tablayout.addTab(tablayout.newTab().setText("0").setCustomView(R.layout.tablayout_main_tab));
        tablayout.addTab(tablayout.newTab().setText("1").setCustomView(R.layout.tablayout_main_tab));
        tablayout.addTab(tablayout.newTab().setText("2").setCustomView(R.layout.tablayout_main_tab));
        tablayout.addTab(tablayout.newTab().setText("3").setCustomView(R.layout.tablayout_main_tab));
        tablayout.addTab(tablayout.newTab().setText("4").setCustomView(R.layout.tablayout_main_tab));

        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.addOnTabSelectedListener(tabSelectedListener);

        TabLayout.Tab t = tablayout.getTabAt(2);
        setIconSet(-1, -1);

        PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager(), tablayout.getTabCount());

        viewpager.setAdapter(padapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        suscribeNotification();

        selectTabIndex(2);

        //handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //handleIntent(intent);
    }

    /*
    private void handleIntent(Intent intent) {
        Bundle x = intent.getExtras();
        String user_id = intent.getStringExtra("userid1");
        if (user_id != null)
            Log.d("TAG", user_id);
    }
    */

    @Override
    public void onResume() {
        super.onResume();

        //handleIntent(getIntent());

        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        if (userid == 0) {
            LoginManager.getInstance().logOut();
            onUserLogout();
        }

        cviewmodel = ViewModelProviders.of(this).get(MensajeChatViewModel.class);
        mviewmodel = ViewModelProviders.of(this).get(MatchCardViewModel.class);
        pviewmodel = ViewModelProviders.of(this).get(PerfilCardViewModel.class);

        nviewmodel = ViewModelProviders.of(this).get(NotificationCardViewModel.class);
        nviewmodel.getData(userid).observe(this, data -> {
            int sinleer = 0;
            for (NotificationCard d : data) {
                if (d.getLeido() == 0) {
                    sinleer++;
                }
            }
            refreshNotificationBadge(sinleer);
        });

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    private void selectTabIndex(final int index) {
        new Handler().postDelayed(() -> tablayout.getTabAt(index).select(), 100);
    }

    private void suscribeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));


            channelId = getString(R.string.messages_notification_channel_id);
            channelName = getString(R.string.messages_notification_channel_name);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                //Log.d("TAG", "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        //Log.d("TAG", "Subscribing to topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_notification_channel_id))
                .addOnCompleteListener(task -> {
                    String msg = getString(R.string.msg_subscribed);
                    if (!task.isSuccessful()) {
                        msg = getString(R.string.msg_subscribe_failed);
                    }
                    //Log.d("TAG", msg);
                });

        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.messages_notification_channel_id))
                .addOnCompleteListener(task -> {
                    String msg = getString(R.string.msg_subscribed);
                    if (!task.isSuccessful()) {
                        msg = getString(R.string.msg_subscribe_failed);
                    }
                    //Log.d("TAG", msg);
                });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d("DEVICE NOTIFICATION ID", msg);
                        int numplataforma = 1; //ANDROID
                        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);
                        new PutNotificationDeviceUserTask(context, userid, token, numplataforma).execute("");
                    }
                });

    }

    private void setIconSet(int index, int r) {
        setIcon(0, R.drawable.tab_white_user);
        setIcon(1, R.drawable.tab_white_fav);
        setIcon(2, R.drawable.tab_white_home);
        setIcon(3, R.drawable.tab_white_chat);
        setIcon(4, R.drawable.tab_white_not);

        if (index >= 0 && r >= 0) {
            setIcon(index, r);
        }
    }

    private void setIcon(int index, int res) {
        TabLayout.Tab t;
        View v;
        ImageView i;

        t = tablayout.getTabAt(index);
        v = t.getCustomView();
        i = v.findViewById(R.id.tab_icon);
        i.setBackgroundResource(res);

        //colocar layout de notificacion
        if (index == 4) {
            TextView tx = v.findViewById(R.id.badge_text);
            tx.setBackgroundResource(R.drawable.rounded_notification);
        }
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int posicion = tab.getPosition();
            viewpager.setCurrentItem(posicion);

            switch (posicion) {
                case 0: {
                    setIconSet(posicion, R.drawable.tab_green_user);
                    break;
                }
                case 1: {
                    setIconSet(posicion, R.drawable.tab_green_fav);
                    break;
                }
                case 2: {
                    setIconSet(posicion, R.drawable.tab_green_home);
                    break;
                }
                case 3: {
                    setIconSet(posicion, R.drawable.tab_green_chat);
                    break;
                }
                case 4: {
                    setIconSet(posicion, R.drawable.tab_green_not);
                    break;
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    @Override
    public void onUserLogout() {
        Intent i = new Intent(this, FacebookLoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onAvatarProfile() {
        Intent i = new Intent(this, PhotosetActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onUserProfile() {
        Intent i;
        i = new Intent(this, ConfigureProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onUserQuimica() {
        Intent i;
        i = new Intent(this, ChemistryActivity.class);
        startActivity(i);
    }

    @Override
    public void onUserMuro() {
        Toasty.success(this, "Muro", Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onUserJustoAhora() {
        Intent i;
        i = new Intent(this, JustNowActivity.class);
        startActivity(i);
    }

    @Override
    public void onUserFiltro() {
        Toasty.success(this, "Filtro", Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onUserBloqueados() {
        Toasty.success(this, "Bloqueados", Toast.LENGTH_SHORT, true).show();
        Intent i;
        i = new Intent(this, BlockedUsersActivity.class);
        startActivity(i);
    }

    @Override
    public void onUserStore() {
        Toasty.success(this, "Tienda", Toast.LENGTH_SHORT, true).show();
        Intent i;
        i = new Intent(this, StoreActivity.class);

        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onUserCancel() {
        Toasty.success(this, "Cancelar", Toast.LENGTH_SHORT, true).show();
    }

    public void refreshNotificationBadge(int conteo) {
        TabLayout.Tab tab;
        tab = tablayout.getTabAt(4);
        View tabView = tab.getCustomView();
        TextView badgeText = tabView.findViewById(R.id.badge_text);

        badgeText.setVisibility(View.VISIBLE);
        if (conteo == 0) {
            badgeText.setVisibility(View.GONE);
        } else if (conteo <= 100) {
            badgeText.setText(" " + conteo + " ");
        } else {
            badgeText.setText(" * ");
        }
    }

    @Override
    public void sendFavoriteData(long id) {
        abrirPerfil(id);
    }

    @Override
    public void sendProfileData(long id) {
        abrirPerfil(id);
    }

    private void abrirPerfil(long id) {
        Intent i;
        i = new Intent(this, ProfileActivity.class);

        b = new Bundle();
        b.putLong("id", id);

        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void sendChatRequest(String chatRoom, long userid2) {
        startChat(chatRoom, userid2);
    }

    @Override
    public void sendNotificationClickData(String chatRoom, long userid2) {
        startChat(chatRoom, userid2);
    }

    public void clickSticker(View v) {
        for (int index = 0; index < getSupportFragmentManager().getFragments().size(); index++) {
            String fname = (getSupportFragmentManager().getFragments().get(index)).getClass().getName();
            if (fname.contains(".HomeFragment")) {
                HomeFragment fragment = (HomeFragment) getSupportFragmentManager().getFragments().get(index);
                fragment.clickSticker(v);
                continue;
            }
        }
    }

    private void startChat(String chatRoom, long userid2) {
        Intent i;
        i = new Intent(this, ChatActivity.class);

        b = new Bundle();
        b.putLong("userid2", userid2);

        mviewmodel.getMatchData(userid2);

        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void OnTaskCompletedProfile(JSONObject datos) {
        try {
            Usuario u = new Usuario(datos);
            PerfilCard p = new PerfilCard(u.getId());
            p.setStrfirstname(u.getFirstname());
            p.setStremail(u.getEmail());
            p.setDatefechanacimiento(u.getFechaNacimiento());
            p.setStrgender(u.getGender());
            p.setUbicacion(u.getUbicacion());
            p.setPrefer(u.getPrefer());
            p.setMinedad(u.getMinedad());
            p.setMaxedad(u.getMaxedad());
            p.setFacebookid(u.getFacebookId());
            p.setWeight(u.getWeight());
            p.setHeight(u.getHeight());
            p.setSkin(u.getSkin());
            p.setHair(u.getHair());
            p.setHaircolor(u.getHairColor());
            p.setRace(u.getRace());
            p.setOccupation(u.getOccupation());
            p.setScholarship(u.getScholarship());
            p.setAlcohol(u.getAlcohol());
            p.setSmoke(u.getSmoke());
            p.setSpirituality(u.getSpirituality());
            p.setSpirituality(u.getSpirituality());
            p.setCivilstate(u.getCivilState());
            p.setSiblings(u.getSiblings());

            pviewmodel.insert(p);
        } catch (Exception w) {
        }
    }
}