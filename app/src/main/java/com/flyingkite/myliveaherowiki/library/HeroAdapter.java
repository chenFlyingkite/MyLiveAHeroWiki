package com.flyingkite.myliveaherowiki.library;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyingkite.library.widget.RVAdapter;
import com.flyingkite.library.widget.RVSelectAdapter;
import com.flyingkite.myliveaherowiki.R;
import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.data.HeroRes;
import com.flyingkite.myliveaherowiki.data.HeroSkill;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeroAdapter extends RVAdapter<Hero, HeroAdapter.HeroVH, HeroAdapter.ItemListener> {

    public interface ItemListener extends RVSelectAdapter.ItemListener<Hero, HeroVH> {

    }

    private int viewMode = HeroLibrary.GRID_HERO;

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int mode) {
        viewMode = mode;
    }

    @Override
    public HeroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int id = R.layout.view_item_hero;
        if (viewMode == HeroLibrary.ROWS_HERO) {
            id = R.layout.view_row_hero;
        }
        View v = inflateView(parent, id);
        return new HeroVH(v);
    }

    @Override
    public void onBindViewHolder(HeroVH vh, int position) {
        super.onBindViewHolder(vh, position);
        Hero h = itemOf(position);
        vh.setHero(h);
    }

    public static class HeroVH extends RecyclerView.ViewHolder {
        private ImageView heroAttr;
        private ImageView heroIcon;
        private ImageView heroStar;
        private TextView heroName;
        private TextView heroMsg;
        private View heroArea;
        private HeroSkillView heroSkill1;
        private HeroSkillView heroSkill2;
        private HeroSkillView heroSkill3;

        private boolean sidekickOnly;

        public HeroVH(View v) {
            super(v);
            heroAttr = v.findViewById(R.id.hero_item_attr);
            heroIcon = v.findViewById(R.id.hero_item_icon);
            heroStar = v.findViewById(R.id.hero_item_star);
            heroName = v.findViewById(R.id.tiText);
            heroMsg = v.findViewById(R.id.tiMessage);
            heroArea = v.findViewById(R.id.heroSkillArea);
            createHeroSkill(v);
        }

        public void setHero(Hero h) {
            sidekickOnly = h.heroSkills.size() == 0;

            // Views
            if (heroMsg != null) {
                heroMsg.setVisibility(View.GONE);
            }
            if (heroName != null) {
                heroName.setText(h.nameJa);
                heroName.setVisibility(View.VISIBLE);
            }
            int image;
            int frame;
            if (sidekickOnly) {
                // side kick
                image = HeroRes.getSideImage(h);
                frame = HeroRes.getSideFrame(h);
            } else {
                image = HeroRes.getHeroImage(h);
                frame = HeroRes.getHeroFrame(h);
            }
            heroIcon.setImageResource(image);
            heroAttr.setImageResource(HeroRes.getAttrBgImage(h));
            heroStar.setImageResource(frame);
            // setup hero skill views
            setupHeroSkill(h);
        }

        private void setupHeroSkill(Hero h) {
            if (heroSkill1 == null || heroArea == null) return;

            heroArea.setVisibility(sidekickOnly ? View.GONE : View.VISIBLE);

            if (sidekickOnly) return;

            HeroSkill h0 = h.heroSkills.get(0);
            HeroSkill h1 = h.heroSkills.get(1);
            HeroSkill h2 = h.heroSkills.get(2);
            heroSkill1.setSkill1(h0);
            heroSkill2.setSkill1(h1);
            heroSkill3.setSkill1(h2);
            heroSkill1.setSkill2(null);
            heroSkill2.setSkill2(null);
            heroSkill3.setSkill2(null);
            // + skill
            if (h.heroSkills.size() >= 4) {
                HeroSkill s = h.heroSkills.get(3);
                if (s.name.startsWith(h0.name)) {
                    heroSkill1.setSkill2(s);
                } else if (s.name.startsWith(h1.name)) {
                    heroSkill2.setSkill2(s);
                } else if (s.name.startsWith(h2.name)) {
                    heroSkill3.setSkill2(s);
                }
            }
            heroSkill1.show();
            heroSkill2.show();
            heroSkill3.show();
        }

        private void createHeroSkill(View v) {
            heroSkill1 = makeSkillView(v, R.id.heroSkill1);
            heroSkill2 = makeSkillView(v, R.id.heroSkill2);
            heroSkill3 = makeSkillView(v, R.id.heroSkill3);
        }

        private HeroSkillView makeSkillView(View v, int id) {
            View w = v.findViewById(id);
            HeroSkillView h = null;
            if (w != null) {
                h = new HeroSkillView(w);
            }
            return h;
        }
    }

    public static class HeroSkillView {
        private HeroSkill skill1;
        private HeroSkill skill2;
        private boolean isSkill1;

        private TextView name;
        private TextView view;
        private TextView content;
        private ImageView skillPlus;

        public HeroSkillView(View v) {
            v.setOnClickListener((w) -> {
                setIsSkill1(!isSkill1);
                show();
            });
            isSkill1 = true;
            name = v.findViewById(R.id.heroSkillName);
            view = v.findViewById(R.id.heroSkillView);
            content = v.findViewById(R.id.heroSkillContent);
            skillPlus = v.findViewById(R.id.heroSkillPlus);
        }

        public void setSkill1(HeroSkill s) {
            skill1 = s;
        }

        public void setSkill2(HeroSkill s) {
            skill2 = s;
        }

        public void setIsSkill1(boolean b) {
            isSkill1 = b;
        }

        public void show() {
            if (isSkill1 || skill2 == null) {
                show(skill1);
            } else {
                show(skill2);
            }
        }

        private void show(HeroSkill s) {
            view.setText("" + s.view);
            name.setText("" + s.name);
            content.setText("" + s.content);
            skillPlus.setVisibility(skill2 != null ? View.VISIBLE : View.GONE);
        }
    }
}
