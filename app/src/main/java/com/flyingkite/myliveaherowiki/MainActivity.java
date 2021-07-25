package com.flyingkite.myliveaherowiki;

import android.os.Bundle;
import android.widget.TextView;

import com.flyingkite.myliveaherowiki.data.Hero;
import com.flyingkite.myliveaherowiki.lah.LiveAHeroWiki;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import flyingkite.tool.TaskMonitor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LiveAHeroWiki.attendDatabaseTasks(new TaskMonitor.OnTaskState() {
            @Override
            public void onTaskDone(int index, String tag) {
                runOnUiThread(() -> {
                    Hero[] hs = LiveAHeroWiki.getAllHeros();
                    String s = new Gson().toJson(hs);
                    TextView t = findViewById(R.id.mainText);
                    t.setText(s);
                });
            }
        });
    }
}