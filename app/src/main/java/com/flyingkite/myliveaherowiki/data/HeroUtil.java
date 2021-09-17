package com.flyingkite.myliveaherowiki.data;

import java.util.List;

public class HeroUtil {

    public static int getHeroSkillPlus(Hero h) {
        List<HeroSkill> li = h.heroSkills;
        if (li.size() >= 4) {
            HeroSkill s = li.get(3);
            for (int i = 0; i < 3; i++) {
                HeroSkill x = li.get(i);
                if (s.name.startsWith(x.name)) {
                    return i;
                }
            }
        }
        return -1;
    }

}
