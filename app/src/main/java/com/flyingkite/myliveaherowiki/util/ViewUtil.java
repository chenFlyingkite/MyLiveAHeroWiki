package com.flyingkite.myliveaherowiki.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.IdRes;
import flyingkite.functional.Projector;

public interface ViewUtil {

    default void getTagsWhen(Projector<View, Boolean> fn, ViewGroup vg, Set<String> result, boolean addAllIfEmpty) {
        if (result == null) {
            result = new HashSet<>();
        }
        int n = vg == null ? 0 : vg.getChildCount();

        Set<String> all = new HashSet<>();
        boolean added = false;
        for (int i = 0; i < n; i++) {
            View w = vg.getChildAt(i);
            String tag = w.getTag().toString();
            if (fn.get(w)) {
                added = true;
                result.add(tag);
            }
            all.add(tag);
        }
        // If no children is added, add all the child tags
        if (addAllIfEmpty && !added) {
            result.addAll(all);
        }
    }

    default void getSelectTags(ViewGroup vg, Set<String> result, boolean addAllIfEmpty) {
        getTagsWhen(View::isSelected, vg, result, addAllIfEmpty);
    }

    default <T extends ViewGroup> T setTargetChildClick(View parent, @IdRes int targetId, View.OnClickListener childClick) {
        T vg = parent.findViewById(targetId);
        setChildClick(vg, childClick);
        return vg;
    }

    default void setChildClick(ViewGroup vg, View.OnClickListener clk) {
        int n = vg.getChildCount();
        for (int i = 0; i < n; i++) {
            View w = vg.getChildAt(i);
            w.setOnClickListener(clk);
        }
    }

    default void toggleAndClearIfAll(View v, ViewGroup vg) {
        toggleSelected(v);
        clearIfAllSelected(vg);
    }

    default void toggleSelected(View v) {
        if (v == null) return;
        v.setSelected(!v.isSelected());
    }

    default void clearIfAllSelected(ViewGroup vg) {
        // Deselect all if all selected
        if (isAllAsSelected(vg, true)) {
            setAllChildSelected(vg, false);
        }
    }

    /**
     * @return true if ALL children's isSelected() == selected,
     *         false otherwise
     */
    default boolean isAllAsSelected(ViewGroup vg, boolean selected) {
        if (vg == null) return false;

        int n = vg.getChildCount();
        for (int i = 0; i < n; i++) {
            if (vg.getChildAt(i).isSelected() != selected) {
                return false;
            }
        }
        return true;
    }

    default void setAllChildSelected(ViewGroup vg, boolean sel) {
        if (vg == null) return;

        int n = vg.getChildCount();
        for (int i = 0; i < n; i++) {
            vg.getChildAt(i).setSelected(sel);
        }
    }

    default void setCheckedIncludeNo(View clicked, View noView, ViewGroup parent) {
        int n = parent.getChildCount();
        if (clicked != noView) {
            // uncheck no first since select other
            setViewCheck(false, noView);
            boolean non = isAllAsChecked(parent, false);
            // check no if all unchecked
            if (non) {
                setViewCheck(true, noView);
            }
        } else {
            // selected no, uncheck all others except no
            for (int i = 0; i < n; i++) {
                setViewCheck(false, parent.getChildAt(i));
            }
            setViewCheck(true, noView);
        }
    }

    default void setCheckedIncludeNo(View clicked, @IdRes int noId, ViewGroup parent) {
        View noView = null;
        // Find the noView
        int n = parent.getChildCount();
        for (int i = 0; i < n && noView == null; i++) {
            View v = parent.getChildAt(i);
            if (v.getId() == noId) {
                noView = v;
            }
        }

        int vid = clicked.getId();
        if (vid != noId) {
            // uncheck no first since select other
            setViewCheck(false, noView);
            boolean non = isAllAsChecked(parent, false);
            // check no if all unchecked
            if (non) {
                setViewCheck(true, noView);
            }
        } else {
            // selected no, uncheck all others except no
            for (int i = 0; i < n; i++) {
                setViewCheck(false, parent.getChildAt(i));
            }
            setViewCheck(true, noView);
        }
    }

    default void setViewCheck(boolean check, View v) {
        Checkable c;
        if (v instanceof Checkable) {
            c = (Checkable) v;
            c.setChecked(check);
        }
    }

    default boolean isAllAsChecked(ViewGroup vg, boolean checked) {
        if (vg == null) return false;

        int n = vg.getChildCount();
        View v;
        Checkable c;
        for (int i = 0; i < n; i++) {
            v = vg.getChildAt(i);
            if (v instanceof Checkable) {
                c = (Checkable) v;
                if (c.isChecked() != checked) {
                    return false;
                }
            }
        }
        return true;
    }

}
