package com.flyingkite.myliveaherowiki.util.select;

import java.util.ArrayList;
import java.util.List;

// Used to represent selected data
public class SelectedData {
    public int index;
    public String message = "";

    @Override
    public String toString() {
        return index + " -> " + message;
    }

    // projection on list for field, [list.index]
    public static List<Integer> getIndices(List<SelectedData> list) {
        List<Integer> ans = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                SelectedData d = list.get(i);
                ans.add(d.index);
            }
        }
        return ans;
    }
}
