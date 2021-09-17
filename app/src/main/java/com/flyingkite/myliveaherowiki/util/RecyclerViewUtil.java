package com.flyingkite.myliveaherowiki.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import flyingkite.math.Provider;

public interface RecyclerViewUtil {

    default void onToolScrollToPosition(RecyclerView rv, int position) {

    }

    default void initScrollTools(View goTop, View goBottom, Provider<RecyclerView> owner) {
        if (goTop != null) {
            goTop.setOnClickListener((v) -> {
                RecyclerView rv = null;
                if (owner != null) {
                    rv = owner.provide();
                }
                if (rv != null) {
                    rv.scrollToPosition(0);
                    onToolScrollToPosition(rv, 0);
                }
            });
        }

        if (goBottom != null) {
            goBottom.setOnClickListener((v) -> {
                RecyclerView rv = null;
                if (owner != null) {
                    rv = owner.provide();
                }
                if (rv != null) {
                    RecyclerView.Adapter a = rv.getAdapter();
                    int end = 0;
                    if (a != null) {
                        end = a.getItemCount() - 1;
                    }
                    rv.scrollToPosition(end);
                    onToolScrollToPosition(rv, end);
                }
            });
        }
    }

    default void initScrollTools(View goTop, View goBottom, RecyclerView recycler) {
        initScrollTools(goTop, goBottom, () -> {
            return recycler;
        });
    }

}
