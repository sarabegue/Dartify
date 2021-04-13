package com.mentit.dartify.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.HelperClasses.WhiteBorderTransformation;
import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<MensajeChat> lista;
    private int layout;
    private OnItemClickListener listener;
    private Context context;

    public ChatListAdapter(List<MensajeChat> lista, int layout, OnItemClickListener listener, Context context) {
        this.lista = lista;
        this.layout = layout;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int colorborde = ContextCompat.getColor(context, R.color.color_accent);
        ColorDrawable placeholder = new ColorDrawable(Color.GRAY);
        int fondo = R.drawable.rounded_notification_unread;
        int txtcolor = ContextCompat.getColor(context, R.color.color_accent);

        MensajeChat e = lista.get(position);

        if (!e.isLeido()) {
            fondo = R.drawable.rounded_notification_read;
            txtcolor = ContextCompat.getColor(context, R.color.color_darkgray);
        }

        Glide
                .with(context)
                .load(e.getStrResource2())
                .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(placeholder)
                        .transforms(new WhiteBorderTransformation(Color.WHITE), new CircleCrop())
                )
                .into(holder.imagePerson);

        holder.bind(e.getStrChatroom(), e.getStrFirstname2(), e.getStrMensaje(), e.getFormato(e.TIPO_FECHA), fondo, txtcolor, e.getIdDestinatario(), listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<MensajeChat> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String strChatRoom;
        public CardView layout;
        public ImageView imagePerson;
        public ImageView notificationIcon;
        public TextView tvlinea1;
        public TextView tvlinea2;
        public TextView tvhora;
        public long userid2;

        public ViewHolder(View v) {
            super(v);
            this.layout = v.findViewById(R.id.layoutItemNotification);
            this.imagePerson = v.findViewById(R.id.imageViewNotificationPerson);
            this.notificationIcon = v.findViewById(R.id.imageViewNotificationIcon);
            this.tvlinea1 = v.findViewById(R.id.textViewNotificationLine1);
            this.tvlinea2 = v.findViewById(R.id.textViewNotificationLine2);
            this.tvhora = v.findViewById(R.id.textViewNotificationLine3);
        }

        public void bind(final String strChatRoom, final String linea1, final String linea2, final String hora, final int bg, final int txtcol, final long userid2, final OnItemClickListener listener) {
            this.tvlinea1.setText(linea1);
            this.tvlinea2.setText(linea2);
            this.tvhora.setText(hora);
            this.strChatRoom = strChatRoom;
            this.userid2 = userid2;

            layout.setBackgroundResource(bg);
            tvlinea1.setTextColor(txtcol);
            tvlinea2.setTextColor(txtcol);
            tvhora.setTextColor(txtcol);

            itemView.setOnClickListener(v -> listener.onItemClick(strChatRoom, userid2, getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String chatRoom, long userid2, int position);
    }
}
