package com.mentit.dartify.HelperClasses;


import androidx.recyclerview.widget.RecyclerView;

public interface OnStartDragInterface {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
