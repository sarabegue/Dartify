package com.mentit.dartify.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.HelperClasses.WhiteBorderTransformation;
import com.mentit.dartify.Models.EstadoCard;
import com.mentit.dartify.R;

import java.util.List;

public class EstadoAdapter extends RecyclerView.Adapter<EstadoAdapter.ViewHolder> {
    private List<EstadoCard> lista;
    private int layout;
    private OnItemClickListener listener;
    private Context context;

    public EstadoAdapter(List<EstadoCard> lista, int layout, OnItemClickListener listener, Context context) {
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
        ColorDrawable placeholder = new ColorDrawable(Color.GRAY);
        Glide
                .with(context)
                .load(lista.get(position).getResource1())
                .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(placeholder)
                        .transforms(new WhiteBorderTransformation(Color.WHITE), new CircleCrop())
                )
                .into(holder.imagePerson);

        holder.bind(lista.get(position).getUserid1(), lista.get(position).getTexto1(), lista.get(position).getTexto2(), lista.get(position).getHora(), listener);
    }

    @Override
    public int getItemCount() {
        //Log.d("LIST SIZE", lista.size() + "");
        return lista.size();
    }

    public void setData(List<EstadoCard> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public TextView line1;
        public TextView line2;
        public TextView line3;
        public ImageView imagePerson;

        public ViewHolder(View v) {
            super(v);
            this.imagePerson = v.findViewById(R.id.imageViewNotificationPerson);
            this.line1 = v.findViewById(R.id.textViewNotificationLine1);
            this.line2 = v.findViewById(R.id.textViewNotificationLine2);
            this.line3 = v.findViewById(R.id.textViewNotificationLine3);
        }

        public void bind(final long id, final String line1, final String line2, final String line3, final OnItemClickListener listener) {
            this.line1.setText(line1);
            this.line2.setText(line2);
            this.line3.setText(line3);
            this.id = id;

            itemView.setOnClickListener(v -> listener.onItemClick(id, getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id, int position);
    }
}