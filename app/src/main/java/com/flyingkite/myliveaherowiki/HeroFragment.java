package com.flyingkite.myliveaherowiki;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.data.HeroSkill;
import com.flyingkite.myliveaherowiki.data.HeroUtil;
import com.flyingkite.myliveaherowiki.data.Heros;
import com.flyingkite.myliveaherowiki.data.SideSkill;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;
import com.flyingkite.myliveaherowiki.lah.query.AllHero;
import com.flyingkite.myliveaherowiki.library.HeroAdapter;
import com.flyingkite.myliveaherowiki.library.HeroLibrary;
import com.flyingkite.myliveaherowiki.util.ViewUtil;
import com.flyingkite.myliveaherowiki.util.select.SelectedData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import flyingkite.tool.TaskMonitor;

public class HeroFragment extends BaseFragment implements ViewUtil {

    private View gridList;
    private View heroSide;
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
    private CheckBox sortSpecialOneTeam;
    private CheckBox sortSpecialAllTeam;
    private CheckBox sortSpecialAttackUp;
    private CheckBox sortSpecialAttackDown;
    private CheckBox sortSpecialDefenseUp;
    private CheckBox sortSpecialDefenseDown;
    private CheckBox sortSpecialSpeedUp;
    private CheckBox sortSpecialSpeedDown;
    private CheckBox sortSpecialViewGet;
    private CheckBox sortSpecialChance;
    private CheckBox sortSpecialRecover;
    private CheckBox search;
    private RadioGroup sortCommon;
    private EditText searchText;
    private View searchClear;
    private View searchApply;

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
        gridList = findViewById(R.id.lahGridList);
        gridList.setOnClickListener((v) -> {
            int mode = heroLib.getViewMode();
            int next = mode ^ 0x1;
            updateMode(next);
            heroLib.setViewMode(next);
            applySelection();
            logE("mode = %s", mode);
        });

        heroSide = findViewById(R.id.lahModeHeroSide);
        heroSide.setOnClickListener((v) -> {
            int mode = heroLib.getViewMode();
            int next = mode ^ 0x2;
            updateMode(next);
            heroLib.setViewMode(next);
            applySelection();
            logE("mode = %s", mode);
        });

        lahInfo = findViewById(R.id.lahInfo);
        recycler = findViewById(R.id.lahRecycler);
        initScrollTools(findViewById(R.id.lahGoTop), findViewById(R.id.lahGoBottom), recycler);

