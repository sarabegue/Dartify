package com.mentit.dartify.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.HelperClasses.WhiteBorderTransformation;
import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.Models.ViewModel.EstadoCardViewModel;
import com.mentit.dartify.Models.ViewModel.FavoriteCardViewModel;
import com.mentit.dartify.Models.ViewModel.FotoUsuarioViewModel;
import com.mentit.dartify.Models.ViewModel.MensajeChatViewModel;
import com.mentit.dartify.Models.ViewModel.NotificationCardViewModel;
import com.mentit.dartify.Models.ViewModel.PerfilCardViewModel;
import com.mentit.dartify.Models.ViewModel.PersonaOcultaViewModel;
import com.mentit.dartify.R;


public class MenuFragment extends Fragment {
    private FotoUsuarioViewModel foviewmodel;

    OnFragmentMenuListener mCallBack;

    private LinearLayout linearLayoutProfile;
    private ImageView imageViewAvatar;
    private Button buttonQuimica;
    private Button buttonMyWall;
    private Button buttonJustNow;
    private Button buttonFilter;
    private Button buttonBlockedUsers;
    private Button buttonLogout;
    private Button buttonDeleteAccount;
    private TextView textviewFirstName;
    private Context context;
    private ImageView ivStore;
    private NotificationCardViewModel dcviewmodel;
    private MensajeChatViewModel mcviewmodel;
    private FavoriteCardViewModel fviewmodel;
    private EstadoCardViewModel eviewmodel;
    private PerfilCardViewModel pviewmodel;
    private PersonaOcultaViewModel poviewmodel;

    private long userid;

    public MenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        linearLayoutProfile = v.findViewById(R.id.linearLayoutProfile);
        imageViewAvatar = v.findViewById(R.id.imageViewAvatar);

        buttonQuimica = v.findViewById(R.id.buttonQuimica);
        buttonMyWall = v.findViewById(R.id.buttonMyWall);
        buttonJustNow = v.findViewById(R.id.buttonJustNow);
        buttonFilter = v.findViewById(R.id.buttonFilter);
        buttonBlockedUsers = v.findViewById(R.id.buttonBlockedUsers);
        buttonDeleteAccount = v.findViewById(R.id.buttonDeleteAccount);
        buttonLogout = v.findViewById(R.id.buttonLogout);
        textviewFirstName = v.findViewById(R.id.textviewFirstName);

        ivStore = v.findViewById(R.id.storeButton);

        textviewFirstName.setOnClickListener(clickUserProfile);
        imageViewAvatar.setOnClickListener(clickAvatarProfile);
        buttonQuimica.setOnClickListener(clickUserQuimica);
        buttonMyWall.setOnClickListener(clickUserMuro);
        buttonJustNow.setOnClickListener(clickUserJustoAhora);
        buttonFilter.setOnClickListener(clickUserFiltro);
        buttonBlockedUsers.setOnClickListener(clickUserBloqueados);
        buttonLogout.setOnClickListener(clickLogout);
        buttonDeleteAccount.setOnClickListener(clickUserCancel);

        //ivStore.setOnClickListener(clickUserStore);

        context = v.getContext();
        dcviewmodel = ViewModelProviders.of(this).get(NotificationCardViewModel.class);
        mcviewmodel = ViewModelProviders.of(this).get(MensajeChatViewModel.class);
        fviewmodel = ViewModelProviders.of(this).get(FavoriteCardViewModel.class);
        eviewmodel = ViewModelProviders.of(this).get(EstadoCardViewModel.class);
        foviewmodel = ViewModelProviders.of(this).get(FotoUsuarioViewModel.class);
        pviewmodel = ViewModelProviders.of(this).get(PerfilCardViewModel.class);
        poviewmodel = ViewModelProviders.of(this).get(PersonaOcultaViewModel.class);

        String name = SharedPreferenceUtils.getInstance(container.getContext()).getStringValue(container.getContext().getString(R.string.user_name), "");
        textviewFirstName.setText(name);
        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        foviewmodel = ViewModelProviders.of(this).get(FotoUsuarioViewModel.class);

        foviewmodel.getData(userid).observe(this, data -> {
            Log.d("AQUI", "");
            if (data.size() > 0) {
                FotoUsuario d = data.get(0);
                if (d.getResource1() != null) {
                    Glide
                            .with(getContext())
                            .load(d.getResource1())
                            .apply(new RequestOptions()
                                    .fitCenter()
                                    .transform(new WhiteBorderTransformation(Color.WHITE), new CircleCrop())
                            )
                            .into(imageViewAvatar);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMenuListener) {
            mCallBack = (OnFragmentMenuListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    private View.OnClickListener clickAvatarProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onAvatarProfile();
        }
    };

    private View.OnClickListener clickUserProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserProfile();
        }
    };

    private View.OnClickListener clickUserQuimica = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserQuimica();
        }
    };

    private View.OnClickListener clickUserMuro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserMuro();
        }
    };

    private View.OnClickListener clickUserJustoAhora = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserJustoAhora();
        }
    };

    private View.OnClickListener clickUserFiltro = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserFiltro();
        }
    };

    private View.OnClickListener clickUserStore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserStore();
        }
    };

    private View.OnClickListener clickUserBloqueados = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserBloqueados();
        }
    };

    private View.OnClickListener clickUserCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.onUserCancel();
        }
    };

    private View.OnClickListener clickLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dcviewmodel.deleteAll();
            mcviewmodel.deleteAll();
            fviewmodel.deleteAll();
            eviewmodel.deleteAll();
            pviewmodel.deleteAll();
            poviewmodel.deleteAll();

            LoginManager.getInstance().logOut();
            SharedPreferenceUtils.getInstance(getContext()).clear();

            new Thread(() -> Glide.get(getActivity()).clearDiskCache()).start();

            Glide.get(getActivity()).clearMemory();

            mCallBack.onUserLogout();
        }
    };

    public interface OnFragmentMenuListener {
        void onAvatarProfile();

        void onUserProfile();

        void onUserQuimica();

        void onUserMuro();

        void onUserJustoAhora();

        void onUserFiltro();

        void onUserBloqueados();

        void onUserStore();

        void onUserLogout();

        void onUserCancel();
    }
}
