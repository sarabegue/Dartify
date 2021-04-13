package com.mentit.dartify.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Adapters.NotificationCardAdapter;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.NotificationCard;
import com.mentit.dartify.Models.ViewModel.NotificationCardViewModel;
import com.mentit.dartify.R;
import com.mentit.dartify.util.Valores;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private NotificationCardViewModel dcviewmodel;

    private RecyclerView recycler;
    private NotificationCardAdapter nAdapter;
    private RecyclerView.LayoutManager gLayoutManager;

    private View iv;
    private View t1;
    private View t2;

    private Context context;

    DataListener callBack;

    private List<NotificationCard> notificaciones;
    private long userid;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        this.context = this.getContext();
        notificaciones = new ArrayList<>();

        recycler = v.findViewById(R.id.recyclerNotification);
        gLayoutManager = new LinearLayoutManager(getActivity());
        nAdapter = new NotificationCardAdapter(notificaciones, R.layout.cardview_elemento, nClickListener, container.getContext());

        recycler.setLayoutManager(gLayoutManager);
        recycler.setAdapter(nAdapter);

        iv = v.findViewById(R.id.imageViewEmpty);
        t1 = v.findViewById(R.id.textviewNotificationLine1);
        t2 = v.findViewById(R.id.textviewNotificationLine2);
        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        dcviewmodel = ViewModelProviders.of(this).get(NotificationCardViewModel.class);
        dcviewmodel.getData(userid).observe(this, data -> {
            nAdapter.setData(data);
            if (data.size() > 0) {
                iv.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
            }
        });
        dcviewmodel.readAll(userid);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (DataListener) context;
        } catch (Exception w) {
        }
    }

    private NotificationCardAdapter.OnItemClickListener nClickListener = new NotificationCardAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(long userid1, long userid2, int tipo, int position, String chatRoom) {
            //Callback a main activity
            if (tipo == Valores.NOTIFICACION_MENSAJE) {
                callBack.sendNotificationClickData(chatRoom, userid2);
            }
            if (tipo == Valores.NOTIFICACION_STICKER) {
                callBack.sendNotificationClickData(chatRoom, userid2);
            }
        }
    };

    public interface DataListener {
        void sendNotificationClickData(String chatRoom, long userid2);
    }
}