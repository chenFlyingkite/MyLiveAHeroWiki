package com.flyingkite.myliveaherowiki.data;

import com.flyingkite.myliveaherowiki.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HeroRes {
    private static final Map<String, Integer> heroImage = new HashMap<>();
    private static final Map<String, Integer> sideImage = new HashMap<>();
    private static final Map<String, Integer> attrImage = new HashMap<>();
    private static final Map<String, Integer> attrBgImage = new HashMap<>();
    private static final Map<Integer, Integer> rankImage = new HashMap<>();
    private static final Set<String> side4star = new HashSet<>();
    static {
        attr();
        hero();
    }

    private static void attr() {
        attrImage.put("水", R.drawable.icon_attr_water);
        attrImage.put("火", R.drawable.icon_attr_fire);
        attrImage.put("木", R.drawable.icon_attr_wood);
        attrImage.put("光", R.drawable.icon_attr_light);
        attrImage.put("影", R.drawable.icon_attr_dark);

        attrBgImage.put("水", R.drawable.ui_frame_h_base_water);
        attrBgImage.put("火", R.drawable.ui_frame_h_base_fire);
        attrBgImage.put("木", R.drawable.ui_frame_h_base_wood);
        attrBgImage.put("光", R.drawable.ui_frame_h_base_light);
        attrBgImage.put("影", R.drawable.ui_frame_h_base_dark);

        side4star.add("アカシ");
        side4star.add("モクダイ");
        side4star.add("スイ");
        side4star.add("フラミー");
        side4star.add("ロレン");
        side4star.add("ゴロウ");
        side4star.add("ハックル");
        side4star.add("カラスキ");
        side4star.add("メリデ");
        rankImage.put(1, R.drawable.ui_frame_h_00);
        rankImage.put(3, R.drawable.ui_frame_h_01);
        rankImage.put(4, R.drawable.ui_frame_h_02);
        rankImage.put(5, R.drawable.ui_frame_h_03);
    }

    private static void hero() {
        heroImage.put("アカシ", R.drawable.icon_akashi_h01);
        sideImage.put("アカシ", R.drawable.icon_akashi_s01);
        heroImage.put("モクダイ", R.drawable.icon_mokdai_h01);
        sideImage.put("モクダイ", R.drawable.icon_mokdai_s01);
        heroImage.put("スイ", R.drawable.icon_sui_h01);
        sideImage.put("スイ", R.drawable.icon_sui_s01);
        heroImage.put("ライキ", R.drawable.icon_ryekie_h01);
        sideImage.put("ライキ", R.drawable.icon_ryekie_s01);
        heroImage.put("酔虎のライキ", R.drawable.icon_ryekie2_h01);
        sideImage.put("酔虎のライキ", R.drawable.icon_ryekie_s01);
        heroImage.put("クローネ", R.drawable.icon_crowne_h01);
        sideImage.put("クローネ", R.drawable.icon_crowne_s01);
        heroImage.put("ガンメイ", R.drawable.icon_gammei_h01);
        sideImage.put("ガンメイ", R.drawable.icon_gammei_s01);
        heroImage.put("バレル", R.drawable.icon_barrel_h01);
        sideImage.put("バレル", R.drawable.icon_barrel_s01);
        heroImage.put("ハロン", R.drawable.icon_furlong_h01);
        sideImage.put("ハロン", R.drawable.icon_furlong_s01);
        heroImage.put("ヴィクトム", R.drawable.icon_victom_h01);
        sideImage.put("ヴィクトム", R.drawable.icon_victom_s01);
        heroImage.put("キョウイチ", R.drawable.icon_kyoichi_h01);
        sideImage.put("キョウイチ", R.drawable.icon_kyoichi_s01);
        heroImage.put("潜行のキョウイチ", R.drawable.icon_kyoichi2_h01);
        sideImage.put("潜行のキョウイチ", R.drawable.icon_kyoichi_s01);
        heroImage.put("フラミー", R.drawable.icon_flamier_h01);
        sideImage.put("フラミー", R.drawable.icon_flamier_s01);
        heroImage.put("ショウエン", R.drawable.icon_shoen_h01);
        sideImage.put("ショウエン", R.drawable.icon_shoen_s01);
        heroImage.put("隠密のショウエン", R.drawable.icon_shoen2_h01);
        sideImage.put("隠密のショウエン", R.drawable.icon_shoen_s01);
        heroImage.put("トウシュウ", R.drawable.icon_toshu_h01);
        sideImage.put("トウシュウ", R.drawable.icon_toshu_s01);
        heroImage.put("マルフィク", R.drawable.icon_marfik_h01);
        sideImage.put("マルフィク", R.drawable.icon_marfik_s01);
        heroImage.put("探険のマルフィク", R.drawable.icon_marfik2_h01);
        sideImage.put("探険のマルフィク", R.drawable.icon_marfik_s01);
        heroImage.put("ポラリスマスク", R.drawable.icon_polaris_h01);
        sideImage.put("ポラリスマスク", R.drawable.icon_polaris_s01);
        heroImage.put("コウキ", R.drawable.icon_kouki_h01);
        sideImage.put("コウキ", R.drawable.icon_kouki_s01);
        heroImage.put("ヒトミ", R.drawable.icon_hitomi_h01);
        sideImage.put("ヒトミ", R.drawable.icon_hitomi_s01);
        heroImage.put("ラクタ", R.drawable.icon_rakta_h01);
        sideImage.put("ラクタ", R.drawable.icon_rakta_s01);
        heroImage.put("ロレン", R.drawable.icon_loren_h01);
        sideImage.put("ロレン", R.drawable.icon_loren_s01);
        heroImage.put("イサリビ", R.drawable.icon_isaribi_h01);
        sideImage.put("イサリビ", R.drawable.icon_isaribi_s01);
        heroImage.put("ゴロウ", R.drawable.icon_goro_h01);
        sideImage.put("ゴロウ", R.drawable.icon_goro_s01);
        heroImage.put("ディグラム", R.drawable.icon_digram_h01);
        sideImage.put("ディグラム", R.drawable.icon_digram_s01);
        heroImage.put("アンドリュー", R.drawable.icon_andrew_h01);
        sideImage.put("アンドリュー", R.drawable.icon_andrew_s01);
        heroImage.put("アルキバ", R.drawable.icon_alchiba_h01);
        sideImage.put("アルキバ", R.drawable.icon_alchiba_s01);
        heroImage.put("追跡のアルキバ", R.drawable.icon_alchiba2_h01);
        sideImage.put("追跡のアルキバ", R.drawable.icon_alchiba_s01);
        heroImage.put("スバル", R.drawable.icon_subaru_h01);
        sideImage.put("スバル", R.drawable.icon_subaru_s01);
        heroImage.put("キルシュ", R.drawable.icon_kirsch_h01);
        sideImage.put("キルシュ", R.drawable.icon_kirsch_s01);
        heroImage.put("ナリヒト", R.drawable.icon_narihito_h01);
        sideImage.put("ナリヒト", R.drawable.icon_narihito_s01);
        heroImage.put("スハイル", R.drawable.icon_suhail_h01);
        sideImage.put("スハイル", R.drawable.icon_suhail_s01);
        heroImage.put("モノマサ", R.drawable.icon_monomasa_h01);
        sideImage.put("モノマサ", R.drawable.icon_monomasa_s01);
        heroImage.put("プロキー", R.drawable.icon_procy_h01);
        sideImage.put("プロキー", R.drawable.icon_procy_s01);
        heroImage.put("ゴメイサ", R.drawable.icon_gomeisa_h01);
        sideImage.put("ゴメイサ", R.drawable.icon_gomeisa_s01);
        //heroImage.put("ハックル", R.drawable.icon_huckle_s01); // no hero
        sideImage.put("ハックル", R.drawable.icon_huckle_s01);
        heroImage.put("木のウルフマン", R.drawable.icon_wolfman_wood_h01);
        sideImage.put("木のウルフマン", R.drawable.icon_wolfman_wood_s01);
        heroImage.put("影のウルフマン", R.drawable.icon_wolfman_dark_h01);
        sideImage.put("影のウルフマン", R.drawable.icon_wolfman_dark_s01);
        heroImage.put("ネッセン", R.drawable.icon_nessen_h01);
        sideImage.put("ネッセン", R.drawable.icon_nessen_s01);
        heroImage.put("ヒサキ", R.drawable.icon_hisaki_h01);
        sideImage.put("ヒサキ", R.drawable.icon_hisaki_s01);
        heroImage.put("マクラータ", R.drawable.icon_maculata_h01);
        sideImage.put("マクラータ", R.drawable.icon_maculata_s01);
        heroImage.put("ルティリクス", R.drawable.icon_rutilix_h01);
        sideImage.put("ルティリクス", R.drawable.icon_rutilix_s01);
        heroImage.put("アルフェッカ", R.drawable.icon_alphecca_h01);
        sideImage.put("アルフェッカ", R.drawable.icon_alphecca_s01);
        heroImage.put("シャフト", R.drawable.icon_shaft_h01);
        sideImage.put("シャフト", R.drawable.icon_shaft_s01);
        heroImage.put("カラスキ", R.drawable.icon_kalaski_h01);
        sideImage.put("カラスキ", R.drawable.icon_kalaski_s01);
        //heroImage.put("メリデ", R.drawable.icon_melide_s01); // no hero
        sideImage.put("メリデ", R.drawable.icon_melide_s01);
        heroImage.put("ヨシオリ", R.drawable.icon_yoshiori_h01);
        sideImage.put("ヨシオリ", R.drawable.icon_yoshiori_s01);
        //heroImage.put("主人公", R.drawable.icon_player_s01); // no hero
        sideImage.put("主人公", R.drawable.icon_player_s01);
    }

    public static int getHeroImage(Hero h) {
        return get(h, (he) -> {
            return heroImage.get(h.nameJa);
        });
    }

    public static int getSideImage(Hero h) {
        return get(h, (he) -> {
            return sideImage.get(h.nameJa);
        });
    }

    public static int getAttrImage(Hero h) {
        return get(h, (he) -> {
            return attrImage.get(h.attribute);
        });
    }

    public static int getAttrBgImage(Hero h) {
        return get(h, (he) -> {
            return attrBgImage.get(h.attribute);
        });
    }

    public static int getHeroFrame(Hero h) {
        return get(h, (he) -> {
            return rankImage.get(h.rankFirst);
        });
    }

    public static int getSideFrame(Hero h) {
        return get(h, (he) -> {
            if (side4star.contains(h.nameJa)) {
                return R.drawable.ui_frame_s_02;
            }
            return R.drawable.ui_frame_s_01;
        });
    }

    //-- Interface to simplify fetch
    private interface Got {
        Integer get(Hero h);
    }

    // Try to get it from g, and return 0 if null
    private static int get(Hero h, Got g){
        if (h != null) {
            Integer id = g.get(h);
            if (id != null) {
                return id;
            }
        }
        return 0;
    }
}
