package com.flyingkite.myliveaherowiki.library;

import android.content.Context;

import com.flyingkite.library.widget.Library;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HeroLibrary extends Library<HeroAdapter> {

    private int viewMode = HeroAdapter.GRID_HERO;
    private Map<Integer, HeroAdapter> adapters = new HashMap<>();

    public HeroLibrary(RecyclerView recycler) {
        super(recycler);
        setup();
        setViewMode(viewMode);
    }

    private void setup() {
        // adapters
        for (int i = 0; i < 4; i++) {
            int mode = i;
            HeroAdapter ha = new HeroAdapter();
            ha.setMode(mode);
            adapters.put(mode, ha);
        }
    }

    public Map<Integer, HeroAdapter> getAdapters() {
        return adapters;
    }

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int mode) {
        viewMode = mode;
        updateMode(mode);
    }

    private void updateMode(int mode) {
        if (recyclerView == null) {
            return;
        }

        Context c = recyclerView.getContext();
        RecyclerView.LayoutManager lm;
        if (HeroAdapter.isRowMode(mode)) {
            lm = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        } else {
            lm = new GridLayoutManager(c, 5);
        }
        recyclerView.setLayoutManager(lm);
        setViewAdapter(adapters.get(mode));
    }
}
