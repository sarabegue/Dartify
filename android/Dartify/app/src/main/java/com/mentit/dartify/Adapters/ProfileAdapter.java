package com.mentit.dartify.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mentit.dartify.Models.FotoUsuario;
import com.mentit.dartify.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<FotoUsuario> lista;
    private int layout;
    private OnItemClickListener listener;
    private Context context;

    public ProfileAdapter(List<FotoUsuario> lista, int layout, OnItemClickListener listener, Context context) {
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
                )
                .into(holder.imagePerson);

        holder.bind(0, listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<FotoUsuario> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public long id;
        public ImageView imagePerson;

        public ViewHolder(View v) {
            super(v);
            this.imagePerson = v.findViewById(R.id.imageViewProfile);
        }

        public void bind(final long id, final OnItemClickListener listener) {
            this.id = id;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(id, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id, int position);
    }
}