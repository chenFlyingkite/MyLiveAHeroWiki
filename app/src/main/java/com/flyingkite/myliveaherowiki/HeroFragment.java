package com.flyingkite.myliveaherowiki;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.data.HeroSkill;
import com.flyingkite.myliveaherowiki.data.HeroUtil;
import com.flyingkite.myliveaherowiki.data.SideSkill;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;
import com.flyingkite.myliveaherowiki.lah.query.AllHero;
import com.flyingkite.myliveaherowiki.library.HeroAdapter;
import com.flyingkite.myliveaherowiki.library.HeroLibrary;
import com.flyingkite.myliveaherowiki.util.ViewUtil;
import com.flyingkite.myliveaherowiki.util.select.SelectedData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import flyingkite.tool.TaskMonitor;

public class HeroFragment extends BaseFragment implements ViewUtil {

    private HeroLibrary heroLib;
    private View sortArea;
    private RecyclerView recycler;
    private TextView lahInfo;
    // 屬性 種族 星
    private ViewGroup sortAttributes;
    private ViewGroup sortRanks;
    private ViewGroup sortSpecial;

    private ViewGroup specialArea;
    private CheckBox specialAll;
    private CheckBox specialSkill1;
    private CheckBox specialSkill2;
    private CheckBox specialSkill3;
    private CheckBox specialSkill4;
    private CheckBox specialEquip;
    private CheckBox sortSpecialNo;
    private CheckBox sortSpecialOneEnemy;
    private CheckBox sortSpecialAllEnemy;


