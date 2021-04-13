package com.mentit.dartify.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Adapters.FavoriteAdapter;
import com.mentit.dartify.HelperClasses.EqualSpacingItemDecoration;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.Models.ViewModel.FavoriteCardViewModel;
import com.mentit.dartify.Models.ViewModel.PerfilCardViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.Tasks.User.GetUserProfileTask;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class FavoriteFragment extends Fragment {
    private FavoriteCardViewModel viewmodel;
    private static PerfilCardViewModel mcvm;

    private RecyclerView recycler;
    private FavoriteAdapter nAdapter;
    private RecyclerView.LayoutManager gLayoutManager;
    private DataListener callBack;
    private long userid;

    private View iv;
    private View t1;
    private View t2;

    private Context context;

    private List<FavoriteCard> favoritoslist;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        context = this.getContext();
        favoritoslist = new ArrayList<>();

        recycler = v.findViewById(R.id.recyclerViewFavorites);
        gLayoutManager = new GridLayoutManager(getActivity(), 2);
        nAdapter = new FavoriteAdapter(favoritoslist, R.layout.cardview_favorite, nClickListener, nClickDeleteListener, context);

        recycler.setLayoutManager(gLayoutManager);
        recycler.setAdapter(nAdapter);
        recycler.setHasFixedSize(true);
        recycler.addItemDecoration(new EqualSpacingItemDecoration(30, EqualSpacingItemDecoration.GRID));

        iv = v.findViewById(R.id.imageViewEmpty);
        t1 = v.findViewById(R.id.textviewFavoritesLine1);
        t2 = v.findViewById(R.id.textviewNotificationLine2);

        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mcvm = ViewModelProviders.of(this).get(PerfilCardViewModel.class);

        viewmodel = ViewModelProviders.of(this).get(FavoriteCardViewModel.class);
        viewmodel.getData(userid).observe(this, data -> {
            nAdapter.setData(data);
            if (data.size() > 0) {
                iv.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
            } else {
                iv.setVisibility((View.VISIBLE));
                t1.setVisibility((View.VISIBLE));
                t2.setVisibility((View.VISIBLE));
            }

            for (FavoriteCard p : data) {
                new GetUserProfileTask(context, p.getLongUserId2()).execute("");
            }
        });
    }

    private FavoriteAdapter.OnItemClickListener nClickListener = new FavoriteAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(long id, int position) {
            callBack.sendFavoriteData(id);
        }
    };

    private FavoriteAdapter.OnDeleteClickListener nClickDeleteListener = new FavoriteAdapter.OnDeleteClickListener() {
        @Override
        public void onDeleteClickListener(long id, int position) {
            viewmodel.deleteFavorite(userid, id);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (DataListener) context;
        } catch (Exception w) {
        }
    }

    public interface DataListener {
        void sendFavoriteData(long id);
    }
}
