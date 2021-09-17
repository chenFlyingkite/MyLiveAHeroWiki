package com.flyingkite.myliveaherowiki.library;

import android.content.Context;

import com.flyingkite.library.widget.Library;
import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HeroLibrary extends Library<HeroAdapter> {

    public static final int GRID_HERO = 0;
    public static final int ROWS_HERO = 1;
    public static final int GRID_SIDEKICK = 2;
    public static final int ROWS_SIDEKICK = 3;

    private int viewMode = GRID_HERO;
    private List<Hero> allHero = new ArrayList<>();
    private Map<Integer, HeroAdapter> adapters = new HashMap<>();

    public HeroLibrary(RecyclerView recycler) {
        super(recycler);
        setup();
        setViewMode(viewMode);
    }

    public static boolean isSideMode(int m) {
        return m == GRID_SIDEKICK || m == ROWS_SIDEKICK;
    }

    public static boolean isRowMode(int m) {
        return m == ROWS_HERO || m == ROWS_SIDEKICK;
    }

    private void setup() {
        // data
        allHero = Arrays.asList(LiveAHeroWiki.getAllHeros());
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
        if (isRowMode(mode)) {
            lm = new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false);
        } else {
            lm = new GridLayoutManager(c, 5);
        }
        recyclerView.setLayoutManager(lm);
        setViewAdapter(adapters.get(mode));
    }
}
