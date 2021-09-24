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

    //-- Hero modes
    public static final int GRID_HERO = 0;
    public static final int ROWS_HERO = 1;
    public static final int GRID_SIDEKICK = 2;
    public static final int ROWS_SIDEKICK = 3;

    public static boolean isSideMode(int m) {
        return m == GRID_SIDEKICK || m == ROWS_SIDEKICK;
    }

    public static boolean isRowMode(int m) {
        return m == ROWS_HERO || m == ROWS_SIDEKICK;
    }
    //--

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
        setMode(GRID_HERO);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int m) {
        mode = m;
        if (isSideMode(m)) {
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
        if (isRowMode(mode)) {
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
    private TicTac2 t = new TicTac2();

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

        public void setHero(Hero h, int mode) {
            TicTac2 t = new TicTac2();
            t.enable(false);
            t.tic();
            t.tic();
            sidekickOnly = h.heroSkills.size() == 0;
            // Views
            if (heroName != null) {
                heroName.setText(h.nameJa);
                heroName.setVisibility(View.VISIBLE);
            }
            boolean side = isSideMode(mode);
            boolean row = isRowMode(mode);
            t.tac("setNames");
            t.tic();
            setHeroImage(h, side); // slow
            t.tac("setHeroImage");
            // setup hero skill views
            t.tic();
            setMode(side);
            t.tac("setMode");
            t.tic();
            if (row) {
                if (side) {
                    setupSideSkill(h);
                } else {
                    setupHeroSkill(h);
                }
            }
            t.tac("setSkill"); // slow
            t.tac("setHero");
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

        private void setMode(boolean isSideMode) {
            if (heroSkills != null) {
                boolean show = isSideMode || !sidekickOnly;
                heroSkills.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }

        private void setupSideSkill(Hero h) {
            //--
            heroSkill1.reset();
            heroSkill1.source = new HSViewData() {
                @Override
                public int getCount() {
                    return h.sideSkills.size();
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void show(int index) {
                    HeroSkill x = h.sideSkills.get(index);
                    heroSkill1.view.setText("" + x.view);
                    heroSkill1.name.setText(x.name);
                    heroSkill1.content.setText(x.content);
                    heroSkill1.skillPlus.setVisibility(getCount() > 1 ? View.VISIBLE : View.GONE);
                }
            };
            heroSkill1.show();

            //--
            heroSkill2.reset();
            heroSkill2.source = new HSViewData() {
                @Override
                public int getCount() {
                    return h.sideEquips.size();
                }

                @Override
                public void show(int index) {
                    SideSkill ss = h.sideEquips.get(index);
                    heroSkill2.view.setText("");
                    heroSkill2.name.setText(ss.name);
                    heroSkill2.content.setText(ss.content);
                    heroSkill2.skillPlus.setVisibility(View.VISIBLE);
                }
            };
            heroSkill2.show();

            //--
            heroSkill3.reset();
            heroSkill3.root.setVisibility(View.GONE);
        }

        private void setupHeroSkill(Hero h) {
            HeroSkillView[] hs = {heroSkill1, heroSkill2, heroSkill3};
            int n = h.heroSkills.size();
            for (int i = 0; i < hs.length; i++) {
                HeroSkillView hi = hs[i];
                hi.reset();
                if (i >= n) {
                    continue; // Sidekick only
                }
                final int atI = i;
                hi.source = new HSViewData() {
                    @Override
                    public int getCount() {
                        return h.heroPlus == atI ? 2 : 1;
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void show(int index) {
                        HeroSkill x;
                        if (index != 0) {
                            x = h.heroSkills.get(3); // plus skill is at last
                        } else {
                            x = h.heroSkills.get(atI);
                        }
                        hi.view.setText("" + x.view);
                        hi.name.setText(x.name);
                        hi.content.setText(x.content);
                        hi.skillPlus.setVisibility(getCount() > 1 ? View.VISIBLE : View.GONE);
                    }
                };
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
    }

    private interface HSViewData {
        int getCount();
        void show(int index);
    }

    public static class HeroSkillView {
        private View root;
        private TextView name;
        private TextView view;
        private TextView content;
        private ImageView skillPlus;
        private int showAt;
        private HSViewData source;

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
            showAt = 0;
        }

        private void showNextSkill() {
            showAt++;
            int n = source == null ? 0 : source.getCount();
            if (showAt >= n) {
                showAt = 0;
            }
            show();
        }

        private void show() {
            if (source != null) {
                source.show(showAt);
            }
        }
    }
}