    private final List<Hero> allHero = new ArrayList<>();

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_hero;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToolbar();
        loadHero();
    }

    private void setupToolbar() {
        findViewById(R.id.lahToolBar).setOnClickListener((v) -> {
            int mode = heroLib.getViewMode();
            int next = mode ^ 0x1;
            v.setSelected(HeroLibrary.isRowMode(mode));
            heroLib.setViewMode(next);
            applySelection();
            logE("mode = %s", mode);
        });

        findViewById(R.id.lahFavor).setOnClickListener((v) -> {
            int mode = heroLib.getViewMode();
            int next = mode ^ 0x2;
            v.setSelected(HeroLibrary.isSideMode(mode));
            heroLib.setViewMode(next);
            applySelection();
            logE("mode = %s", mode);
        });

        lahInfo = findViewById(R.id.lahInfo);
        recycler = findViewById(R.id.lahRecycler);
        initScrollTools(findViewById(R.id.lahGoTop), findViewById(R.id.lahGoBottom), recycler);

        initSortMenu();
    }

    @Override
    public boolean onBackPressed() {
        // hide sort menu
        if (sortArea.getVisibility() == View.VISIBLE) {
            sortArea.callOnClick();
            return true;
        }
        return false;
    }


    private void initSortMenu() {
        sortArea = findViewById(R.id.lahSortArea);
        sortArea.setOnClickListener((v) -> {
            sortArea.setVisibility(View.INVISIBLE);
        });
        findViewById(R.id.lahSortMenu).setOnClickListener((v) -> {
            sortArea.setVisibility(View.VISIBLE);
        });

        View menu = findViewById(R.id.lahSortPopup);
        makeMenu(menu);
    }

    private void makeMenu(View menu) {
        initShareImage(menu);
        initSortReset(menu);
        initSortByAttribute(menu);
        initSortByRank(menu);
        initSortBySpecial(menu);
        initSortSpecial(menu);
    }


    private void initShareImage(View parent) {
        parent.findViewById(R.id.tosSave).setOnClickListener((v) -> {
            View view = recycler;
            //String name = ShareHelper.cacheName("1.png");
            //logE("Save to %s", name);

            //ShareHelper.shareImage(getActivity(), view, name);
            //logShare("library");
        });
    }

    private void initSortReset(View menu) {
        menu.findViewById(R.id.sortReset).setOnClickListener(this::clickReset);
    }

    private void resetMenu() {
        ViewGroup[] vgs = {sortAttributes, sortRanks,};
        for (ViewGroup vg : vgs) {
            setAllChildSelected(vg, false);
        }
//        sortCommon.check(R.id.sortCommonNormId);
//        sortFormulaCard.check(R.id.sortFormulaCardNo);
//        sortFormulaList.check(R.id.sortFormulaNo);
        setCheckedIncludeNo(sortSpecialNo, R.id.sortSpecialNo, sortSpecial);
        setCheckedIncludeNo(specialAll, specialAll, specialArea);
//        setCheckedIncludeNo(sortImproveNo, R.id.sortImproveNo, sortImprove);
//        search.setChecked(false);
    }

    // click listeners for sort menus --------
    private void clickReset(View v) {
        resetMenu();
        applySelection();
    }

    private void initSortByAttribute(View menu) {
        sortAttributes = initSortOf(menu, R.id.sortAttributes, this::clickAttr);
    }

    private void initSortByRank(View menu) {
        sortRanks = initSortOf(menu, R.id.sortRank, this::clickRank);
    }

    private void initSortBySpecial(View menu) {
        sortSpecial = initSortOf(menu, R.id.sortSpecialSkills, this::clickSpecial);
        sortSpecialNo = findViewById(R.id.sortSpecialNo);
        sortSpecialOneEnemy = findViewById(R.id.sortSpecialOneEnemy);
        sortSpecialAllEnemy = findViewById(R.id.sortSpecialAllEnemy);
    }

    private void initSortSpecial(View menu) {
        specialAll = findViewById(R.id.specialAll);
        specialSkill1 = findViewById(R.id.specialSkill1);
        specialSkill2 = findViewById(R.id.specialSkill2);
        specialSkill3 = findViewById(R.id.specialSkill3);
        specialSkill4 = findViewById(R.id.specialSkill4);
        specialEquip = findViewById(R.id.specialEquip);
        specialArea = initSortOf(menu, R.id.specialArea, this::clickSpecialDom);
    }

    private void clickAttr(View v) {
        nonAllApply(v, sortAttributes);
    }

    private void clickRank(View v) {
        nonAllApply(v, sortRanks);
    }

    private void clickSpecial(View v) {
        setCheckedIncludeNo(v, R.id.sortSpecialNo, sortSpecial);
        applySelection();
    }

    private void clickSpecialDom(View v) {
        setCheckedIncludeNo(v, specialAll, specialArea);
        applySelection();
    }

    private void nonAllApply(View v, ViewGroup vg) {
        toggleAndClearIfAll(v, vg);

        applySelection();
    }

    private <T extends ViewGroup> T initSortOf(View menu, @IdRes int id, View.OnClickListener childClick) {
        return setTargetChildClick(menu, id, childClick);
    }

    private void applySelection() {
        // Attribute
        Set<String> attrs = new HashSet<>();
        getSelectTags(sortAttributes, attrs, true);
        // Race
//        List<String> races = new ArrayList<>();
//        getSelectTags(sortRace, races, true);
        // Rank
        Set<String> ranks = new HashSet<>();
        getSelectTags(sortRanks, ranks, true);

        logE("-----Hero-----");
        logE("A = %s", attrs);
        logE("R = %s", ranks);

        if (heroLib.adapter != null) {
            LahSelectHero sel = new LahSelectHero();
            sel.attrs = attrs;
            sel.ranks = ranks;
            sel.adapter = heroLib.adapter;
            heroLib.adapter.setSelection(sel);
        }
    }

    private TaskMonitor.OnTaskState onCardsReady = new TaskMonitor.OnTaskState() {
        @Override
        public void onTaskDone(int index, String tag) {
            log("#%s (%s) is done", index, tag);
        }

        @Override
        public void onAllTaskDone() {
            if (getActivity() == null) return;

            getActivity().runOnUiThread(() -> {
                if (isActivityGone()) {
                    return;
                }

                Hero[] hs = LiveAHeroWiki.getAllHeros();

                log("All task OK");
                onHeroReady(hs);
                //logE("hero = %s", s);
            });
        }
    };

    private void onHeroReady(Hero[] all) {
        allHero.clear();
        allHero.addAll(Arrays.asList(all));
        int n = all.length;
        showToast(getString(R.string.hero_loaded, n));
        lahInfo.setText(getString(R.string.hero_selection, n, n));
        initLibrary();
    }

    private void initLibrary() {
        HeroLibrary lib = new HeroLibrary(recycler);
        lib.setViewMode(HeroLibrary.GRID_HERO);
        Map<Integer, HeroAdapter> adapters = lib.getAdapters();
        for (int i = 0; i < 4; i++) {
            HeroAdapter ha = adapters.get(i);
            ha.setItemListener(new HeroAdapter.ItemListener() {
                @Override
                public void onFiltered(int selected, int total) {
                    if (isActivityGone()) return;

                    lahInfo.setText(getString(R.string.hero_selection, selected, total));
                }
            });
        }
        heroLib = lib;
    }

    private void loadHero() {
        LiveAHeroWiki.attendDatabaseTasks(onCardsReady);
    }

    //--

    // Actual implementation of TosSelectCard --------
    private class LahSelectHero extends AllHero<Hero> {
        private Set<String> attrs;
        private Set<String> ranks;
        private HeroAdapter adapter;

        public LahSelectHero() {
            super(new ArrayList<>());
        }

        @NonNull
        @Override
        public List<Hero> from() {
            return adapter.getDataList();
        }

        @Override
        public void onPrepare() {

        }

        @Override
        public String typeName() {
            return "Hero";
        }

        @Override
        public boolean onSelect(Hero h) {
            return selectForBasic(h)
                && selectForSkill(h)
                ;
        }

        private boolean isSideMode() {
            if (adapter != null) {
                return HeroLibrary.isSideMode(adapter.getMode());
            } else {
                return false;
            }
        }

        // tod also for side mode
        private boolean selectForBasic(Hero c) {
            boolean inAttr;
            if (isSideMode()) {
                inAttr = true;
            } else {
                if (c.attribute.isEmpty()) { // sidekick only
                    inAttr = attrs.size() == 5; // no attr selected
                } else {
                    inAttr = attrs.contains(c.attribute);
                }
            }
            return inAttr
                && ranks.contains("" + c.rankFirst);
        }

        private boolean selectForSkill(Hero h) {
            if (sortSpecialNo.isChecked()) {
                return true;
            }

            List<String> src = getSkillSort(h);
            // todo how about select one & all?
            for (int i = 0; i < src.size(); i++) {
                String s = src.get(i);
                if (hasItsTag(s, sortSpecialOneEnemy)) {
                    return true;
                } else if (hasItsTag(s, sortSpecialAllEnemy)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasItsTag(String s, CheckBox c) {
            if (c.isChecked()) {
                String k = (String) c.getTag();
                if (s.contains(k)) {
                    return true;
                }
            }
            return false;
        }

        private List<String> getSkillSort(Hero h) {
            List<String> ans = new ArrayList<>();
            boolean isAll = specialAll.isChecked();
            if (isSideMode()) {
                if (isAll || specialSkill4.isChecked()) {
                    for (int i = 0; i < h.sideSkills.size(); i++) {
                        HeroSkill hs = h.sideSkills.get(i);
                        ans.add(hs.content);
                    }
                }
                if (isAll || specialEquip.isChecked()) {
                    for (int i = 0; i < h.sideEquips.size(); i++) {
                        SideSkill ss = h.sideEquips.get(i);
                        ans.add(ss.content);
                    }
                }
            } else {
                if (h.heroSkills.size() > 0) {
                    int plus = HeroUtil.getHeroSkillPlus(h);
                    CheckBox[] chk = {specialSkill1, specialSkill2, specialSkill3};
                    for (int i = 0; i < chk.length; i++) {
                        if (isAll || chk[i].isChecked()) {
                            HeroSkill hs = h.heroSkills.get(i);
                            ans.add(hs.content);
                            if (plus == i) {
                                HeroSkill hs2 = h.heroSkills.get(3);
                                ans.add(hs2.content);
                            }
                        }
                    }
                }
            }
            return ans;
        }


        @NonNull
        @Override
        public List<SelectedData> sort(@NonNull List<SelectedData> result) {
            return result;
        }
    }
    //--
}
