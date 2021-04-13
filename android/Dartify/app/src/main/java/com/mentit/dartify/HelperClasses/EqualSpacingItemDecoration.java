package com.mentit.dartify.HelperClasses;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EqualSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private int displayMode;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    public EqualSpacingItemDecoration(int spacing) {
        this(spacing, -1);
    }

    public EqualSpacingItemDecoration(int spacing, int displayMode) {
        this.spacing = spacing;
        this.displayMode = displayMode;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        view.measure(1,1);
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredWidth();

        setSpacingForDirection(outRect, layoutManager, position, itemCount, width, height);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount, int width, int height) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        switch (displayMode) {
            case HORIZONTAL:
                outRect.left = spacing;
                outRect.right = position == itemCount - 1 ? spacing : 0;
                outRect.top = spacing;
                outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.left = spacing;
                outRect.right = spacing;
                outRect.top = spacing;
                outRect.bottom = position == itemCount - 1 ? spacing : 0;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;

                    if (itemCount % 2 == 1) {
                        rows = rows + 1;
                    }

                    int unidad = (gridLayoutManager.getWidth() - width * 2) / 3;
                    Log.d("unidad", unidad + "");

                    if (position % 2 == 0) {
                        outRect.left = unidad;
                        outRect.right = unidad / 2;
                    } else {
                        outRect.left = unidad / 2;
                        outRect.right = unidad;
                    }

                    outRect.top = unidad / 2;
                    outRect.bottom = unidad / 2;
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}
