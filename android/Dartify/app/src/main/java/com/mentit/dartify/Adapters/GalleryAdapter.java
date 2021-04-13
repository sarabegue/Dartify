package com.mentit.dartify.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.Models.POJO.User.Foto;
import com.mentit.dartify.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<Foto> listaImagenes;
    private int layout;
    private OnItemClickListener listener;

    public GalleryAdapter(ArrayList<Foto> fotos, int layout, OnItemClickListener listener) {
        this.listaImagenes = fotos;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int sizes = 400;

        int placeholder = R.drawable.dartify_placeholder;
/*
        Picasso.get()
                .load(listaImagenes.get(position).getSource().toString())
                .placeholder(placeholder)
                .resize(200, 200)
                .centerCrop()
                .into(holder.image);*/
        holder.bind(listaImagenes.get(position).getSource(), listener);
    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView name;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            this.image = v.findViewById(R.id.imageViewGallery);
        }

        public void bind(final String name, final OnItemClickListener listener) {
            //this.name.setText(name);
            itemView.setOnClickListener(v -> listener.onItemClick(name, getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position);
    }

}