        initSortMenu();
    }

    private void updateMode(int mode) {
        gridList.setSelected(HeroAdapter.isRowMode(mode));
        heroSide.setSelected(HeroAdapter.isSideMode(mode));
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
        initSortBy(menu);
        initSortByText();
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
        sortCommon.check(R.id.sortCommonId);
//        sortFormulaCard.check(R.id.sortFormulaCardNo);
//        sortFormulaList.check(R.id.sortFormulaNo);
        setCheckedIncludeNo(sortSpecialNo, R.id.sortSpecialNo, sortSpecial);
        setCheckedIncludeNo(specialAll, specialAll, specialArea);
//        setCheckedIncludeNo(sortImproveNo, R.id.sortImproveNo, sortImprove);
        search.setChecked(false);
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
        sortSpecialOneTeam = findViewById(R.id.sortSpecialOneTeam);
        sortSpecialAllTeam = findViewById(R.id.sortSpecialAllTeam);
        sortSpecialAttackUp = findViewById(R.id.sortSpecialAttackUp);
        sortSpecialAttackDown = findViewById(R.id.sortSpecialAttackDown);
        sortSpecialDefenseUp = findViewById(R.id.sortSpecialDefenseUp);
        sortSpecialDefenseDown = findViewById(R.id.sortSpecialDefenseDown);
        sortSpecialSpeedUp = findViewById(R.id.sortSpecialSpeedUp);
        sortSpecialSpeedDown = findViewById(R.id.sortSpecialSpeedDown);
        sortSpecialViewGet = findViewById(R.id.sortSpecialViewGet);
        sortSpecialChance = findViewById(R.id.sortSpecialChance);
        sortSpecialRecover = findViewById(R.id.sortSpecialRecover);
        search = findViewById(R.id.sortSearchUse);
    }

    private void initSortBy(View menu) {
        sortCommon = initSortOf(menu, R.id.sortCommonList, this::clickCommon);
    }

    private void clickCommon(View v) {
        int id = v.getId();
        sortCommon.check(id);

        applySelection();
    }

    private void initSortByText() {
        searchText = findViewById(R.id.searchText);
        searchClear = findViewById(R.id.searchClear);
        searchApply = findViewById(R.id.searchApply);

        searchClear.setOnClickListener((v) -> {
            searchText.setText("");
            clickSearch(v);
        });
        searchApply.setOnClickListener((v) -> {
            //logSearch(searchText.getText().toString());
            search.setChecked(true);
            clickSearch(v);
        });
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

    private void clickSearch(View v) {
        applySelection();
    }

    private <T extends ViewGroup> T initSortOf(View menu, @IdRes int id, View.OnClickListener childClick) {
        return setTargetChildClick(menu, id, childClick);
    }

    private void applySelection() {
        // Attribute
        Set<String> attrs = new HashSet<>();
        getSelectTags(sortAttributes, attrs, true);
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
        updateMode(heroLib.getViewMode());
    }

    private void initLibrary() {
        HeroLibrary lib = new HeroLibrary(recycler);
        lib.setViewMode(HeroAdapter.ROWS_HERO);
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
        private Map<String, Integer> orderRole = new HashMap<>();

        public LahSelectHero() {
            super(new ArrayList<>());
            String[] order = HeroUtil.roleOrder;
            for (int i = 0; i < order.length; i++) {
                orderRole.put(order[i], i);
            }
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
                return HeroAdapter.isSideMode(adapter.getMode());
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
            String find = searchText.getText().toString();
            boolean hasSearch = !find.isEmpty();
            // todo how about select one & all?
            for (int i = 0; i < src.size(); i++) {
                String s = src.get(i);
                if (hasRegexTag(s, sortSpecialOneEnemy)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialAllEnemy)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialOneTeam)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialAllTeam)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialAttackUp)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialAttackDown)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialDefenseUp)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialDefenseDown)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialSpeedUp)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialSpeedDown)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialViewGet)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialChance)) {
                    return true;
                } else if (hasRegexTag(s, sortSpecialRecover)) {
                    return true;
                } else if (hasSearch) {
                    if (s.contains(find)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean hasRegexTag(String s, CheckBox c) {
            if (c.isChecked()) {
                String k = (String) c.getTag();
                Pattern p = Pattern.compile(k);
                if (p.matcher(s).find()) {
                    return true;
                }
                // string
//                if (s.contains(k)) {
//                    return true;
//                }
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
            Comparator<SelectedData> cmp = getCommonComparator();
            if (cmp != null) {
                Collections.sort(result, cmp);
            }
            return result;
        }




        private Comparator<SelectedData> getCommonComparator() {
            // Create comparator
            int id = sortCommon.getCheckedRadioButtonId();
            if (id == RadioGroup.NO_ID) {
                return null;
            }
            return (o1, o2) -> {
                int k1 = o1.index;
                int k2 = o2.index;
                boolean dsc = false;
                Hero c1 = from().get(k1);
                Hero c2 = from().get(k2);
                int v1 = 0, v2 = 0;
                int id1 = Heros.getByJA(c1.nameJa).ordinal();
                int id2 = Heros.getByJA(c2.nameJa).ordinal();

                if (id == R.id.sortCommonId) {
                    v1 = id1;
                    v2 = id2;
                } else if (id == R.id.sortCommonRank) {
                    v1 = c1.rankFirst;
                    v2 = c2.rankFirst;
                } else if (id == R.id.sortCommonJob) {
                    v1 = orderRole.get(c1.role);
                    v2 = orderRole.get(c2.role);
                } else if (id == R.id.sortCommonHp) {
//                    v1 = c1;
//                    v2 = c2;
                } else if (id == R.id.sortCommonAttack) {
//                    v1 = c1.maxHPAme + c1.maxAttackAme + c1.maxRecoveryAme;
//                    v2 = c2.maxHPAme + c2.maxAttackAme + c2.maxRecoveryAme;
                } else if (id == R.id.sortCommonSpeed) {
//                } else if (id == R.id.sortCommonViewSkill1) {
//                    v1 = getHeroSkillView(c1, 0);
//                    v2 = getHeroSkillView(c2, 0);
                } else if (id == R.id.sortCommonViewSkill2) {
                    v1 = getHeroSkillView(c1, 1);
                    v2 = getHeroSkillView(c2, 1);
                } else if (id == R.id.sortCommonViewSkill3) {
                    v1 = getHeroSkillView(c1, 2);
                    v2 = getHeroSkillView(c2, 2);
                } else if (id == R.id.sortCommonViewSkillSide) {
                    if (isSideMode()) {
                        v1 = getSideSkillView(c1);
                        v2 = getSideSkillView(c2);
                    }
                } else if (id == 0) {
                } else {
                }

                int ans;
                if (dsc) {
                    ans = Integer.compare(v2, v1);
                } else {
                    ans = Integer.compare(v1, v2);
                }
                if (ans == 0) { // compare id if same
                    ans = Integer.compare(id1, id2);
                }
                return ans;
            };
        }

        private int getSideSkillView(Hero h) {
            int n = h.sideSkills.size();
            return h.sideSkills.get(n - 1).view;
        }

        private int getHeroSkillView(Hero h, int k) {
            int v = 0;
            int n = h.heroSkills.size();
            if (n > k) {
                v = h.heroSkills.get(k).view;
            }
            if (h.heroPlus == k && n > 3) {
                v = Math.min(v, h.heroSkills.get(3).view);
            }
            return v;
        }
    }
    //--
}
