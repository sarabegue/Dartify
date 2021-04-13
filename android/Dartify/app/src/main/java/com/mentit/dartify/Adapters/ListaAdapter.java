package com.mentit.dartify.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.mentit.dartify.Models.ElementoLista;

import java.util.List;

public class ListaAdapter extends ArrayAdapter<ElementoLista> {

    private List<ElementoLista> items;

    public ListaAdapter(@NonNull Context context, @LayoutRes int resource, List<ElementoLista> list) {
        super(context, resource, 0, list);
        items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getID();
    }

    public int getPosition(int id) {
        int posicion = -1;

        for (int index = 0; index < getCount(); index++) {
            ElementoLista e = items.get(index);
            if (e.getID() == id) {
                return index;
            }
        }

        return posicion;
    }
}
