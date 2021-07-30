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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeroAdapter extends RVAdapter<Hero, HeroAdapter.HeroVH, HeroAdapter.ItemListener> {

    public interface ItemListener extends RVSelectAdapter.ItemListener<Hero, HeroVH> {

    }

    @Override
    public HeroVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflateView(parent, R.layout.view_item_hero);
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

        public HeroVH(View v) {
            super(v);
            heroAttr = v.findViewById(R.id.hero_item_attr);
            heroIcon = v.findViewById(R.id.hero_item_icon);
            heroStar = v.findViewById(R.id.hero_item_star);
            heroName = v.findViewById(R.id.tiText);
            heroMsg = v.findViewById(R.id.tiMessage);
        }

        public void setHero(Hero h) {
            heroMsg.setVisibility(View.GONE);
            heroName.setText(h.nameJa);
            heroName.setVisibility(View.VISIBLE);
            heroIcon.setImageResource(HeroRes.getHeroImage(h));
            heroAttr.setImageResource(HeroRes.getAttrImage(h));
            heroStar.setImageResource(HeroRes.getHeroFrame(h));
        }
    }
}
