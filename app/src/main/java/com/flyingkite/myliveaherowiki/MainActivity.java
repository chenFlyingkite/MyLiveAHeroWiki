package com.flyingkite.myliveaherowiki;

import android.os.Bundle;
import android.widget.TextView;

import com.flyingkite.library.widget.Library;
import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;
import com.flyingkite.myliveaherowiki.library.HeroAdapter;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import flyingkite.tool.TaskMonitor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadHero();
    }

    private void initLibrary() {
        Library<HeroAdapter> lib = new Library<>(findViewById(R.id.heroLib), 5);
        HeroAdapter ha = new HeroAdapter();

        List<Hero> allHero = Arrays.asList(LiveAHeroWiki.getAllHeros());
        ha.setDataList(allHero);
        lib.setViewAdapter(ha);
    }

    private void loadHero() {
        LiveAHeroWiki.attendDatabaseTasks(new TaskMonitor.OnTaskState() {
            @Override
            public void onTaskDone(int index, String tag) {
                runOnUiThread(() -> {
                    Hero[] hs = LiveAHeroWiki.getAllHeros();
                    String s = new Gson().toJson(hs);
                    TextView t = findViewById(R.id.mainText);
                    t.setText(s);
                    initLibrary();
                });
            }
        });
    }
}