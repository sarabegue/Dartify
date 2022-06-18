package com.mentit.dartify.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Activities.ChatActivity;
import com.mentit.dartify.Activities.MainActivity;
import com.mentit.dartify.Activities.RegisterActivity;
import com.mentit.dartify.Adapters.PeopleAdapter;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.Models.MatchCard;
import com.mentit.dartify.Models.PersonaOculta;
import com.mentit.dartify.Models.ViewModel.FavoriteCardViewModel;
import com.mentit.dartify.Models.ViewModel.MatchCardViewModel;
import com.mentit.dartify.Models.ViewModel.MensajeChatViewModel;
import com.mentit.dartify.Models.ViewModel.PersonaOcultaViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.User.GetUserProfileTask;
import com.mentit.dartify.util.FormatUtil;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private MatchCardViewModel viewmodel;
    private FavoriteCardViewModel fviewmodel;
    private MensajeChatViewModel mviewmodel;
    private PersonaOcultaViewModel poviewmodel;

    private ConstraintLayout layout;

    private PeopleAdapter nAdapter;
    private CardStackLayoutManager cardmanager;

    private LongPressPopup popupA;
    private ImageView favoriteImageButton;
    private ImageView chatImageButton;
    private ImageView justnowImageButton;
    private ImageView filterImageButton;
    private ImageView stickerImageButton;
    private CardStackView cardStackView;

    DataListener callBack;

    private View iv;
    private View t1;
    private View t2;

    private Context context;
    private List<MatchCard> matches;
    private List<FavoriteCard> favorites;
    private List<PersonaOculta> ocultos;

    int status = 0;
    private long userid1 = 0;
    private long userid2 = 0;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mviewmodel = ViewModelProviders.of(this).get(MensajeChatViewModel.class);

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        context = this.getContext();
        matches = new ArrayList<>();

        cardStackView = v.findViewById(R.id.recyclerViewPeople);
        nAdapter = new PeopleAdapter(matches, R.layout.cardview_person3, nClickListener, context);

        cardmanager = new CardStackLayoutManager(context, clistener);

        SwipeAnimationSetting swipesetting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(1000)
                .build();

        cardmanager.setSwipeThreshold(0.7f);

        cardmanager.setSwipeAnimationSetting(swipesetting);
        cardmanager.setCanScrollHorizontal(true);
        cardmanager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(cardmanager);
        cardStackView.setAdapter(nAdapter);

        favoriteImageButton = v.findViewById(R.id.imageViewFavoritePerson);
        chatImageButton = v.findViewById(R.id.imageViewChat);
        justnowImageButton = v.findViewById(R.id.buttonJustNow);
        filterImageButton = v.findViewById(R.id.buttonFilter);
        stickerImageButton = v.findViewById(R.id.imageViewSticker);

        layout = v.findViewById(R.id.contraintlayouthome);
        iv = v.findViewById(R.id.imageViewEmpty);
        t1 = v.findViewById(R.id.textviewHomeLine1);
        t2 = v.findViewById(R.id.textviewHomeLine2);

        userid1 = SharedPreferenceUtils.getInstance(context).getLongValue(this.getString(R.string.user_id), 0);

        favoriteImageButton.setOnClickListener(clickFavoriteListener);
        chatImageButton.setOnClickListener(clickChatListener);
        justnowImageButton.setOnClickListener(clickJustNowListener);
        filterImageButton.setOnClickListener(clickFilterListener);

        cardStackView.addOnScrollListener(recyclerlistenerposition);

        popupA = new LongPressPopupBuilder(context)
                .setTarget(stickerImageButton)
                .setPopupView(R.layout.popup_layout_stickers, (popupTag, root) -> {
                })
                .setTag("popa")
                .build();

        popupA.register();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        fviewmodel = ViewModelProviders.of(this).get(FavoriteCardViewModel.class);
        viewmodel = ViewModelProviders.of(this).get(MatchCardViewModel.class);
        poviewmodel = ViewModelProviders.of(this).get(PersonaOcultaViewModel.class);

        int membresia = SharedPreferenceUtils.getInstance(context).getIntValue("membresia", 1);

        viewmodel.getData(userid1, membresia).observe(this, data -> {
            nAdapter.setData(data);
            if (data.size() > 0) {
                iv.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.VISIBLE);
            }
        });
        fviewmodel.getData(userid1).observe(this, data -> {
            favorites = data;
            actualizarStatus();
        });
        poviewmodel.getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (DataListener) context;
        } catch (Exception w) {
        }
    }

    private RecyclerView.OnScrollListener recyclerlistenerposition = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            actualizarStatus();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            actualizarStatus();
        }
    };

    public void clickSticker(View v) {
        popupA.dismissNow();

        mviewmodel.guardarMensaje(FormatUtil.getChatRoom(userid1, userid2), userid1, userid2, v.getTag().toString(), 2);
    }

    private void actualizarStatus() {
        userid2 = getUserIdOnTop();

        if (userid2 == -1) return;

        new GetUserProfileTask(  context  , userid2).execute("");

        status = 0;
        if (favorites != null) {
            for (FavoriteCard f : favorites) {
                if (userid2 == f.getLongUserId2()) {
                    status = 1;
                    continue;
                }
            }
        }
        refreshStatus(status);
    }

    private long getUserIdOnTop() {
        if (cardmanager.getTopView() == null) return -1;
        int pos = cardmanager.getTopPosition();

        if (nAdapter.getItemCount() > 0) {
            return nAdapter.getCardAtPosition(pos).getUserid2();
        }

        return -1;
    }

    private PeopleAdapter.OnItemClickListener nClickListener = new PeopleAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String name, int position, long itemid) {
            callBack.sendProfileData(itemid);
        }
    };

    private View.OnClickListener clickFavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            status = (status == 1) ? 0 : 1;

            if (status == 0) {
                favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_disabled_star));
                fviewmodel.deleteFavorite(userid1, userid2);
            }
            if (status == 1) {
                favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_enabled_star));
                fviewmodel.addFavorite(userid1, userid2);
            }
        }
    };

    private View.OnClickListener clickChatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userid2", userid2);
            startActivity(intent);
        }
    };

    private View.OnClickListener clickJustNowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toasty.success(context, "JUST NOW", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener clickFilterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toasty.success(context, "FILTER", Toast.LENGTH_SHORT).show();
        }
    };

    public void refreshStatus(int status) {
        this.status = status;
        if (status == 1) {
            favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_enabled_star));
        } else {
            favoriteImageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home_disabled_star));
        }
    }

    private CardStackListener clistener = new CardStackListener() {
        private long userid2tmp = 0;

        @Override
        public void onCardDragging(Direction direction, float ratio) {
            userid2tmp = getUserIdOnTop();
            actualizarStatus();
        }

        @Override
        public void onCardSwiped(Direction direction) {
            switch (direction) {
                case Left: {
                    //Log.d("DIRECCION", "DESCARTADO");
                    if (userid2tmp > 0) {
                        fviewmodel.deleteFavorite(userid1, userid2tmp);
                        viewmodel.visto(userid2tmp);
                        Toasty.custom(context, "DESCARTADO", R.drawable.favorite_cross, getResources().getColor(R.color.color_darkgray), Toast.LENGTH_SHORT, true, true).show();
                    }
                    break;
                }
                case Right: {
                    //Log.d("DIRECCION", "FAVORITO");
                    if (userid2tmp > 0) {
                        fviewmodel.addFavorite(userid1, userid2tmp);
                        viewmodel.visto(userid2tmp);
                        Toasty.custom(context, "FAVORITO", R.drawable.favorite_star, getResources().getColor(R.color.color_green), Toast.LENGTH_SHORT, true, true).show();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCardRewound() {

        }

        @Override
        public void onCardCanceled() {

        }

        @Override
        public void onCardAppeared(View view, int position) {

        }

        @Override
        public void onCardDisappeared(View view, int position) {

        }
    };

    public interface DataListener {
        void sendProfileData(long id);
    }
}