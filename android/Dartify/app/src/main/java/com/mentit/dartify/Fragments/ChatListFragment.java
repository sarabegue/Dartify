package com.mentit.dartify.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Adapters.ChatListAdapter;
import com.mentit.dartify.HelperClasses.SharedPreferenceUtils;
import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.Models.ViewModel.MensajeChatViewModel;
import com.mentit.dartify.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class ChatListFragment extends Fragment {
    private MensajeChatViewModel viewmodel;

    private RecyclerView recycler;
    private ChatListAdapter nAdapter;
    private RecyclerView.LayoutManager gLayoutManager;
    private DataListener callBack;

    private View iv;
    private View t1;
    private View t2;

    private Context context;

    private List<MensajeChat> chatlist;
    private long userid;

    public ChatListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);

        context = this.getContext();
        chatlist = new ArrayList<>();

        recycler = v.findViewById(R.id.recyclerChatList);
        gLayoutManager = new LinearLayoutManager(getActivity());
        nAdapter = new ChatListAdapter(chatlist, R.layout.cardview_elemento, nClickListener, container.getContext());

        recycler.setLayoutManager(gLayoutManager);
        recycler.setAdapter(nAdapter);

        iv = v.findViewById(R.id.imageViewEmpty);
        t1 = v.findViewById(R.id.textviewChatLine1);
        t2 = v.findViewById(R.id.textviewChatLine2);
        userid = SharedPreferenceUtils.getInstance(context).getLongValue(context.getString(R.string.user_id), 0);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewmodel = ViewModelProviders.of(this).get(MensajeChatViewModel.class);
        viewmodel.getChatList(userid).observe(this, data -> {
            nAdapter.setData(data);
            if (data.size() > 0) {
                iv.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (DataListener) context;
        } catch (Exception w) {
        }
    }

    private ChatListAdapter.OnItemClickListener nClickListener = new ChatListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(String chatRoom, long userid2, int position) {
            callBack.sendChatRequest(chatRoom, userid2);
        }
    };

    public interface DataListener {
        void sendChatRequest(String chatRoom, long userid2);
    }
}