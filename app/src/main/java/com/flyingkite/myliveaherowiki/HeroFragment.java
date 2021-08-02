package com.flyingkite.myliveaherowiki;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;
import com.flyingkite.myliveaherowiki.library.HeroLibrary;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import flyingkite.tool.TaskMonitor;

public class HeroFragment extends BaseFragment {

    private HeroLibrary heroLib;

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_hero;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadHero();
        setupToolbar();
    }

    private void setupToolbar() {
        findViewById(R.id.lahToolBar).setOnClickListener((v) -> {
            int mode = heroLib.getViewMode();
            int next = mode ^ 0x1;
            v.setSelected(mode == HeroLibrary.GRID_HERO);
            heroLib.setViewMode(next);
        });
    }

    private TaskMonitor.OnTaskState onCardsReady = new TaskMonitor.OnTaskState() {
        @Override
        public void onTaskDone(int index, String tag) {
            log("#%s (%s) is done", index, tag);
        }

        @Override
        public void onAllTaskDone() {
            getActivity().runOnUiThread(() -> {
                if (isActivityGone()) {
                    return;
                }

                Hero[] hs = LiveAHeroWiki.getAllHeros();
                String s = new Gson().toJson(hs);
                TextView t = findViewById(R.id.lahInfo);
                t.setText(hs.length + " Hero loaded");
                initLibrary();
                log("All task OK");
                //logE("hero = %s", s);
            });
        }
    };

    private void initLibrary() {
        HeroLibrary lib = new HeroLibrary(findViewById(R.id.lahRecycler));
        lib.setViewMode(HeroLibrary.GRID_HERO);
        heroLib = lib;
    }

    private void loadHero() {
        LiveAHeroWiki.attendDatabaseTasks(onCardsReady);
    }
}
