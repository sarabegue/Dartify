package com.mentit.dartify.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.HelperClasses.WhiteBorderTransformation;
import com.mentit.dartify.Models.NotificationCard;
import com.mentit.dartify.R;
import com.mentit.dartify.util.FormatUtil;
import com.mentit.dartify.util.Valores;

import java.util.ArrayList;
import java.util.List;

public class NotificationCardAdapter extends RecyclerView.Adapter<NotificationCardAdapter.ViewHolder> {
    private List<NotificationCard> lista = new ArrayList<>();
    private int layout;
    private OnItemClickListener listener;
    private Context context;

    public NotificationCardAdapter(List<NotificationCard> lista, int layout, OnItemClickListener listener, Context context) {
        this.lista = lista;
        this.layout = layout;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int colorborde = ContextCompat.getColor(context, R.color.color_accent);
        ColorDrawable placeholder = new ColorDrawable(Color.GRAY);
        int fondo = R.drawable.rounded_notification_unread;
        int txtcolor = ContextCompat.getColor(context, R.color.color_accent);

        NotificationCard e = lista.get(position);

        if (e.getLeido() == 1) {
            fondo = R.drawable.rounded_notification_read;
            txtcolor = ContextCompat.getColor(context, R.color.color_darkgray);
        }

        holder.notificationIcon.setImageDrawable(null);

        if (e.getTipo() == Valores.NOTIFICACION_STICKER) {
            int icono = 0;
            switch (e.getTexto2()) {
                case "corazon": {
                    icono = R.drawable.iconos_accion_corazon;
                    break;
                }
                case "saludo": {
                    icono = R.drawable.iconos_accion_saludo;
                    break;
                }
                case "beso": {
                    icono = R.drawable.iconos_accion_beso;
                    break;
                }
                case "flor": {
                    icono = R.drawable.iconos_accion_flor;
                    break;
                }
            }

            e.setTexto2("Te envió un sticker");

            Glide
                    .with(context)
                    .load(icono)
                    .into(holder.notificationIcon);
        }

        Glide
                .with(context)
                .load(e.getResource1())
                .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(placeholder)
                        .transforms(new WhiteBorderTransformation(Color.WHITE), new CircleCrop())
                )
                .into(holder.imagePerson);

        holder.bind(e.getId(), e.getTexto1(), e.getTexto2(), e.getHora(), fondo, txtcolor, listener, e.getUserid2(), e.getUserid1(), FormatUtil.getChatRoom(e.getUserid1(), e.getUserid2()), e.getTipo());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<NotificationCard> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public CardView layout;
        public ImageView imagePerson;
        public ImageView notificationIcon;
        public TextView tvlinea1;
        public TextView tvlinea2;
        public TextView tvhora;

        public long userid1 = 0;
        public long userid2 = 0;
        public int tipo = -1;
        public String chatRoom = "";

        public ViewHolder(View v) {
            super(v);
            this.layout = v.findViewById(R.id.layoutItemNotification);
            this.imagePerson = v.findViewById(R.id.imageViewNotificationPerson);
            this.notificationIcon = v.findViewById(R.id.imageViewNotificationIcon);
            this.tvlinea1 = v.findViewById(R.id.textViewNotificationLine1);
            this.tvlinea2 = v.findViewById(R.id.textViewNotificationLine2);
            this.tvhora = v.findViewById(R.id.textViewNotificationLine3);
        }

        public void bind(final long id, final String linea1, final String linea2, final String hora, final int bg, final int txtcol, final OnItemClickListener listener, long userid1, long userid2, String chatroom, int tipo) {
            this.tvlinea1.setText(linea1);
            this.tvlinea2.setText(linea2);
            this.tvhora.setText(hora);
            this.id = id;
            this.userid1 = userid1;
            this.userid2 = userid2;
            this.tipo = tipo;
            this.chatRoom = chatroom;

            layout.setBackgroundResource(bg);
            tvlinea1.setTextColor(txtcol);
            tvlinea2.setTextColor(txtcol);
            tvhora.setTextColor(txtcol);

            itemView.setOnClickListener(v -> listener.onItemClick(userid1, userid2, tipo, getAdapterPosition(), chatRoom));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long userid1, long userid2, int tipo, int position, String chatRoom);
    }
}
