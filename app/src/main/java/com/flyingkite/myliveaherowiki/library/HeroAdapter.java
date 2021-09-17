package com.flyingkite.myliveaherowiki.library;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyingkite.library.TicTac2;
import com.flyingkite.library.log.Loggable;
import com.flyingkite.library.util.ThreadUtil;
import com.flyingkite.library.widget.RVSelectAdapter;
import com.flyingkite.myliveaherowiki.R;
import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.data.HeroRes;
import com.flyingkite.myliveaherowiki.data.HeroSkill;
import com.flyingkite.myliveaherowiki.data.Heros;
import com.flyingkite.myliveaherowiki.data.SideSkill;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;
import com.flyingkite.myliveaherowiki.lah.query.AllHero;
import com.flyingkite.myliveaherowiki.util.select.SelectedData;
import com.flyingkite.myliveaherowiki.util.select.Selector;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeroAdapter extends RVSelectAdapter<Hero, HeroAdapter.HeroVH, HeroAdapter.ItemListener> implements Loggable {

    public interface ItemListener extends RVSelectAdapter.ItemListener<Hero, HeroVH> {
        default void onFiltered(int selected, int total) {}
    }

    private int mode;
    private final List<Hero> allHero = new ArrayList<>();
    private final List<Hero> allSide = new ArrayList<>();

    private Selector<Hero> selection;
    // current selection runnable
    private Runnable selectTask;
    private List<SelectedData> selectedResult = new ArrayList<>();

    public HeroAdapter() {
        Hero[] hs = LiveAHeroWiki.getAllHeros();
        for (int i = 0; i < hs.length; i++) {
            Hero h = hs[i];
            allHero.add(h);
            Heros he = Heros.getByJA(h.nameJa);
            if (he.isHero2()) {
            } else {
                allSide.add(h);
            }
        }
        setMode(HeroLibrary.GRID_HERO);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int m) {
        mode = m;
        if (HeroLibrary.isSideMode(m)) {
            setDataList(allSide);
        } else {
            setDataList(allHero);
        }
    }

    public List<Hero> getDataList() {
        return dataList;
    }

    @Override
    public boolean hasSelection() {
        return selectTask != null;
    }

    public void setSelection(Selector<Hero> s) {
        if (selection != null) {
            selection.setCancelled(true);
        }
        selectTask = getSearchTask(s);
        ThreadUtil.cachedThreadPool.submit(selectTask);
    }

    private Runnable getSearchTask(Selector<Hero> s) {
        return new Runnable() {
            @Override
            public void run() {
                selection = s == null ? new AllHero<>(dataList) : s;
                if (selectTask != this) return;
                // perform query and projection on index
                List<SelectedData> done = selection.query();
                List<Integer> index = SelectedData.getIndices(done);

                ThreadUtil.runOnUiThread(() -> {
                    if (selectTask != this) return;

                    selectedResult = done;
                    selectedIndices = index;
                    notifyDataSetChanged();
                    notifyFiltered();
                });
            }
        };
    }

    @Override
    public HeroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int id;
        if (HeroLibrary.isRowMode(mode)) {
            id = R.layout.view_row_hero;
        } else {
            id = R.layout.view_item_hero;
        }
        View v = inflateView(parent, id);
        return new HeroVH(v);
    }

    @Override
    public void onBindViewHolder(HeroVH vh, int position) {
//        t.tic();
        super.onBindViewHolder(vh, position);
        Hero h = itemOf(position);
//        t.tic();
        logE("setHero #%2d, %s", position, h);
        vh.setHero(h, mode); // slow ?
//        t.tac("setHero");
//        t.tac("end onBind");
    }
    TicTac2 t = new TicTac2();

    private void notifyFiltered() {
        if (onItem != null) {
            onItem.onFiltered(selectedResult.size(), dataList.size());
        }
    }

    public static class HeroVH extends RecyclerView.ViewHolder {
        private View heroSkills;

        private ImageView heroImage;
        private TextView heroName;
        private HeroSkillView heroSkill1;
        private HeroSkillView heroSkill2;
        private HeroSkillView heroSkill3;

        private boolean sidekickOnly;

        public HeroVH(View v) {
            super(v);
            heroName = v.findViewById(R.id.heroText);
            heroImage = v.findViewById(R.id.heroImage);
            heroSkills = v.findViewById(R.id.heroSkillArea);
            makeHeroSkill(v);
        }

        TicTac2 t = new TicTac2();
        public void setHero(Hero h, int mode) {
//            t.tic();
//            t.tic();
            sidekickOnly = h.heroSkills.size() == 0;
            // Views
            if (heroName != null) {
                heroName.setText(h.nameJa);
                heroName.setVisibility(View.VISIBLE);
            }
            boolean side = HeroLibrary.isSideMode(mode);
            boolean row = HeroLibrary.isRowMode(mode);
//            t.tac("setNames");
//            t.tic();
            setHeroImage(h, side); // slow
//            t.tac("setImage");
            // setup hero skill views
//            t.tic();
            setMode();
//            t.tac("setMode");
//            t.tic();
            if (row) {
                if (side) {
                    setupSideSkill(h);
                } else {
                    setupHeroSkill(h);
                }
            }
//            t.tac("setSkill"); // slow
//            t.tac("end");
        }

        private void setHeroImage(Hero h, boolean isSideMode) {
            int res;
            if (isSideMode) {
                res = HeroRes.getSideBadge(h);
            } else {
                if (sidekickOnly) {
                    res = HeroRes.getSideBadge(h);
                } else {
                    res = HeroRes.getHeroBadge(h);
                }
            }
            heroImage.setImageResource(res);
        }

        private void setMode() {
            if (heroSkills != null) {
                heroSkills.setVisibility(sidekickOnly ? View.GONE : View.VISIBLE);
            }
        }

        private void setupSideSkill(Hero h) {
            heroSkill1.reset();
            heroSkill1.heroSkills.addAll(h.sideSkills);
            heroSkill1.show();

            // convert SideSkill into HeroSkill and view = -1, to make view to be gone
            heroSkill2.reset();
            for (int j = 0; j < h.sideEquips.size(); j++) {
                HeroSkill hh = new HeroSkill();
                SideSkill ss = h.sideEquips.get(j);
                hh.view = -1;
                hh.name = ss.name;
                hh.content = ss.content;
                heroSkill2.heroSkills.add(hh);
            }
            heroSkill2.show();

            heroSkill3.reset();
            heroSkill3.root.setVisibility(View.GONE);
        }

        private void setupHeroSkill(Hero h) {
            HeroSkillView[] hs = {heroSkill1, heroSkill2, heroSkill3};
            for (int i = 0; i < hs.length; i++) {
                HeroSkillView hi = hs[i];
                hi.reset();
                if (i >= h.heroSkills.size()) {
                    continue; // Sidekick only
                }
                HeroSkill x = h.heroSkills.get(i);
                hi.heroSkills.add(x);
                // + skill
                if (h.heroSkills.size() >= 4) {
                    HeroSkill s = h.heroSkills.get(3);
                    if (s.name.startsWith(x.name)) {
                        hi.heroSkills.add(s);
                        hi.plusAt = 1;
                    }
                }
                hi.show();
            }
        }

        private void makeHeroSkill(View v) {
            heroSkill1 = makeHSView(v, R.id.heroSkill1);
            heroSkill2 = makeHSView(v, R.id.heroSkill2);
            heroSkill3 = makeHSView(v, R.id.heroSkill3);
        }

        private HeroSkillView makeHSView(View v, int id) {
            View w = v.findViewById(id);
            HeroSkillView h = null;
            if (w != null) {
                h = new HeroSkillView(w);
            }
            return h;
        }

        private SideSkillView makeSSView(View v, int id) {
            View w = v.findViewById(id);
            SideSkillView h = null;
            if (w != null) {
                h = new SideSkillView(w);
            }
            return h;
        }
    }

    public static class HeroSkillView {
        private View root;
        private TextView name;
        private TextView view;
        private TextView content;
        private ImageView skillPlus;
        private List<HeroSkill> heroSkills = new ArrayList<>();
        private int showAt;
        private int plusAt = -1;

        public HeroSkillView(View v) {
            v.setOnClickListener((w) -> {
                showNextSkill();
            });
            root = v;
            name = v.findViewById(R.id.heroSkillName);
            view = v.findViewById(R.id.heroSkillView);
            content = v.findViewById(R.id.heroSkillContent);
            skillPlus = v.findViewById(R.id.heroSkillPlus);
        }

        private void reset() {
            heroSkills.clear();
            showAt = 0;
            plusAt = -1;
        }

        private void showNextSkill() {
            showAt++;
            if (showAt >= heroSkills.size()) {
                showAt = 0;
            }
            show();
        }

        private void show() {
            show(heroSkills.get(showAt));
        }

        @SuppressLint("SetTextI18n")
        private void show(HeroSkill s) {
            if (s.view < 0) {
                view.setText("");
            } else {
                view.setText("" + s.view);
            }
            name.setText("" + s.name);
            content.setText("" + s.content);
            skillPlus.setVisibility(heroSkills.size() > 1 ? View.VISIBLE : View.GONE);
            //skillPlus.setVisibility(plusAt == showAt ? View.VISIBLE : View.GONE);
        }
    }

    public static class SideSkillView {
        // name and contents same size
        private List<String> names = new ArrayList<>();
        private List<String> contents = new ArrayList<>();
        private int level;

        private TextView name;
        private TextView content;

        public SideSkillView(View v) {
            v.setOnClickListener((w) -> {
                level++;
                if (level >= names.size()) {
                    level = 0;
                }
                show();
            });
            name = v.findViewById(R.id.sideSkillName);
            content = v.findViewById(R.id.sideSkillContent);
        }

        public void show() {
            if (level < names.size()) {
                name.setText(names.get(level));
                content.setText(contents.get(level));
            }
        }

        public void setHeroSkill(List<HeroSkill> li) {
            names.clear();
            contents.clear();
            for (int i = 0; i < li.size(); i++) {
                HeroSkill h = li.get(i);
                names.add(h.name);
                contents.add(h.content);
            }
        }

        public void setSideSkill(List<SideSkill> li) {
            names.clear();
            contents.clear();
            for (int i = 0; i < li.size(); i++) {
                SideSkill h = li.get(i);
                names.add(h.name);
                contents.add(h.content);
            }
        }
    }
}
