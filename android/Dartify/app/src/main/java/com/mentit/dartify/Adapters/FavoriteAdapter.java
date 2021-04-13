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
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.Models.FavoriteCard;
import com.mentit.dartify.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<FavoriteCard> lista;
    private int layout;
    private OnItemClickListener listener;
    private OnDeleteClickListener listenerdelete;
    private Context context;

    public FavoriteAdapter(List<FavoriteCard> lista, int layout, OnItemClickListener listener, OnDeleteClickListener listenerdelete, Context context) {
        this.lista = lista;
        this.layout = layout;
        this.listener = listener;
        this.listenerdelete = listenerdelete;
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
                .load(lista.get(position).getStrResource())
                .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(placeholder)
                )
                .into(holder.imagePerson);

        holder.bind(lista.get(position).getLongUserId2(), lista.get(position).getStrFirstname(), lista.get(position).getEdad() + " " + context.getString(R.string.years), listener, listenerdelete);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<FavoriteCard> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public TextView line1;
        public TextView line2;
        public ImageView imagePerson;
        public ImageView imageViewFavoriteDelete;

        public ViewHolder(View v) {
            super(v);
            this.imagePerson = v.findViewById(R.id.imageViewFavoritePerson);
            this.line1 = v.findViewById(R.id.textViewFavoriteLine1);
            this.line2 = v.findViewById(R.id.textViewFavoriteLine2);
            this.imageViewFavoriteDelete = v.findViewById(R.id.imageViewFavoriteDelete);
        }

        public void bind(final long id, final String line1, final String line2, final OnItemClickListener listener, final OnDeleteClickListener listenerdelete) {
            this.line1.setText(line1);
            this.line2.setText(line2);
            this.id = id;

            itemView.setOnClickListener(v -> listener.onItemClick(id, getAdapterPosition()));

            imageViewFavoriteDelete.setOnClickListener(v -> listenerdelete.onDeleteClickListener(id, getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClickListener(long id, int position);
    }
}