package com.flyingkite.myliveaherowiki.lah;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.flyingkite.library.TicTac2;
import com.flyingkite.library.log.Loggable;
import com.flyingkite.library.util.GsonUtil;
import com.flyingkite.library.util.ThreadUtil;
import com.flyingkite.myliveaherowiki.data.Hero;

import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import flyingkite.tool.TaskMonitor;

public class LiveAHeroWiki {
    // Main Contents
    private static Hero[] allHeros;
    // Tags for Task monitor
    public static final String TAG_ALL_HEROS = "AllHeros";
    public static final String[] TAG_ALL_TASKS = {
            TAG_ALL_HEROS,
    };

    public static void init(Context ctx) {
        AssetManager am = ctx.getAssets();
        ExecutorService p = ThreadUtil.cachedThreadPool;

        p.submit(() -> {
            TicTac2 t = new TicTac2();
            t.tic();
            allHeros = GsonUtil.loadAsset("hero.json", Hero[].class, am);
            t.tac("%s hero loaded", len(allHeros));
            monitorDB.notifyClientsState();
        });

//        p.submit(() -> {
//            TicTac2 t = new TicTac2.v();
//            t.tic();
//            File f = getTosCardFavorFile();
//            if (f.exists()) {
//                cardFavor = GsonUtil.loadFile(f, CardFavor.class);
//            }
//            if (cardFavor == null) {
//                cardFavor = new CardFavor();
//            }
//            logFavorite(cardFavor);
//            t.tac("%s cards favored", cardFavor.favors.size());
//            monitorDB.notifyClientsState();
//        });
//
//        p.submit(() -> {
//            TicTac2 t = new TicTac2.v();
//            t.tic();
//            mainStages = GsonUtil.loadAsset("mainStage.json", MainStage[].class, am);
//            t.tac("%s main stages loaded", mainStages.length);
//            monitorDB.notifyClientsState();
//        });
    }

    //--

    private static TaskMonitor.TaskOwner monitorSource = new TaskMonitor.TaskOwner() {
        @Override
        public int taskCount() {
            return TAG_ALL_TASKS.length;
        }

        @Override
        public boolean isTaskDone(int index) {
            String tag = TAG_ALL_TASKS[index];
            switch (tag) {
                case TAG_ALL_HEROS: return allHeros != null;
//                case TAG_ARM_CRAFTS: return armCrafts != null;
//                case TAG_NORMAL_CRAFTS: return normalCrafts != null;
//                case TAG_ALL_CARDS: return allCards != null;
//                case TAG_CARD_FAVOR: return cardFavor != null;
//                case TAG_MAIN_STAGE: return mainStages != null;
//                case TAG_RELIC_PASS: return relicStages != null;
//                case TAG_STORY_STAGE: return storyStages != null;
//                case TAG_REALM_STAGE: return realmStages != null;
//                case TAG_ULTIM_STAGE: return ultimStages != null;
//                case TAG_WEB_PIN: return webPin != null;
                //case TAG_AME_SKILL: return ameSkills != null;
                default:
                    throw new NullPointerException(taskCount() + " tasks but did not define done for " + index);
            }
        }

        @Override
        public String getTaskTag(int index) {
            return TAG_ALL_TASKS[index];
        }
    };
    private static TaskMonitor monitorDB = new TaskMonitor(monitorSource);

    // attend & absent
    // retain & remove
    public static void attendDatabaseTasks(@NonNull TaskMonitor.OnTaskState listener) {
        ExecutorService p = ThreadUtil.cachedThreadPool;
        p.submit(() -> {
            monitorDB.registerClient(listener);
        });
    }
    //--

    public static Hero[] getAllHeros() {
        return allHeros;
    }

    private static <T> int len(T[] a) {
        return a == null ? 0 : a.length;
    }

    private static Loggable z = new Loggable() {
        @Override
        public void log(String message) {
            Log.i(LTag(), message);
        }
    };
}
