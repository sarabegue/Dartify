package com.mentit.dartify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mentit.dartify.HelperClasses.ItemTouchHelperAdapter;
import com.mentit.dartify.HelperClasses.ItemTouchHelperViewHolder;
import com.mentit.dartify.Models.Chiste;
import com.mentit.dartify.R;

import java.util.ArrayList;
import java.util.Collections;

public class HumorAdapter extends RecyclerView.Adapter<HumorAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Chiste> lista;
    private int layout;
    private OnItemClickListener listener;
    private Context context;

    public HumorAdapter(ArrayList<Chiste> lista, int layout, OnItemClickListener listener, Context context) {
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
        Chiste chiste = lista.get(position);
        holder.bind(chiste, listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public Chiste getItem(int position) {
        return lista.get(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(lista, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        //Log.d("ARRASTRE DE ", fromPosition + " a " + toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        lista.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public LinearLayout layout;
        public TextView chiste;

        public ViewHolder(View v) {
            super(v);
            this.chiste = v.findViewById(R.id.textviewchiste);
            this.layout = v.findViewById(R.id.layoutchiste);
        }

        public void bind(final Chiste chiste, final OnItemClickListener listener) {
            this.chiste.setText(chiste.getChiste());
            this.layout.setTag(chiste.getCategoria());

            itemView.setOnClickListener(v -> listener.onItemClick(chiste.getChiste(), getAdapterPosition()));
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position);
    }
}
