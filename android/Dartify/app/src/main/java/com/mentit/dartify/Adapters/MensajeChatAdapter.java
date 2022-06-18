package com.mentit.dartify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mentit.dartify.Models.MensajeChat;
import com.mentit.dartify.R;

import java.util.ArrayList;
import java.util.List;

public class MensajeChatAdapter extends RecyclerView.Adapter {
    private List<MensajeChat> lista = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;

    private static final int VIEW_TYPE_MESSAGE_FECHA = 0;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_STICKER_SENT = 3;
    private static final int VIEW_TYPE_STICKER_RECEIVED = 4;

    public MensajeChatAdapter(ArrayList<MensajeChat> lista, OnItemClickListener listener, Context context) {
        this.lista = lista;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_messagesent, parent, false);
            return new MessageSentHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_messagereceived, parent, false);
            return new MessageReceivedHolder(view);
        } else if (viewType == VIEW_TYPE_STICKER_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stickersent, parent, false);
            return new StickerSentHolder(view);
        } else if (viewType == VIEW_TYPE_STICKER_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_stickerreceived, parent, false);
            return new StickerReceivedHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        MensajeChat message = lista.get(position);

        /*
        if (message.getTipo() == message.TIPO_FECHA) {
            return VIEW_TYPE_MESSAGE_FECHA;
        }

        return -1;*/

        if (message.getTipo() == message.TIPO_MENSAJE) {
            if (message.isFromme()) {
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

        if (message.getTipo() == message.TIPO_STICKER) {
            if (message.isFromme()) {
                return VIEW_TYPE_STICKER_SENT;
            } else {
                return VIEW_TYPE_STICKER_RECEIVED;
            }
        }

        return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MensajeChat m = lista.get(position);
        int icono = 0;

        if (m.getTipo() == m.TIPO_STICKER) {
            switch (m.getStrMensaje()) {
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
        }

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT: {
                if (m.getStrFecha() == null) {
                    ((MessageSentHolder) holder).bind(m.getStrMensaje(), "Enviando...");
                } else {
                    ((MessageSentHolder) holder).bind(m.getStrMensaje(), m.getFormato(m.TIPO_FECHA));
                }
                break;
            }
            case VIEW_TYPE_MESSAGE_RECEIVED: {
                ((MessageReceivedHolder) holder).bind(m.getStrMensaje(), m.getFormato(m.TIPO_FECHA));
                break;
            }
            case VIEW_TYPE_STICKER_SENT: {
                ((StickerSentHolder) holder).bind(m.getFormato(m.TIPO_FECHA));
                Glide
                        .with(context)
                        .load(icono)
                        .into(((StickerSentHolder) holder).stickerImage);
                break;
            }
            case VIEW_TYPE_STICKER_RECEIVED: {
                ((StickerReceivedHolder) holder).bind(m.getFormato(m.TIPO_FECHA));
                Glide
                        .with(context)
                        .load(icono)
                        .into(((StickerReceivedHolder) holder).stickerImage);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<MensajeChat> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class MessageSentHolder extends RecyclerView.ViewHolder {
        public long id;
        public LinearLayout layoutmensaje;
        public EmojiTextView tvchatmessage;
        public TextView tvchattime;

        public MessageSentHolder(View v) {
            super(v);
            this.layoutmensaje = v.findViewById(R.id.layoutmessage);
            this.tvchatmessage = v.findViewById(R.id.textviewchatMessage);
            this.tvchattime = v.findViewById(R.id.textviewchatTime);
        }

        public void bind(final CharSequence mensaje, final String date) {
            this.tvchatmessage.setText(mensaje);
            this.tvchattime.setText(date);
        }
    }

    public static class MessageReceivedHolder extends RecyclerView.ViewHolder {
        public long id;
        public LinearLayout layoutmensaje;
        public EmojiTextView tvchatmessage;
        public TextView tvchattime;

        public MessageReceivedHolder(View v) {
            super(v);
            this.layoutmensaje = v.findViewById(R.id.layoutmessage);
            this.tvchatmessage = v.findViewById(R.id.textviewchatMessage);
            this.tvchattime = v.findViewById(R.id.textviewchatTime);
        }

        public void bind(final CharSequence mensaje, final String date) {
            this.tvchatmessage.setText(mensaje);
            this.tvchattime.setText(date);
        }
    }

    public static class StickerReceivedHolder extends RecyclerView.ViewHolder {
        public long id;
        public LinearLayout layoutmensaje;
        public ImageView stickerImage;
        public TextView tvchattime;

        public StickerReceivedHolder(View v) {
            super(v);
            this.layoutmensaje = v.findViewById(R.id.layoutmessage);
            this.stickerImage = v.findViewById(R.id.textviewchatSticker);
            this.tvchattime = v.findViewById(R.id.textviewchatTime);
        }

        public void bind(final String date) {
            this.tvchattime.setText(date);
        }
    }

    public static class StickerSentHolder extends RecyclerView.ViewHolder {
        public long id;
        public LinearLayout layoutmensaje;
        public ImageView stickerImage;
        public TextView tvchattime;

        public StickerSentHolder(View v) {
            super(v);
            this.layoutmensaje = v.findViewById(R.id.layoutmessage);
            this.stickerImage = v.findViewById(R.id.textviewchatSticker);
            this.tvchattime = v.findViewById(R.id.textviewchatTime);
        }

        public void bind(final String date) {
            this.tvchattime.setText(date);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id, int position);
    }
}