package com.flyingkite.myliveaherowiki.lah.query;

import com.flyingkite.myliveaherowiki.util.select.Selector;

import java.util.List;

import androidx.annotation.NonNull;

public class AllHero<T> implements Selector<T> {
    protected List<T> data;
    private boolean isCancelled = false;

    public AllHero(List<T> list) {
        data = list;
    }

    @NonNull
    @Override
    public List<T> from() {
        return data;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

}