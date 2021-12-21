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
import com.mentit.dartify.Models.MatchCard;
import com.mentit.dartify.R;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {
    private List<MatchCard> lista;
    private int layout;
    private OnItemClickListener listener;
    private Context context;

    public PeopleAdapter(List<MatchCard> lista, int layout, OnItemClickListener listener, Context context) {
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
        MatchCard o = lista.get(position);

        Glide
                .with(context)
                .load(o.getResource1())
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(placeholder)
                )
                .into(holder.imagePerson);

        holder.bind(o.getUserid2(), o.getTexto1(), o.getEdad() + " " + context.getString(R.string.years), listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<MatchCard> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public MatchCard getCardAtPosition(int index) {
        if (lista.size() == 0) return null;
        return this.lista.get(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imagePerson;
        public TextView line1;
        public TextView line2;

        public long itemid = 0;

        public ViewHolder(View v) {
            super(v);
            this.imagePerson = v.findViewById(R.id.imageViewPerson1);
            this.line1 = v.findViewById(R.id.textViewPersonLine1);
            this.line2 = v.findViewById(R.id.textViewPersonLine2);
        }

        public void bind(final long id, final String line1, final String line2, final OnItemClickListener listener) {
            this.line1.setText(line1);
            this.line2.setText(line2);

            itemid = id;

            imagePerson.setOnClickListener(v -> listener.onItemClick(line1, getAdapterPosition(), itemid));
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position, long itemid);
    }
}