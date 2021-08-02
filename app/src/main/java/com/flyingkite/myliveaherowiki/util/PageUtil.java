package com.flyingkite.myliveaherowiki.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import androidx.annotation.IdRes;

public interface PageUtil {

    default Activity getActivity() {
        return null;
    }

    default View getView() {
        return null;
    }

    default void setOnClickListeners(View.OnClickListener lis, @IdRes int... ids) {
        for (int i : ids) {
            findViewById(i).setOnClickListener(lis);
        }
    }

    default <T extends View> T findViewById(@IdRes int id) {
        View w = getView();
        if (w != null) {
            T v = w.findViewById(id);
            if (v != null) {
                return v;
            }
        }

        if (getActivity() != null) {
            return getActivity().findViewById(id);
        }
        return null;
    }

    default boolean isActivityGone() {
        Activity act = getActivity();
        if (act == null) return true;
        if (act.isFinishing()) return true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return act.isDestroyed();
        }
        return false;
    }
}
