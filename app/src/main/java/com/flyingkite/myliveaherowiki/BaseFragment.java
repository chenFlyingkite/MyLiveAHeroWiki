package com.flyingkite.myliveaherowiki;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyingkite.library.log.Loggable;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public class BaseFragment extends Fragment implements Loggable {
    //protected static final ExecutorService sSingle = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        log("onAttach(%s)", context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate(%s)", savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        int pageId = getPageLayoutId();
        if (pageId > 0) {
            return inflater.inflate(pageId, container, false);
        } else {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        log("onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        log("onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        log("onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        log("onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy()");
    }

    @LayoutRes
    protected int getPageLayoutId() {
        return -1;
    }

}