package com.flyingkite.myliveaherowiki;

import android.os.Bundle;
import android.view.View;

import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;

import androidx.annotation.Nullable;
import flyingkite.tool.TaskMonitor;

public class HeroFragment extends BaseFragment {
    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_hero;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveAHeroWiki.attendDatabaseTasks(onCardsReady);
    }

    private TaskMonitor.OnTaskState onCardsReady = new TaskMonitor.OnTaskState() {
        @Override
        public void onTaskDone(int index, String tag) {
            log("#%s (%s) is done", index, tag);
        }

        @Override
        public void onAllTaskDone() {
            log("All task OK");
//            if (isActivityGone()) {
//                return;
//            }

            getActivity().runOnUiThread(() -> {
                //onCardsReady(TosWiki.allCards());
            });
        }
    };
}
